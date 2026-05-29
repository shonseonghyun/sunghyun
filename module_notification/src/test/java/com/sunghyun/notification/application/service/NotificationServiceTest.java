package com.sunghyun.notification.application.service;

import com.sunghyun.notification.application.port.out.NotiHistoryRepository;
import com.sunghyun.notification.application.port.out.NotificationSendPort;
import com.sunghyun.notification.config.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test","mail"})
class NotificationServiceTest {

    @Autowired
    private NotificationService target;

    @Autowired
    private NotificationSendPort notificationSendPort;

    @Autowired
    private NotiHistoryRepository notiHistoryRepository;

    @Test
    void 발송예외발생해도_이력저장된다() throws InterruptedException {
        //given
        final Long memberNo = 1L;
        final String email = "sunghyun7895@naver.com";
        final Message<String> message = new Message<>() {
            @Override
            public String getSubject(String data) {
                return data+"/제목";
            }

            @Override
            public String getContent(String data) {
                return data+"/내용";
            }
        };
        final String data = "데이터";

        //when
        target.doNoti(memberNo,email,message,data);

        Thread.sleep(5000);

        //then
        assertThat(notiHistoryRepository.findAll().size()).isEqualTo(1);
    }

//    @Test
//    void 이력저장실패해도_발송된다() throws InterruptedException {
//        //given
//        final Long memberNo = 1L;
//        final String email = "sunghyun7895@naver.com";
//        final Message<String> message = new Message<>() {
//            @Override
//            public String getSubject(String data) {
//                return data+"/제목";
//            }
//
//            @Override
//            public String getContent(String data) {
//                return data+"/내용";
//            }
//        };
//        final String data = "데이터";
//
//        //when
//        target.doNoti(memberNo,email,message,data);
//
//        Thread.sleep(5000);
//
//        //then
//        assertThat(notiHistoryRepository.findAll().size()).isEqualTo(1);
//    }
}