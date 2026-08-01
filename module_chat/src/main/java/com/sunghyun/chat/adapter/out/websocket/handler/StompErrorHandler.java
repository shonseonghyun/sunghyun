package com.sunghyun.chat.adapter.out.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import com.sunghyun.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

/**
 * STOMP 프레임 처리 시 발생한 예외를 핸들링하는 클래스
 */


@Slf4j
@Component
@RequiredArgsConstructor
public class StompErrorHandler extends StompSubProtocolErrorHandler {
    private final ObjectMapper om;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        // 1. 인터셉터에서 던진 예외 원인 추출
        ErrorCode errorCode;
        Throwable cause = ex.getCause();

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        // 2. 개발자가 정의한 BaseException인지 확인
        if (cause instanceof BaseException baseException) {
            errorCode = baseException.getErrorCode();
        } else {
            log.error("처리되지 않은 웹소켓 예외 발생: {}", cause != null ? cause.getMessage() : ex.getMessage());
            errorCode = ErrorCode.F001;
        }

        // 3. 커스텀 에러 메시지 프레임 생성 후 반환
        try {
            return prepareGlobalResponse(errorCode, accessor);
        } catch (Exception e) {
            log.error("에러 프레임 생성 중 예기치 못한 예외 발생: ", e);
            return MessageBuilder.createMessage(
                    new byte[0],
                    accessor.getMessageHeaders()
            );
        }
    }

    private Message<byte[]> prepareGlobalResponse(ErrorCode errorCode,StompHeaderAccessor accessor) throws Exception {
        // 1. 공통 응답 포맷(GlobalResponse) 생성 및 JSON 변환
        GlobalResponse response = GlobalResponse.of(errorCode);
        String jsonPayload = om.writeValueAsString(response);

        // STOMP 표준 헤더인 'message'에 에러 메시지 세팅 (선택사항)
        accessor.setMessage(errorCode.getMessage());
        accessor.setLeaveMutable(true);

        // 3. Body(Payload)에 JSON 데이터를 실어서 메시지 객체 생성
        return MessageBuilder.createMessage(
                jsonPayload.getBytes(StandardCharsets.UTF_8),
                accessor.getMessageHeaders()
        );
    }
}
