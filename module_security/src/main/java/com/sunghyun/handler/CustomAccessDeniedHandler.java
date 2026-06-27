package com.sunghyun.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
*   인증은 되었지만 권한 없는 사용자가 권한 필요한 엔드포인트 접근 시 발생하는 403 Forbidden 예외를 핸들링하는 핸들러
* */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper om;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        GlobalResponse errorResponse = GlobalResponse.of(ErrorCode.T05);
        response.getWriter().write(om.writeValueAsString(errorResponse));
    }
}
