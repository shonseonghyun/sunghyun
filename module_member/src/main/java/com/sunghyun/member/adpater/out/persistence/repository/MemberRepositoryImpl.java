package com.sunghyun.member.adpater.out.persistence.repository;

import com.sunghyun.member.adpater.out.persistence.entity.FriendShipRequestEntity;
import com.sunghyun.member.adpater.out.persistence.entity.MemberEntity;
import com.sunghyun.member.application.dto.res.FriendDetailResDto;
import com.sunghyun.member.domain.enums.FriendShipRequestStatus;
import com.sunghyun.member.domain.model.FriendShipRequest;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final SpringJpaMemberRepository springJpaMemberRepository;
    private final SpringJpaFriendShipRequestRepository springJpaFriendShipRequestRepository;

    @Override
    public Member save(final Member member) {
        return springJpaMemberRepository.save(MemberEntity.fromDomain(member))
                .toDomain();
    }

    @Override
    public FriendShipRequest save(FriendShipRequest friendShipRequest) {
        return springJpaFriendShipRequestRepository.save(FriendShipRequestEntity.fromDomain(friendShipRequest))
                .toDomain();
    }

    @Override
    public boolean existFriendShipRequestWithStatues(Long requesterMemberNo, Long receiverMemberNo, List<FriendShipRequestStatus> statusList) {
        return springJpaFriendShipRequestRepository.existsRequestBetweenWithStatuses(requesterMemberNo,receiverMemberNo,statusList);
    }

    @Override
    public Optional<FriendShipRequest> getFriendShipRequestByRequestNo(Long friendShipNo) {
        return springJpaFriendShipRequestRepository.findById(friendShipNo)
                .map(FriendShipRequestEntity::toDomain);
    }

    @Override
    public Optional<Member> getMemberByMemberNo(final Long memberNo) {
        return springJpaMemberRepository.findById(memberNo)
                .map(MemberEntity::toDomain);
    }

    @Override
    public void deleteMember(final Long memberNo) {
        springJpaMemberRepository.deleteById(memberNo);
    }

    @Override
    public Optional<Member> getMemberById(final String id) {
        return springJpaMemberRepository.findMemberById(id)
                .map(MemberEntity::toDomain);
    }

    @Override
    public List<FriendDetailResDto> getFriendsWithMemberInfo(Long memberNo) {
        return springJpaFriendShipRequestRepository.findFriendsWithMemberInfo(memberNo);
    }

    @Override
    public List<FriendDetailResDto> getPendingRequestsWithMemberInfo(Long memberNo) {
        return springJpaFriendShipRequestRepository.findPendingRequestsWithMemberInfo(memberNo);
    }
}