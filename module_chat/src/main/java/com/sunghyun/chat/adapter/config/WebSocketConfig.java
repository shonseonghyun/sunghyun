package com.sunghyun.chat.adapter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import reactor.netty.tcp.TcpClient;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final HandshakeInterceptor socketInterceptor;
    private final ChannelInterceptor authenticationChannelInterceptor;
    private final HandlerMethodArgumentResolver stompAuthMemberArgumentResolver;
    private final StompSubProtocolErrorHandler stompErrorHandler;

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(stompErrorHandler);

        // Websocket 연결 endpoint 설정
        registry.addEndpoint("/ws/chat")
//                .addInterceptors(socketInterceptor)
                // cors 설정 - 허용할 origin 패턴 설정
                // 실제 환경에선 API서버 도메인만 허용
                .setAllowedOriginPatterns("*")
                // SockJs 지원 추가
//                .withSockJS()
        ;
    }

/*    @Override
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
    }    */

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        TcpClient tcpClient = TcpClient
                .create()
                .host(RABBITMQ_HOST)
                .port(61613);
//                .secure(SslProvider.defaultClientProvider());
        ReactorNettyTcpClient<byte[]> client = new ReactorNettyTcpClient<>(tcpClient, new StompReactorNettyCodec());

        // 💡 1. 메모리 브로커(enableSimpleBroker) 대신 RabbitMQ 브로커 릴레이 설정 적용
        //클라이언트는 해당 prefix로 시작된 경로에 대해서만 구독할 수 있음
//        registry.enableStompBrokerRelay("/sub", "/queue")
        registry.enableStompBrokerRelay("/topic", "/queue", "/exchange", "/amq/queue")
                .setTcpClient(client) // RabbitMQ와 연결할 클라이언트 설정
                .setRelayHost(RABBITMQ_HOST)
                .setRelayPort(61613)       // RabbitMQ STOMP 플러그인 기본 포트
                .setClientLogin("guest")   // RabbitMQ 계정
                .setClientPasscode("guest"); // RabbitMQ 비밀번호

        // 발행 경로 설정 - 클라이언트가 메시지를 발행할 때 사용할 prefix
        // 클라이언트가 메시지를 보낼 때는 이 prefix로 시작하는 endpoint로 메시지 전송
        registry.setApplicationDestinationPrefixes("/pub");

        registry.setUserDestinationPrefix("/user");

//        registry.setPathMatcher(new AntPathMatcher(".")); // url을 chat/room/3 -> chat.room.3으로 참조하기 위한 설정
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