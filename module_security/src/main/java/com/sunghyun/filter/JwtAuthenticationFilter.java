package com.sunghyun.filter;

import com.sunghyun.config.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = jwtProvider.resolveToken(request);

        if(StringUtils.hasText(accessToken) && jwtProvider.validate(accessToken)){
            // 토큰 검증 성공 시 뭘 해야할까?? 토큰의 데이터들은 어디서 쓰일까?
            // 인증객체를 꺼내서 인가 여부 판단하므로, 인가필터에서 사용할 수 있도록 시큐리티 컨텍스트에 세팅해줘야 한다.

            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }
}
