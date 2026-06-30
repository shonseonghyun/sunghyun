package com.sunghyun.member.adpater.out.eventListener;

import com.sunghyun.member.domain.event.MemberRegisteredEvent;
import com.sunghyun.member.application.port.repository.MemberIdPendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberIdPendingEventListener {
    private final MemberIdPendingRepository memberIdPendingRepository;

    @Async
//    @Transactional(propagation = Propagation.REQUIRES_NEW) //RDB 쓰기 작업이 없는 순수 레디스 로직이므로 무의미한 DB 커넥션 점유 방지
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(final MemberRegisteredEvent event) {
        final String id = event.getId();
        memberIdPendingRepository.deletePendingId(id);
    }
}
