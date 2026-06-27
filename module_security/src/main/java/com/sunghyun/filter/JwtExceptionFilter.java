package com.sunghyun.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.exceptions.InvalidTokenException;
import com.sunghyun.web.GlobalResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
*   JwtAuthenticationFilter에서 예외 발생 시, 시큐리티 필터 체인을 2번 돌지 않도록 필터 삽입
*   2번 도는 이유??
*   시큐리티 예외후처리 담당인 ExceptionTranslationFilter까지 처리되기 전 예외가 던져지게 되어,
*   필터에서 처리되지 않은 예외가 WAS(Tomcat)까지 전달되면, WAS가 에러 처리를 위해 /error라는 경로로 내부 포워딩(재요청)**을 수행하기 때문입니다.
* */

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper om = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (InvalidTokenException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            GlobalResponse errorResponse = GlobalResponse.of(e.getErrorCode());
            response.getWriter().write(om.writeValueAsString(errorResponse));
        }
    }
}
