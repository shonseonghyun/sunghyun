package com.sunghyun.schedule.application.service;

import com.sunghyun.feign.dto.InternalGuestRecruitmentDto;
import com.sunghyun.feign.dto.InternalLocationResponseDto;
import com.sunghyun.feign.dto.InternalSchedulePollConflictResponseDto;
import com.sunghyun.feign.dto.InternalSchedulePollResponseDto;
import com.sunghyun.schedule.application.dto.req.SchedulePollCreateReqDto;
import com.sunghyun.schedule.application.dto.req.SchedulePollLocationSetReqDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollMinPlayerMetResDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollResDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollSimpleResDto;
import com.sunghyun.schedule.application.port.in.SchedulePollUseCase;
import com.sunghyun.schedule.application.port.out.GuestQueryPort;
import com.sunghyun.schedule.application.port.out.TeamQueryPort;
import com.sunghyun.schedule.domain.Location;
import com.sunghyun.schedule.domain.SchedulePoll;
import com.sunghyun.schedule.domain.enums.AttendanceStatus;
import com.sunghyun.schedule.domain.enums.MinPlayerStatusChange;
import com.sunghyun.schedule.domain.exception.AccessDeniedException;
import com.sunghyun.schedule.domain.exception.NoJoinedTeamException;
import com.sunghyun.schedule.domain.exception.NotFoundLocationException;
import com.sunghyun.schedule.domain.exception.NotFoundSchedulePollException;
import com.sunghyun.schedule.domain.repository.LocationRepository;
import com.sunghyun.schedule.domain.repository.SchedulePollRepository;
import com.sunghyun.team.domain.model.TeamMember;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulePollService implements SchedulePollUseCase {
    private final SchedulePollRepository schedulePollRepository;
    private final LocationRepository locationRepository;
    private final TeamQueryPort teamQueryPort;
    private final GuestQueryPort guestQueryPort;

    @Override
    @Transactional
    public void createSchedulePoll(Long memberNo, SchedulePollCreateReqDto reqDto) {
        // 팀 모듈에 리더 여부 검증 요청
        if(!teamQueryPort.isTeamLeader(reqDto.getTeamNo(),memberNo)){
            throw new AccessDeniedException(ErrorCode.T05);
        }

        List<TeamMember> teamMembers = teamQueryPort.getTeamMembers(reqDto.getTeamNo());
        List<Long> teamMembersNo = teamMembers.stream().map(TeamMember::getTeamMemberNo)
                .toList();

        SchedulePoll newSchedulePoll = SchedulePoll.create(
                reqDto.getTeamNo(),
                reqDto.getTitle(),
                reqDto.getMatchType(),
                reqDto.getPlayDt(),
                reqDto.getPlayTm(),
                reqDto.getLocation(),
                reqDto.getExpectedAmount(),
                reqDto.getMin(),
                reqDto.getMax(),
                reqDto.getClosedDt(),
                reqDto.getClosedTm(),
                teamMembersNo
        );

        SchedulePoll savedSchedulePoll = schedulePollRepository.save(newSchedulePoll);

        //이게 메인 로직에 영향을 미치네..?
        if(reqDto.getLocation() != null){
            Location savedLocation = locationRepository.save(reqDto.getLocation());
        }

        // TODO:
        //  팀 회원들에게 알림
    }

    @Override
    public SchedulePollResDto getSchedulePoll(Long schedulePollNo, Long memberNo) {
        // 도메인 조회
        SchedulePoll selectedSchedulePoll = schedulePollRepository.findBySchedulePollNo(schedulePollNo)
                .orElseThrow(()-> new NotFoundSchedulePollException(ErrorCode.P001));


        // 장소 도메인 조회
//        Location selectedLocation = locationRepository.findByLocationNo(selectedSchedulePoll.getMatchInfo().getLocation().getLocationNo())
//                .orElseThrow(()->new NotFoundLocationException(ErrorCode.P005));

        // 장소 미정 체크
        if(selectedSchedulePoll.getMatchInfo().getLocation() != null) {
            // 있는데 조회가 안되는 경우
            Location selectedLocation = locationRepository.findByLocationNo(selectedSchedulePoll.getMatchInfo().getLocation().getLocationNo())
                    .orElseThrow(() -> new NotFoundLocationException(ErrorCode.P005));
            selectedSchedulePoll.setLocation(selectedLocation);
        }

        Long teamNo = selectedSchedulePoll.getTeamNo();

        Long myTeamMemberNo = getValidTeamMemberNo(teamNo, memberNo);

        GlobalResponse<InternalGuestRecruitmentDto> guestRecruitmentResponseDto = guestQueryPort.getGuestRecruitment(schedulePollNo);

        return SchedulePollResDto.from(selectedSchedulePoll,myTeamMemberNo,guestRecruitmentResponseDto.getData());
    }

    @Override
    public List<SchedulePollSimpleResDto> getSchedulePolls(Long memberNo, Long lastSchedulePollNo, int size) {
        // 팀 멤버 검증
//        if (!teamQueryPort.isTeamMember(teamNo, memberNo)) {
//            throw new AccessDeniedException(ErrorCode.T05);
//        }

        Long teamNo = teamQueryPort.getTeamNoByMemberNo(memberNo)
                .orElseThrow(()->new NoJoinedTeamException(ErrorCode.T009));

        // memberNo + teamNo 조합으로 해당 팀에서의 myTeamMemberNo 조회
        Long myTeamMemberNo = teamQueryPort.getTeamMemberNo(teamNo, memberNo);

        List<SchedulePoll> polls = schedulePollRepository.findAllByTeamNoNoOffset(teamNo,lastSchedulePollNo,size);

        return polls.stream()
                .map(poll -> SchedulePollSimpleResDto.from(poll, myTeamMemberNo))
                .toList();
    }

    @Override
    @Transactional
    public void vote(Long memberNo, Long schedulePollNo, AttendanceStatus status) {

        SchedulePoll selectedSchedulePoll = schedulePollRepository.findBySchedulePollNo(schedulePollNo)
                .orElseThrow(()-> new NotFoundSchedulePollException(ErrorCode.P001));

        Long myTeamMemberNo = getValidTeamMemberNo(selectedSchedulePoll.getTeamNo(), memberNo);

        // 도메인 투표 수행 및 상태 변화 결과 수신
        MinPlayerStatusChange statusChange = selectedSchedulePoll.vote(myTeamMemberNo,status);

        schedulePollRepository.save(selectedSchedulePoll);

        // TODO:
        switch (statusChange) {
            case MET -> {
                // TODO: 리더 알림 -> 최소 인원 확보 (매칭/자체전 상세 등록 권장)
            }
            case UNMET -> {
                // TODO: 리더 알림 -> 최소 인원 미달 발생(용병 모집 활용 제안)
            }
            case NO_CHANGE -> {
                // 알림 미발송
            }
        }
    }

    @Override
    public List<SchedulePollMinPlayerMetResDto> getMinPlayerMetSchedulePolls(Long teamNo, Long memberNo) {
        if (!teamQueryPort.isTeamLeader(teamNo, memberNo)) {
            throw new AccessDeniedException(ErrorCode.T05);
        }

        return schedulePollRepository.findAllByTeamNo(teamNo)
                .stream()
                .filter(SchedulePoll::isMinPlayerMet)
                .map(SchedulePollMinPlayerMetResDto::from)
                .toList()
                ;
    }

    @Override
    public InternalSchedulePollResponseDto getSchedulePollForServer(Long schedulePollNo) {
        SchedulePoll selectedSchedulePoll = schedulePollRepository.findBySchedulePollNo(schedulePollNo)
                .orElseThrow(()-> new NotFoundSchedulePollException(ErrorCode.P001));

        if(selectedSchedulePoll.getMatchInfo().getLocation() == null){
            return InternalSchedulePollResponseDto.builder()
                    .teamNo(selectedSchedulePoll.getTeamNo())
                    .matchType(selectedSchedulePoll.getMatchInfo().getMatchType().getDesc())
                    .playDt(selectedSchedulePoll.getMatchInfo().getPlayDt())
                    .playTm(selectedSchedulePoll.getMatchInfo().getPlayTm())
                    .build()
                    ;
        }
        else{
            Location selectLocation = locationRepository.findByLocationNo(selectedSchedulePoll.getMatchInfo().getLocation().getLocationNo())
                    .orElse(null)
                    ;

            return InternalSchedulePollResponseDto.builder()
                    .teamNo(selectedSchedulePoll.getTeamNo())
                    .matchType(selectedSchedulePoll.getMatchInfo().getMatchType().getDesc())
                    .location(
                            InternalLocationResponseDto
                                .builder()
                                .locationName(selectLocation.getLocationName())
                                    .address(selectLocation.getAddress())
                                    .roadAddress(selectLocation.getRoadAddress())
                                    .addressY(selectLocation.getAddressY())
                                    .addressX(selectLocation.getAddressX())
                                    .build()
                    )
                    .playDt(selectedSchedulePoll.getMatchInfo().getPlayDt())
                    .playTm(selectedSchedulePoll.getMatchInfo().getPlayTm())
                    .build()
                    ;
        }

    }

    @Override
    public InternalSchedulePollConflictResponseDto hasConflictingSchedulePoll(Long memberNo, String playDt, String playTm) {
        // 1. 회원이 가입되어 있는 팀 PK 조회 (팀 미가입 시 충돌 없음으로 판단)
        Long teamNo = teamQueryPort.getTeamNoByMemberNo(memberNo).orElse(null);
        if (teamNo == null) {
            return new InternalSchedulePollConflictResponseDto(false);
        }

        // 2. 해당 팀 내 회원의 teamMemberNo 식별
        Long myTeamMemberNo = teamQueryPort.getTeamMemberNo(teamNo, memberNo);
        if (myTeamMemberNo == null) {
            return new InternalSchedulePollConflictResponseDto(false);
        }

        // 3. 동일 날짜/시각 참석(ATTENDING) 상태의 팀 스케줄 존재 여부 검증
        boolean hasConflict = schedulePollRepository.existsConflictingSchedule(myTeamMemberNo, playDt, playTm);

        return new InternalSchedulePollConflictResponseDto(hasConflict);
    }

    @Override
    @Transactional
    public void setLocation(Long memberNo, Long schedulePollNo, SchedulePollLocationSetReqDto reqDto) {
        //일정 존재하는지 확인
        SchedulePoll selectedSchedulePoll = schedulePollRepository.findBySchedulePollNo(schedulePollNo)
                .orElseThrow(()-> new NotFoundSchedulePollException(ErrorCode.P001));


        //팀 사람인지 확인
        if (!teamQueryPort.isTeamLeader(selectedSchedulePoll.getTeamNo(), memberNo)) {
            throw new AccessDeniedException(ErrorCode.T05);
        }

        Location location = reqDto.toDomain();
        selectedSchedulePoll.setLocation(location);

        schedulePollRepository.save(selectedSchedulePoll);
        locationRepository.save(location);
    }

    private Long getValidTeamMemberNo(Long teamNo, Long memberNo) {
        Long myTeamMemberNo = teamQueryPort.getTeamMemberNo(teamNo, memberNo);
        if (myTeamMemberNo == null) {
            throw new AccessDeniedException(ErrorCode.T05); // 권한 없음 예외
        }

        return myTeamMemberNo;
    }
}