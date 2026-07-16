package com.sunghyun.member.adpater.out.persistence.repository;

import com.sunghyun.member.adpater.out.persistence.entity.FriendShipRequestEntity;
import com.sunghyun.member.application.dto.res.FriendDetailResDto;
import com.sunghyun.member.domain.enums.FriendShipRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringJpaFriendShipRequestRepository extends JpaRepository<FriendShipRequestEntity, Long> {

    @Query("SELECT EXISTS (" +
            "  SELECT 1 FROM FriendShipRequestEntity f " +
            "  WHERE ((f.requesterMemberNo = :memberA AND f.receiverMemberNo = :memberB) " +
            "     OR (f.requesterMemberNo = :memberB AND f.receiverMemberNo = :memberA)) " +
            "  AND f.status IN :statuses" +
            ")")
    boolean existsRequestBetweenWithStatuses(
            @Param("memberA") Long memberA,
            @Param("memberB") Long memberB,
            @Param("statuses") List<FriendShipRequestStatus> statuses
    );

    /**
     * 1. 나에게 온 대기 중인 친구 요청 목록 조회 (pending)
     * - 수신자(receiver)가 나여야 함
     * - 조인은 나에게 요청을 보낸 사람(requester)과 해야 함
     */
    @Query("SELECT new com.sunghyun.member.application.dto.res.FriendDetailResDto(" +
            "f.friendShipRequestNo, f.status, m.memberNo, m.name) " +
            "FROM FriendShipRequestEntity f " +
            "JOIN MemberEntity m ON f.requesterMemberNo = m.memberNo " +
            "WHERE f.receiverMemberNo = :memberNo " +
            "AND f.status = com.sunghyun.member.domain.enums.FriendShipRequestStatus.REQUESTED")
    List<FriendDetailResDto> findPendingRequestsWithMemberInfo(@Param("memberNo") Long memberNo);

    /**
     * 2. 내 친구 목록 조회 (friends)
     * - 송신자 혹은 수신자 중 하나가 나여야 함
     * - 내가 송신자면 상대방(수신자) 정보를, 내가 수신자면 상대방(송신자) 정보를 가져오도록 CASE WHEN 처리
     */
    @Query("SELECT new com.sunghyun.member.application.dto.res.FriendDetailResDto(" +
            "  f.friendShipRequestNo, " +
            "  f.status, " +
            "  CASE WHEN f.requesterMemberNo = :memberNo THEN r.memberNo ELSE q.memberNo END, " +
            "  CASE WHEN f.requesterMemberNo = :memberNo THEN r.name ELSE q.name END " +
            ") " +
            "FROM FriendShipRequestEntity f " +
            "JOIN MemberEntity q ON f.requesterMemberNo = q.memberNo " + // 송신자 조인 (q)
            "JOIN MemberEntity r ON f.receiverMemberNo = r.memberNo " +  // 수신자 조인 (r)
            "WHERE (f.requesterMemberNo = :memberNo OR f.receiverMemberNo = :memberNo) " +
            "AND f.status = com.sunghyun.member.domain.enums.FriendShipRequestStatus.ACCEPTED")
    List<FriendDetailResDto> findFriendsWithMemberInfo(@Param("memberNo") Long memberNo);
}