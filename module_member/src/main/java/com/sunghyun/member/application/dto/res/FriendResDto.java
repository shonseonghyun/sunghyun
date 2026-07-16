package com.sunghyun.member.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FriendResDto {
    private List<FriendDetailResDto> friends;
    private List<FriendDetailResDto> pendings;
}
