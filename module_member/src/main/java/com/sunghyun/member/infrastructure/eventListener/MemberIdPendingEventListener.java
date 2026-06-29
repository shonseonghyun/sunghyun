package com.sunghyun.member.infrastructure.eventListener;

import com.sunghyun.member.domain.event.MemberRegisteredEvent;
import com.sunghyun.member.domain.handler.MemberIdPendingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberIdPendingEventListener {
    private final MemberIdPendingHandler memberIdPendingHandler;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(final MemberRegisteredEvent event) {
        final String id = event.getId();
        memberIdPendingHandler.deletePendingId(id);
    }
}
