package com.sunghyun.notification.domain.model;

import com.sunghyun.notification.domain.enums.NotiSetting;
import com.sunghyun.notification.domain.enums.NotiType;
import com.sunghyun.utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotiHistory {
    private Long historyNo;
    private Long memberNo;
    private String email;
    private String subject;
    private String content;
    private String sendDt;
    private String sendTm;

    /**
     * 알림 이력 생성을 위한 정적 팩토리 메서드
     */
    public static NotiHistory create(
            final Long memberNo,
            final String email,
            final String subject,
            final String content
    ) {
        return NotiHistory.builder()
                .memberNo(memberNo)
                .email(email)
                .subject(subject)
                .content(content)
                .sendDt(ApiUtils.getCurrentDt())
                .sendTm(ApiUtils.getCurrentTm())
                .build();
    }
}
