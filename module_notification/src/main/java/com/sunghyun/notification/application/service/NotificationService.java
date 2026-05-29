package com.sunghyun.notification.application.service;

import com.sunghyun.notification.application.port.in.NotificationUseCase;
import com.sunghyun.notification.application.port.out.NotiHistoryRepository;
import com.sunghyun.notification.application.port.out.NotificationSendPort;
import com.sunghyun.notification.config.Message;
import com.sunghyun.notification.domain.model.NotiHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {
    private final NotificationSendPort notificationSendPort;
    private final NotiHistoryRepository notiHistoryRepository;

    @Override
    @Transactional
    public <T> void doNoti(final Long memberNo, final String email, final Message<T> message, T data) {
        //알림 발송 도메인 생성
        NotiHistory notiHistory = NotiHistory.create(
                memberNo,
                email,
                message.getSubject(data),
                message.getContent(data)
        );

        //메일 발송
        notificationSendPort.send(email,message,data);

        //발송 이력 저장
        notiHistoryRepository.save(notiHistory);
    }
}
