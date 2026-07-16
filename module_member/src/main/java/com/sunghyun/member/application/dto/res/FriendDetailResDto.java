package com.sunghyun.member.application.dto.res;

import com.sunghyun.member.domain.enums.FriendShipRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDetailResDto {
    private Long friendShipNo;
    private FriendShipRequestStatus status;

    private Long memberNo;
    private String name;
}
