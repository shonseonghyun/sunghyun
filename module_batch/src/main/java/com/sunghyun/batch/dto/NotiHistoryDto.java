package com.sunghyun.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotiHistoryDto {
    private Long subscriptionNo;
    private Long memberNo;
    private String email;

    private NotificationTargetDto notificationTargetDto;
}