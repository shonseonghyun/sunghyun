package com.sunghyun.member.adpater.in.web;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.member.application.dto.req.FriendshipStatusUpdateReqDto;
import com.sunghyun.member.application.dto.res.FriendResDto;
import com.sunghyun.member.application.port.usecase.FriendUseCase;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse  ;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendUseCase friendUseCase;

    @PostMapping("/friend/{requesterMemberNo}/request/{receiverMemberNo}")
    public GlobalResponse requestFriend(@PathVariable Long requesterMemberNo, @PathVariable Long receiverMemberNo){
        friendUseCase.requestFriend(requesterMemberNo,receiverMemberNo);
        return GlobalResponse.of(ErrorCode.S000);
    }

    @GetMapping("/friends/me")
    public GlobalResponse getFriends(@AuthMember AuthMemberInfo authMemberInfo){
        FriendResDto result = friendUseCase.getFriends(authMemberInfo.getMemberNo());
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PatchMapping("/friend/{friendShipNo}/status")
    public GlobalResponse updateFriendshipStatus(@PathVariable Long friendShipNo, @Valid @RequestBody FriendshipStatusUpdateReqDto dto){
        friendUseCase.updateFriendshipStatus(friendShipNo,dto);
        return GlobalResponse.of(ErrorCode.S000);
    }
}