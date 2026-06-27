package com.sunghyun.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
* 인증이 필요한 엔드포인트에 인증되지 않은 사용자가 접근 시 401 unauthorized 예외를 핸들링하는 핸들러
* */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper om;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        GlobalResponse errorResponse = GlobalResponse.of(ErrorCode.T06);
        response.getWriter().write(om.writeValueAsString(errorResponse));
    }
}
