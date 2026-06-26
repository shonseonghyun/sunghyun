package com.sunghyun.config;

import com.sunghyun.config.authorize.SecurityRequestMatcherHelper;
import com.sunghyun.filter.jwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
    스프링 시큐리티
    Spring 기반의 애플리케이션 보안(인증/인가)을 담당하는 프레임워크
    - 인증(Authentication): 사용자가 누구인지 확인하는 절차
    - 인가(Authorization) : 인증 완료된 사용자가 요청한 자원(url)에 접근한지 확인하는 절차
*/
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtProvider jwtProvider;
    private final SecurityRequestMatcherHelper securityRequestMatcherHelper;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
////                 1. 모든 요청에 대해 인증(로그인)을 요구하도록 설정
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()
//                )
////                 2. 🌟 시큐리티 기본 폼 로그인 창을 활성화! (이게 있어야 창이 뜹니다)
//                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll) // 🔥 [핵심] 로그인 화면과 로그인 처리 URL은 인증 없이 통과하도록 허용!
//                .sessionManagement(
//                        httpSecuritySessionManagementConfigurer ->
//                                httpSecuritySessionManagementConfigurer
//                                        .maximumSessions(2)
//                                        .maxSessionsPreventsLogin(false)
//                                        .expiredUrl("/test")
//                )
                .authorizeHttpRequests(securityRequestMatcherHelper::setAuthorizedRequest)
                .formLogin(AbstractHttpConfigurer::disable) // 시큐리티에서 제공하는 HTTP 기반의 폼 로그인 기반 인증방식 -> 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(CsrfConfigurer::disable) // Rest Api 방식을 사용하기에 비활성화 처리
                .sessionManagement(AbstractHttpConfigurer::disable) // JWT 통해 세션 관리 할 것이기에 비활성화
//                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // https://stackoverflow.com/questions/49081493/what-is-difference-between-disabled-and-stateless-session-management
                .addFilterBefore(new jwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
