//package com.sunghyun.notification.application.event;
//
//
//import com.sunghyun.notification.application.port.in.NotificationUseCase;
//import com.sunghyun.notification.domain.event.NotificationEvent;
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionPhase;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class NotificationEventListener {
//    private final NotificationUseCase notificationUseCase;
//    private final HikariDataSource hikariDataSource;
//
//    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
////    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public <T> void doNoti(final NotificationEvent<T> notificationEvent){
//        int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
//        int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
//        System.out.println("NotificationEvent...");
//        System.out.println("activeConnections = " + activeConnections);
//        System.out.println("idleConnections = " + idleConnections);
//
//        log.info("NotificationEventListener subscribe !!!");
//        final Long memberNo = notificationEvent.getMemberNo();
//        final String email = notificationEvent.getEmail();
//        final Message<T> message = notificationEvent.getMessage();
//        final T data = notificationEvent.getData();
//
//        notificationUseCase.doNoti(memberNo,email,message,data);
//    }
//}
