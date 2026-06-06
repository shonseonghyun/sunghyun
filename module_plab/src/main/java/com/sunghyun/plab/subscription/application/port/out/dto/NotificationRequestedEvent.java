package com.sunghyun.plab.subscription.application.port.out.dto;

import com.sunghyun.utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationRequestedEvent {
    private String outBoxNo;
    private Long memberNo;
    private String email;
    private String subject;
    private String content;

    public NotificationRequestedEvent(Long memberNo, String email, String subject, String content) {
        this.memberNo = memberNo;
        this.email = email;
        this.subject = subject;
        this.content = content;
        this.outBoxNo = ApiUtils.getUUID();
    }
}
