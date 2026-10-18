package com.sunghyun.team.domain.service;

import com.sunghyun.team.domain.exceptions.request.TeamAlreadyJoinedException;
import com.sunghyun.team.domain.exceptions.request.TeamJoinAlreadyJoinedException;
import com.sunghyun.team.domain.exceptions.request.TeamJoinRequestAlreadyRequestedException;
import com.sunghyun.team.domain.exceptions.team.TeamNotFoundException;
import com.sunghyun.team.domain.model.Team;
import com.sunghyun.team.domain.repository.TeamRepository;
import com.sunghyun.team.domain.repository.TeamRequestRepository;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamRequestValidator {
    private final TeamRequestRepository teamRequestRepository;
    private final TeamRepository teamRepository;

    public void validateCanRequest(Long teamNo, Long requesterMemberNo){
        validateNotMemberOfTeam(teamNo,requesterMemberNo);
        validateNotJoinedAnyTeam(requesterMemberNo);
        validatePendingRequest(teamNo,requesterMemberNo);
    }

    private void validateNotMemberOfTeam(Long teamNo,Long requesterMemberNo){
        // 신청 대상 팀 존재 여부 검증
        Team selectedTeam = teamRepository.findByTeamNo(teamNo)
                .orElseThrow(()->new TeamNotFoundException(ErrorCode.T002));

        // 이미 해당 팀의 멤버인지 검증
        if(selectedTeam.isMember(requesterMemberNo)){
            throw new TeamJoinAlreadyJoinedException(ErrorCode.T001);
        }
    }

    private void validateNotJoinedAnyTeam(Long requesterMemberNo) {
        // 이미 '어느 팀이든' 가입된 상태인지 확인 (1인 1팀 정책)
        if (teamRepository.existJoinedTeamByMemberNo(requesterMemberNo)) {
            throw new TeamAlreadyJoinedException(ErrorCode.T007);
        }
    }

    /**
     * 대기(REQUESTED) 중인 신청 건이 있을 때, '해당 팀'인지 '타 팀'인지 구분하여 응답
     */
    private void validatePendingRequest(Long teamNo, Long requesterMemberNo) {
        // 현재 승인 대기(REQUESTED) 중인 신청이 있는지 확인 (1인 1신청 정책)
//        if (teamRequestRepository.existJoinTeamRequested(requesterMemberNo,TeamJoinRequestStatus.REQUESTED)) {
//            throw new TeamJoinRequestAlreadyRequestedException(ErrorCode.T008);
//        }

        teamRequestRepository.findPendingRequestByMemberNo(requesterMemberNo)
                .ifPresent(pendingRequest -> {
                    // 1. 신청한 팀이 '지금 신청하려는 바로 그 팀'인 경우
                    if (pendingRequest.getTeamNo().equals(teamNo)) {
                        throw new TeamJoinRequestAlreadyRequestedException(ErrorCode.T000);
                    }

                    // 2. 신청한 팀이 '다른 팀'인 경우
                    throw new TeamJoinRequestAlreadyRequestedException(ErrorCode.T008);
                });
    }

//    private void validate(Long teamNo, Long requesterMemberNo){
//        // 신청 내역 없다면 신청 가능
//        // 신청 상태라면? 현재 가입 신청된 팀입니다 응답
//        // 거절 상태라면? 신청 가능(로우 증가)
//        // 수락 상태라면? 현재 가입 완료된 팀입니다. 응답
//        // 신청에 대한 직접 취소 상태라면?? 신청 가능(로우 증가)
//        TeamJoinRequest latestRequest = teamRequestRepository
//                .findLatestTeamJoinRequest(teamNo, requesterMemberNo)
//                .orElse(null);
//
//        if(latestRequest == null
//                || latestRequest.getStatus() == TeamJoinRequestStatus.REJECTED
//                || latestRequest.getStatus() == TeamJoinRequestStatus.CANCELLED
//        ) {
//            return ;
//        }
//
//        if(latestRequest.getStatus() == TeamJoinRequestStatus.REQUESTED) {
//            throw new TeamJoinRequestAlreadyRequestedException(ErrorCode.T000);
//        }
//    }
}
