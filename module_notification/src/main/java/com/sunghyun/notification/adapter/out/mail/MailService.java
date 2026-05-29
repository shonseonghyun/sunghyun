package com.sunghyun.notification.adapter.out.mail;

import com.sunghyun.notification.application.port.out.NotificationSendPort;
import com.sunghyun.notification.config.Message;
import com.sunghyun.notification.domain.exception.MailSendException;
import com.sunghyun.web.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailService implements NotificationSendPort {
    private final JavaMailSender emailSender;

    public <T> void send(final String to, final Message<T> messageStrategy, final T data) {
        try {
            log.info(">>> [MailService] 메일 발송 시작 (Thread: {})", Thread.currentThread().getName());

            MimeMessage message = createMessage(to,messageStrategy,data);
            emailSender.send(message);

            log.info(">>> [MailService] 메일 발송 완료 (Thread: {})", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("메일 발송 도중 에러 발생");
            throw new MailSendException(ErrorCode.MA00);
        }
//        finally {
//            throw new RuntimeException("의도적인 예외 발생");
//        }
    }

    // 메일 내용 작성
    public <T> MimeMessage createMessage(final String to, final Message<T> messageStrategy, final T data) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(jakarta.mail.Message.RecipientType.TO, to);

        // 이메일 제목
        message.setSubject(messageStrategy.getSubject(data));

        // 메일 내용, charset타입, subtype
        message.setText(messageStrategy.getContent(data), "utf-8", "html");

        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom("sunghyun7895@naver.com");

        return message;
    }
}