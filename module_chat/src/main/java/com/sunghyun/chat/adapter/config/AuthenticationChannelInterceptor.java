package com.sunghyun.chat.adapter.config;

import com.sunghyun.config.JwtProvider;
import com.sunghyun.dto.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationChannelInterceptor implements ChannelInterceptor {
    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("AuthenticationChannelInterceptor.preSend");

        StompHeaderAccessor headerAccessor = getHeaderAccessor(message);
        validateToken(headerAccessor);

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("AuthenticationChannelInterceptor.postSend");
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    private StompHeaderAccessor getHeaderAccessor(Message<?> message){
        return MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    }

    private String getAccessToken(StompHeaderAccessor stompHeaderAccessor){
        String bearerToken = stompHeaderAccessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        return jwtProvider.resolveTokenInBearer(bearerToken);
    }

    private void validateToken(StompHeaderAccessor headerAccessor) {
        if (headerAccessor == null) {
            return;
        }

        // CONNECT 프레임일 때만 토큰 검증 및 STOMP 세션 유저 등록
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            String accessToken = getAccessToken(headerAccessor);

            // 토큰 검증
            jwtProvider.validate(accessToken+"s");

            // Principal 획득 후 STOMP 세션에 유저 등록
            Principal principal = jwtProvider.getPrincipal(accessToken);
            headerAccessor.setUser(principal);

            log.info("STOMP 웹소켓 유저 인증 성공 - Principal: {}", principal.getName());
        }

        else{
            log.info("STOMP CONNECT 외엔 웹소켓 유저 인증 패스");
        }

        // SEND, SUBSCRIBE 등 다른 프레임은 CONNECT 때 등록된 세션 유저를 스프링이 자동으로 유지해 줍니다.
    }

//    private void validateToken(StompHeaderAccessor headerAccessor){
//
//        //원하는 시점에만
//        if(StompCommand.CONNECT.equals(headerAccessor.getCommand())){
//            // 리액트에서 보낸 헤더는 STOMP 규격상 nativeHeaders라는 Map 안에 List<String> 형태로 담겨 들어오기 때문에, getHeader 메소드로는 가져올 수 없다.
////            Object accessToken = StompHeaderAccessor.wrap(message).getHeader(HttpHeaders.AUTHORIZATION);
////            Object accessToken = StompHeaderAccessor.wrap(message).getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
//
//            String accessToken = getAccessToken(headerAccessor);
//            //Authentication, SecurityContextHolder import 불가
////            Authentication authentication = jwtProvider.getAuthentication(accessToken);
////            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // 토큰 검증만 진행
//            // 특정 url에 대해 권한이 있는가에 대한 검증은 진행하지 않고 있음.
//            // 해야하나?
//            jwtProvider.validate(accessToken);
//
//            // 2. security 모듈에 위임하여 Principal 획득
//            Principal principal = jwtProvider.getPrincipal(accessToken);
//
//            // 3. STOMP 세션 유저 등록
//            // 스프링이 생명주기를 관리해주므로 따로 clear하지 않아도 된다.
//            headerAccessor.setUser(principal);
//        }
//
//        else if(StompCommand.SEND.equals(headerAccessor.getCommand())){
//            String accessToken = getAccessToken(headerAccessor);
//
//            // 1. 토큰 간단 검증
//            jwtProvider.validate(accessToken);
//
////            // 2. security 모듈에 위임하여 Principal 획득
////            Principal principal = jwtProvider.getPrincipal(accessToken);
////
////            // 3. STOMP 세션 유저 등록
////            // 스프링이 생명주기를 관리해주므로 따로 clear하지 않아도 된다.
////            headerAccessor.setUser(principal);
//        }
//
////        log.info("STOMP 웹소켓 유저 인증 성공 - memberNo: {}", .principalgetName());
//        log.info("STOMP 웹소켓 유저 인증 성공");
//    }
}
