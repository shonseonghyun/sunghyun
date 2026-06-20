package com.sunghyun.notification.application.service;

import com.sunghyun.notification.application.port.in.NotificationUseCase;
import com.sunghyun.notification.application.port.in.dto.NotificationRequestEventDto;
import com.sunghyun.notification.application.port.out.NotiHistoryRepository;
import com.sunghyun.notification.application.port.out.NotificationSendPort;
import com.sunghyun.notification.domain.model.NotiHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {
    private final NotificationSendPort notificationSendPort;
    private final NotiHistoryRepository notiHistoryRepository;

    @Override
    @Async
//    @Transactional // 트랜잭션을 선언 시 메일발송 동안 DB커넥션을 물고 있는 문제 발생, 그렇다고 안하자니 메일발송 실패 시 롤백처리가 안되네?
    public void doNoti(final NotificationRequestEventDto dto) {
        //알림 발송 도메인 생성
        NotiHistory notiHistory = NotiHistory.create(
                dto.getMemberNo(),
                dto.getEmail(),
                dto.getSubject(),
                dto.getContent()
        );

        // 메일 발송되었으나 DB 단절 등으로 인한 커밋 실패 시 롤백 가능성 존재
        // 저장이나 발송 순서를 바꿔도 큰 차이는 없다(차이는 오직 빠르게 다음 행 실행할거냐 안할거냐 차이)
        // 이 순서로 둔 이유는 메일 발송 실패 시 발송 이력 저장 롤백되어야 한다라는 것이 목표이다. 아래처럼 순서를 구성한다면 save줄까지 내려가지 않기에 애초에 db이력을 쌓지 않기에 자동으로 롤백된 효과를 낸다.

        //메일 발송
        notificationSendPort.send(dto.getEmail(),dto.getSubject(),dto.getContent());

        //발송 이력 저장
        notiHistoryRepository.save(notiHistory);
    }
}
