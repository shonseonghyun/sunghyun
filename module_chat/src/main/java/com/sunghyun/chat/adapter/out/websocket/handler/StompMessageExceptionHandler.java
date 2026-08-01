package com.sunghyun.chat.adapter.out.websocket.handler;

import com.sunghyun.chat.adapter.config.WebSocketTopicProperties;
import com.sunghyun.chat.domain.exception.ChatException;
import com.sunghyun.dto.UserPrincipal;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @MessageMapping으로 매핑된 요청을 처리하는 과정에서 발생한 예외를 핸들링하는 클래스
 */


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class StompMessageExceptionHandler {
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketTopicProperties topicProperties;


    @MessageExceptionHandler(ChatException.class)
    public void handleChatException(ChatException e, UserPrincipal principal){
        log.error("STOMP 메시지 처리 중 예외 발생: ", e);
        // 만약 principal==null, 인증되지 않았다고 판단하여 더 이상 채팅하지 못하게 웹소켓통신을 끊어야 한다. 끊기 위해, Error프레임으로 응답해야 할텐데 어케하지?
        // 그니까 여기서 Message 프레임으로 강제되는데, 이걸 원하는 서버 응답 프레임으로 하고 싶다 이거지..
        // 아..근데  (principal 처크==인증된 회원인지 검증)를 해당 컴포넌트에서 처리하는 건 옳지 않다고 봐. 왜냐면, 인증된 회원검증을 SEND 프레임으로 들어온 직후에 해야지. SEND 프레임 들어오고 내부 비즈니스로직 다 처리하고 나서 검증을 하는 건 멍청한 짓이거든
//        if (principal != null) {
//            //인증되지 않았다라고 판단
//            log.error("미인증 유저의 메시지 요청 감지. 웹소켓 연결을 종료합니다.");
//            throw new MessageDeliveryException("Unauthenticated websocket session");
//        }
        // + 비즈니스 로직에서 예외가 발생했다 하여 ERROR프레임으로 보낸다는 것은 대부분의 서비스기획가 맞지 않다고 한다. 차라리 클라이언트에서 응답코드에 따라 세션끊을지말지 결정하는 게 나아보인다.

        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                topicProperties.getErrorTopic(), /* /user/회원번호/queue/errors */
                GlobalResponse.of(e.getErrorCode())
        );
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser(value = "${chat.websocket.topic.error-prefix}" ,broadcast = false)
    public GlobalResponse handleUnexpectedException(Exception e, UserPrincipal principal){
        log.error("STOMP 메시지 처리 중 예상치 못한 에러 발생: ", e);

        return GlobalResponse.of(ErrorCode.F001);
    }
}