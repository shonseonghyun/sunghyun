package com.sunghyun.batch.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotiHistoryDto {
    private Long subscriptionNo;
    private Long memberNo;
    private String email;
    private String notiType;
    private String notiValue;    // DB의 noti_setting 컬럼에 매핑
    private String sendDt;       // yyyyMMdd
    private String sendTm;       // HHmmss
}