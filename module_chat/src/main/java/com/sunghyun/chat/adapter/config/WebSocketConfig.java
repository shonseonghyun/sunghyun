package com.sunghyun.chat.adapter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final HandshakeInterceptor socketInterceptor;
    private final ChannelInterceptor authenticationChannelInterceptor;
    private final HandlerMethodArgumentResolver stompAuthMemberArgumentResolver;
    private final StompSubProtocolErrorHandler stompErrorHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(stompErrorHandler);

        // Websocket 연결 endpoint 설정
        registry.addEndpoint("/ws/chat")
//                .addInterceptors(socketInterceptor)
                // cors 설정 - 허용할 origin 패턴 설정
                .setAllowedOriginPatterns("*")
                // SockJs 지원 추가
//                .withSockJS()
        ;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독 경로 설정 - 클라이언트가 구독할 수 있는 endpoint 설정
        // 클라이언트느 이 prefix로 시작하는 주제를 구독할 수 있음
        registry.enableSimpleBroker(
                "/sub","/queue"
        );

        // 발행 경로 설정 - 클라이언트가 메시지를 발행할 때 사용할 prefix
        // 클라이언트가 메시지를 보낼 때는 이 prefix로 시작하는 endpoint로 메시지 전송
        registry.setApplicationDestinationPrefixes("/pub");

        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authenticationChannelInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(stompAuthMemberArgumentResolver);
    }
}