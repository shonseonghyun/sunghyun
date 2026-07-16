package com.sunghyun.member.application.port.usecase;

import com.sunghyun.member.application.dto.req.FriendshipStatusUpdateReqDto;
import com.sunghyun.member.application.dto.res.FriendResDto;

public interface FriendUseCase {
    void requestFriend(final Long requesterMemberNo, final Long receiverMemberNo);
    void updateFriendshipStatus(final Long friendShipNo, final FriendshipStatusUpdateReqDto dto);
    FriendResDto getFriends(final Long memberNo);
}
