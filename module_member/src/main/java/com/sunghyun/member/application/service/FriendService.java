package com.sunghyun.member.application.service;

import com.sunghyun.config.MemberNotFoundException;
import com.sunghyun.member.application.dto.req.FriendshipStatusUpdateReqDto;
import com.sunghyun.member.application.dto.res.FriendDetailResDto;
import com.sunghyun.member.application.dto.res.FriendResDto;
import com.sunghyun.member.application.port.usecase.FriendUseCase;
import com.sunghyun.member.domain.enums.FriendShipRequestStatus;
import com.sunghyun.member.domain.exception.friendship.FriendShipAlreadyRequestedException;
import com.sunghyun.member.domain.exception.friendship.FriendShipRequestNotFoundException;
import com.sunghyun.member.domain.model.FriendShipRequest;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService implements FriendUseCase {
    private final MemberRepository memberRepository;

    @Override
//    @Transactional
    public void requestFriend(final Long requesterMemberNo,final Long receiverMemberNo) {
        // 각각 존재하는 회원인지부터 확인
        Member requester =memberRepository.getMemberByMemberNo(requesterMemberNo)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M000));

        Member receiver = memberRepository.getMemberByMemberNo(receiverMemberNo)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M000));

        // 이미 친구 신청 요청 걸었었는지 체크(a->b, b->a 모두 체크) == 이미 친구인지 확인
        if (memberRepository.existFriendShipRequestWithStatues(requesterMemberNo, receiverMemberNo, Arrays.asList(FriendShipRequestStatus.REQUESTED,FriendShipRequestStatus.ACCEPTED))) {
            throw new FriendShipAlreadyRequestedException(ErrorCode.M010);
        }

        // 친구추가 요청 도메인 생성
        FriendShipRequest friendShipRequest = FriendShipRequest.create(requesterMemberNo,receiverMemberNo);

        // 친구추가 요청 상대방에게 알림(비동기 진행), 외부 요청건이므로 트랜잭션에 최대한 포함되면 안됨

        // 친구추가 요청 저장
        memberRepository.save(friendShipRequest);
    }

    @Override
    @Transactional
    public void updateFriendshipStatus(final Long friendShipNo, final FriendshipStatusUpdateReqDto dto) {
        // 요청건들에 한해서 조회하기
        FriendShipRequest selectedFriendShipRequest = memberRepository.getFriendShipRequestByRequestNo(friendShipNo)
                .orElseThrow(() -> new FriendShipRequestNotFoundException(ErrorCode.F000));

        selectedFriendShipRequest.updateStatus(dto.getStatus());

        if(selectedFriendShipRequest.getStatus() == FriendShipRequestStatus.ACCEPTED){
            // 수락 시 상대방에게 알림(이벤트 발행)
            log.info("수락하여 상대방에게 알림 발송");
        }

        // DB 반영
        memberRepository.save(selectedFriendShipRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public FriendResDto getFriends(final Long memberNo) {
        // 1. 도메인 변환이나 for문 없이, DB에서 조인된 최종 DTO 목록을 바로 가져옵니다. (쿼리 1번)
        List<FriendDetailResDto> pending = memberRepository.getPendingRequestsWithMemberInfo(memberNo);

        // 2. 친구 목록도 동일하게 조회 (쿼리 1번)
        List<FriendDetailResDto> friends = memberRepository.getFriendsWithMemberInfo(memberNo);

        // 3. 바로 리턴!
        return FriendResDto.builder()
                .friends(friends)
                .pendings(pending)
                .build();
    }
}
