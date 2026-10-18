package com.sunghyun.team.application.port;

import com.sunghyun.team.application.port.in.TeamRequestUseCase;
import com.sunghyun.team.domain.event.TeamJoinRequestAcceptedEvent;
import com.sunghyun.team.domain.exceptions.request.TeamJoinJoinRequestNotFoundException;
import com.sunghyun.team.domain.exceptions.team.TeamNotFoundException;
import com.sunghyun.team.domain.model.Team;
import com.sunghyun.team.domain.model.TeamJoinRequest;
import com.sunghyun.team.domain.repository.TeamRepository;
import com.sunghyun.team.domain.repository.TeamRequestRepository;
import com.sunghyun.team.domain.service.TeamRequestValidator;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamRequestService implements TeamRequestUseCase {
    private final TeamRequestRepository teamRequestRepository;
    private final TeamRepository teamRepository;

    private final TeamRequestValidator teamRequestValidator;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void requestTeamJoin(Long teamNo, Long requesterMemberNo) {
        // 검증 위임
        teamRequestValidator.validateCanRequest(teamNo,requesterMemberNo);

        // 신청 도메인 생성,영속화
        TeamJoinRequest newTeamJoinRequest = TeamJoinRequest.create(teamNo,requesterMemberNo);
        teamRequestRepository.save(newTeamJoinRequest);

        // TODO: Leader,SubLeader 대상 알림 이벤트 발행

    }

    @Override
    @Transactional
    public void acceptRequest(Long requestNo, Long approverMemberNo) {
        // 요청 건 조회
        TeamJoinRequest selecetdTeamJoinRequest = teamRequestRepository.findByRequestNo(requestNo)
                .orElseThrow(()->new TeamJoinJoinRequestNotFoundException(ErrorCode.T003));

        // 팀 조회
        Team selectedTeam = teamRepository.findByTeamNo(selecetdTeamJoinRequest.getTeamNo())
                .orElseThrow(()-> new TeamNotFoundException(ErrorCode.T002));

        // 권한 검증
        selectedTeam.validateLeader(approverMemberNo);

        // 팀 측 수락
        selecetdTeamJoinRequest.accept();

        // 영속화
        teamRequestRepository.save(selecetdTeamJoinRequest);

        Long teamNo = selectedTeam.getTeamNo();
        Long requesterMemberNo = selecetdTeamJoinRequest.getMemberNo();

        // TODO:
        //  1. 회원 초대 -> 다음 레이어에서 처리
        //  2. 팀 채팅방 초대 -> 다다음 레이어에서 처리
        //  3. 신청자와 팀 내 알림 -> 다다음 레이어에서 처리
        eventPublisher.publishEvent(new TeamJoinRequestAcceptedEvent(teamNo,requesterMemberNo));
    }

    @Override
    @Transactional
    public void rejectRequest(Long requestNo, Long leaderMemberNo) {
        // 필요한 애그리거트 조회
        TeamJoinRequest selecetdTeamJoinRequest = teamRequestRepository.findByRequestNo(requestNo)
                .orElseThrow(()->new TeamJoinJoinRequestNotFoundException(ErrorCode.T003));
        Team selectedTeam = teamRepository.findByTeamNo(selecetdTeamJoinRequest.getTeamNo())
                .orElseThrow(()-> new TeamNotFoundException(ErrorCode.T002));

        // 회장 검증
        selectedTeam.validateLeader(leaderMemberNo);

        // 팀 측 거절
        selecetdTeamJoinRequest.reject();

        teamRequestRepository.save(selecetdTeamJoinRequest);

        // 후처리 목록
        // 1. 신청자만 알림
    }

    @Override
    @Transactional
    public void cancelRequest(Long requestNo, Long requesterMemberNo) {
        // 필요한 애그리거트 조회
        TeamJoinRequest selecetdTeamJoinRequest = teamRequestRepository.findByRequestNo(requestNo)
                .orElseThrow(()->new TeamJoinJoinRequestNotFoundException(ErrorCode.T003));

        // 신청자 측 취소
        selecetdTeamJoinRequest.cancel(requesterMemberNo);

        teamRequestRepository.save(selecetdTeamJoinRequest);

        // 후처리 목록
        // 1. 회장만 알림
    }
}
