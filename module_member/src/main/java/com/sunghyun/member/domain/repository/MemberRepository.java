package com.sunghyun.member.domain.repository;

import com.sunghyun.member.application.dto.res.FriendDetailResDto;
import com.sunghyun.member.domain.enums.FriendShipRequestStatus;
import com.sunghyun.member.domain.model.FriendShipRequest;
import com.sunghyun.member.domain.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> getMemberByMemberNo(final Long memberNo);
    Optional<Member> getMemberById(final String id);
    void deleteMember(final Long memberNo);
    Member save(final Member member);

    FriendShipRequest save(final FriendShipRequest friendShipRequest);
    boolean existFriendShipRequestWithStatues(Long requesterMemberNo, Long receiverMemberNo, List<FriendShipRequestStatus> statusList);
    Optional<FriendShipRequest> getFriendShipRequestByRequestNo(Long friendShipNo);
    List<FriendDetailResDto> getFriendsWithMemberInfo(Long memberNo);
    List<FriendDetailResDto> getPendingRequestsWithMemberInfo(Long memberNo);
}
