package com.sunghyun.mail;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.MailSendException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;

    @Async
    public <T> void send(final String to,final MailMessage<T> messageStrategy,final T data) {
        try {
            log.info(">>> [MailService] 메일 발송 시작 (Thread: {})", Thread.currentThread().getName());
            MimeMessage message = createMessage(to,messageStrategy,data);
            emailSender.send(message);
            log.info(">>> [MailService] 메일 발송 완료 (Thread: {})", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("메일 발송 도중 에러 발생");
            throw new MailSendException(ErrorCode.MA00);
        }
    }

    // 메일 내용 작성
    private <T> MimeMessage createMessage(final String to, final MailMessage<T> messageStrategy,final T data) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);

        // 이메일 제목
        message.setSubject(messageStrategy.getSubject(data));

        // 메일 내용, charset타입, subtypel
        message.setText(messageStrategy.getContent(data), "utf-8", "html");

        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom("sunghyun7895@naver.com");

        return message;
    }
}
