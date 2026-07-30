package com.sunghyun.chat.adapter.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info("SocketInterceptor.beforeHandshake");
        HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();

        // 근데 이거 궁금한 게 있는데 내가 알기로 핸드쉐이크에서 http header에 세팅이 안된다고 알고 있어. 이 말이 맞다면 header 세팅된 게 없으므로 여기서 get해도 읽어올 게 없는 거 아니야?
        String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(bearerToken);

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("SocketInterceptor.afterHandshake");
    }
}