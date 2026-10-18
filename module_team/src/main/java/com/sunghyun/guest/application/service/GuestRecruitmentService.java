package com.sunghyun.guest.application.service;

import com.sunghyun.feign.dto.InternalGuestApplyerDto;
import com.sunghyun.feign.dto.InternalGuestRecruitmentDto;
import com.sunghyun.feign.dto.InternalSchedulePollResponseDto;
import com.sunghyun.feign.dto.InternalTeamResponseDto;
import com.sunghyun.guest.application.dto.*;
import com.sunghyun.guest.application.port.in.GuestRecruitmentUseCase;
import com.sunghyun.guest.application.port.out.SchedulePollPort;
import com.sunghyun.guest.application.port.out.TeamQueryPort;
import com.sunghyun.guest.domain.GuestRecruitment;
import com.sunghyun.guest.domain.Position;
import com.sunghyun.guest.domain.exception.GuestRecruitmentAlreadyExistException;
import com.sunghyun.guest.domain.exception.MatchConflictException;
import com.sunghyun.guest.domain.exception.NotFoundGuestRecruitmentException;
import com.sunghyun.guest.domain.repository.GuestRecruitmentRepository;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestRecruitmentService implements GuestRecruitmentUseCase {
    private final GuestRecruitmentRepository guestRecruitmentRepository;
    private final TeamQueryPort teamQueryPort;
    private final SchedulePollPort schedulePollPort;

    @Override
    @Transactional
    public GuestRecruitmentCreateResDto createGuestRecruitment(Long memberNo, Long schedulePollNo, GuestRecruitmentCreateReqDto reqDto) {
        // 인원 올바르지 않은 경우 거절

        // 자체전인 경우 장소 미정이면 거절
        // 매칭전

        // 이미 있는 경우 거절
        guestRecruitmentRepository.findBySchedulePollNo(schedulePollNo)
                .ifPresent(i -> {
                    throw new GuestRecruitmentAlreadyExistException(ErrorCode.G006);
                });

        GuestRecruitment guestRecruitment = GuestRecruitment.create(
                memberNo,
                schedulePollNo,
                reqDto.getTitle(),
                reqDto.getRequirements(),
                reqDto.getLevelLimit(),
                reqDto.getMatchFormat(),
                reqDto.getGuestFee(),
                reqDto.getPlayDt(),
                reqDto.getPlayTm(),
                reqDto.getDurationMinute(),
                reqDto.getQuarterDuration(),
                reqDto.getRestDuration(),
                reqDto.getMinGuaranteedQuarter(),
                reqDto.isBeverageProvided(),
                reqDto.getAdditionalNotice()
        );

        GuestRecruitment saved = guestRecruitmentRepository.save(guestRecruitment);
        return new GuestRecruitmentCreateResDto(saved.getRecruitmentNo());
    }

    @Override
    public GuestRecruitmentDetailInfoSelectResDto getGuestRecruitmentDetailInfo(Long recruitmentNo,Long applyedmemberNo) {
        GuestRecruitment selectedGuestRecruitment = guestRecruitmentRepository.findByRecruitmentNo(recruitmentNo)
                .orElseThrow(()-> new NotFoundGuestRecruitmentException(ErrorCode.G000));

        // 스케줄 및 팀 정보 내부 api 통한 조회
        GlobalResponse<InternalSchedulePollResponseDto> schedulePollResponseDto = schedulePollPort.getSchedulePollSimpleInfo(selectedGuestRecruitment.getSchedulePollNo());
        GlobalResponse<InternalTeamResponseDto> teamResponseDto = teamQueryPort.getTeamSimpleInfo(schedulePollResponseDto.getData().getTeamNo());

        return GuestRecruitmentDetailInfoSelectResDto.of(
                selectedGuestRecruitment,
                schedulePollResponseDto.getData(),
                teamResponseDto.getData(),
                applyedmemberNo
        );
    }

    // 동시성 처리 필요함
    @Override
    @Transactional
    public void apply(Long memberNo, Long recruitmentNo, GuestApplyReqDto reqDto) {
        Position applyedPosition = reqDto.getPosition();

        GuestRecruitment selectedGuestRecruitment = guestRecruitmentRepository.findByRecruitmentNo(recruitmentNo)
                .orElseThrow(()-> new NotFoundGuestRecruitmentException(ErrorCode.G000));

        // 1. 도메인 메소드 적용(검증 + 상태 변경 한 번에 일어남 / Fail-Fast)
        selectedGuestRecruitment.apply(memberNo,applyedPosition);

        // 2. [Local DB] 동일 시간대 타 용병 신청 충돌 검증
        boolean hasConflictOfGuest = guestRecruitmentRepository.existsConflictingApplication(memberNo,selectedGuestRecruitment.getPlayDt(),selectedGuestRecruitment.getPlayTm());
        if(hasConflictOfGuest){
            throw new MatchConflictException(ErrorCode.G007);
        }

        // 3. [External Feign] 동일 시간대 소속 팀 일정 참석 충돌 검증 (비싼 네트워크 호출)
        boolean hasConflictOfSchedulePoll = schedulePollPort.existsConflictingSchedulePoll(memberNo,selectedGuestRecruitment.getPlayDt(),selectedGuestRecruitment.getPlayTm())
                .getData().isHasConflict();
        if(hasConflictOfSchedulePoll){
            throw new MatchConflictException(ErrorCode.G007);
        }

        // 4. 변경 사항 저장
        guestRecruitmentRepository.save(selectedGuestRecruitment);
    }

    @Override
    public InternalGuestRecruitmentDto getGuestRecruitmentForServer(Long schedulePollNo) {
        GuestRecruitment selectedGuestRecruitment = guestRecruitmentRepository.findBySchedulePollNo(schedulePollNo)
                .orElse(null);
        if(selectedGuestRecruitment==null){
            return null;
        }

        List<InternalGuestApplyerDto> internalGuestApplyerDtos = selectedGuestRecruitment.getGuestApplyers().stream()
                .map(guestApplyer -> new InternalGuestApplyerDto(guestApplyer.getMemberNo()))
                .toList();

        return InternalGuestRecruitmentDto.of(selectedGuestRecruitment.getRecruitmentNo(),internalGuestApplyerDtos);
    }

    @Override
    public List<GuestRecruitmentSimpleInfoSelectResDto> getGuestRecruitments(Long lastRecruitmentNo, int size) {
        return List.of();
    }
}