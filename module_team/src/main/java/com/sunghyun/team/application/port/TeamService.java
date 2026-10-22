package com.sunghyun.team.application.port;

import com.sunghyun.event.team.TeamCreatedEvent;
import com.sunghyun.schedule.domain.Location;
import com.sunghyun.schedule.domain.exception.NotFoundLocationException;
import com.sunghyun.schedule.domain.repository.LocationRepository;
import com.sunghyun.team.application.port.in.TeamUseCase;
import com.sunghyun.team.application.port.in.dto.TeamCreateReqDto;
import com.sunghyun.team.application.port.out.dto.TeamCreateResDto;
import com.sunghyun.team.application.port.out.dto.TeamDetailInfoSelectResDto;
import com.sunghyun.team.application.port.out.dto.TeamSimpleInfoSelectResDto;
import com.sunghyun.team.domain.exceptions.team.TeamNotFoundException;
import com.sunghyun.team.domain.model.Team;
import com.sunghyun.team.domain.repository.TeamRepository;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {

    private final TeamRepository teamRepository;
    private final LocationRepository locationRepository;

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Value("${kafka.topic.team-created}")
    private String teamCreatedTopic;

    @Override
    @Transactional
    public TeamCreateResDto createTeam(Long creatorMemberNo, TeamCreateReqDto reqDto) {
        Team team = Team.createTeam(
                creatorMemberNo,
                reqDto.getTeamName(),
                reqDto.getTeamType(),
                reqDto.getLogoUrl(),
                reqDto.getTeamDesc(),
                reqDto.getActivityRegion(),
                reqDto.getLocation(),
                reqDto.getGenderComposition(),
                reqDto.getMainAgeGroup(),
                reqDto.getMonthlyFee()
        );

        // 2. 저장소(Repository)를 통한 저장
        Team savedTeam = teamRepository.save(team);

        // 이 부분도 이벤트 발행으로 처리하면 좋겠다.
        if(reqDto.getLocation() != null){
            Location savedLocation = locationRepository.save(reqDto.getLocation());
        }

        // 팀 채팅방 생성!!!!!!
        kafkaTemplate.send(
                teamCreatedTopic,
                String.valueOf(savedTeam.getTeamNo()),
                new TeamCreatedEvent(savedTeam.getTeamNo(),creatorMemberNo)
        );

        // 3. Response DTO 반환
        return TeamCreateResDto.from(savedTeam);
    }

    @Override
    @Transactional
    public void acceptMember(Long teamNo, Long newMemberNo) {
        // 팀 조회
        Team selectedTeam = teamRepository.findByTeamNo(teamNo)
                .orElseThrow(()-> new TeamNotFoundException(ErrorCode.T002));

        selectedTeam.addMember(newMemberNo);

        teamRepository.save(selectedTeam);

        // TODO:
        //  1. 팀 채팅방 초대
        //  2. 신청자와 팀 내 알림
    }

    @Override
    public TeamDetailInfoSelectResDto getTeamDetailInfo(Long teamNo,Long memberNo) {
        Team selectedTeam = teamRepository.findByTeamNo(teamNo)
                .orElseThrow(()-> new TeamNotFoundException(ErrorCode.T002));

        // 게스트가 누른 경우는 false 리턴
        // 게스트가 아니면서 해당팀인경우만 true 리턴
        boolean isMember = memberNo != null & selectedTeam.isMember(memberNo);

        // 자주 가는 구장 세팅된 경우
        Location location = null;
        if(selectedTeam.getLocationNo() != null){
            location = locationRepository.findByLocationNo(selectedTeam.getLocationNo())
                    .orElseThrow(()-> new NotFoundLocationException(ErrorCode.P005));
        }

        return TeamDetailInfoSelectResDto.from(selectedTeam,isMember,location);
    }

    @Override
    public TeamDetailInfoSelectResDto getTeamDetailInfoByMemberNo(Long memberNo) {
        Team selectedTeam = teamRepository.findByMemberNo(memberNo)
                .orElseThrow(()-> new TeamNotFoundException(ErrorCode.T009));

        // 게스트가 누른 경우는 false 리턴
        // 게스트가 아니면서 해당팀인경우만 true 리턴
        boolean isMember = memberNo != null & selectedTeam.isMember(memberNo);

        Location location = null;
        if(selectedTeam.getLocationNo() != null){
            location = locationRepository.findByLocationNo(selectedTeam.getLocationNo())
                    .orElseThrow(()-> new NotFoundLocationException(ErrorCode.P005));
        }

        return TeamDetailInfoSelectResDto.from(selectedTeam,isMember,location);
    }

    @Override
    public TeamSimpleInfoSelectResDto getTeamSimpleInfo(Long teamNo) {
        Team selectedTeam = teamRepository.findByTeamNo(teamNo)
                .orElseThrow(()-> new TeamNotFoundException(ErrorCode.T002));

        return TeamSimpleInfoSelectResDto.from(selectedTeam);
    }
}