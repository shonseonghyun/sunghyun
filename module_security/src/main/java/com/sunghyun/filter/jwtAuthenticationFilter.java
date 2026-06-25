package com.sunghyun.filter;

import com.sunghyun.config.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class jwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("jwtAuthenticationFilter 진입");
        /*
        * 토큰 없이 들어온 경우
        *   1. 로그인 O, 필터 자연스레 패스?
        *   2. 로그인 X, 예외 던지기?
        * */

        final String accessToken = resolveToken(request);

        if(StringUtils.hasText(accessToken) && jwtProvider.validate(accessToken)){
            // 토큰 존재하며 검증 성공 시
            // 뭘 해야할까..?
            // 인가 관련 필터까지 봐야 알 수 있을 듯 하다. 확인해보니 인증객체를 꺼내서 인가 여부 판단한다.

            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 만약 그냥 filterChain.doFilter하지 않고 return만 하면?
//        return
        // 아 근데 해당 토큰이 들어오고 안들어오고는 중요한 게 아니야. 그냥 들어온 토큰이 있으면 해당 인증(또는 검증)만 하는거지.

        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
