package com.sunghyun.config;

import com.sunghyun.config.authorize.Role;
import com.sunghyun.config.authorize.SecurityMatcherChain;
import com.sunghyun.config.authorize.SecurityRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class TeamSecurityConfig {
    @Bean
    public SecurityMatcherChain securityMatcherChain(){
        SecurityMatcherChain securityMatcherChain = new SecurityMatcherChain();

        securityMatcherChain.addAll(
                // 1. 내 팀 정보 조회 (GET /api/v1/teams/my) -> USER 권한 필요
                // (* 매처보다 먼저 선언되어야 /my가 permitAll에 걸리지 않습니다)
                SecurityRequestMatcher.patterns(HttpMethod.GET, "/api/v1/teams/my")
                        .hasRole(Role.USER),

                // 2. 단건 팀 상세 조회 (GET /api/v1/teams/{teamNo}) -> 누구나 허용
                // (GET 메서드만 한정하여 PUT, DELETE 등은 허용되지 않도록 방지)
                SecurityRequestMatcher.patterns(HttpMethod.GET, "/api/v1/teams/*")
                        .permitAll(),

                // 3. 기타 모든 팀 관련 API (POST /api/v1/teams 포함) -> USER 권한 필요
                SecurityRequestMatcher.patterns("/api/v1/teams/**")
                        .hasRole(Role.USER),

                // 4. 내부 서비스 통신 API -> 누구나 허용 (또는 내부 IP/인증 정책 적용)
                SecurityRequestMatcher.patterns("/internal/v1/teams/**")
                        .permitAll(),

                //shedulePoll
                SecurityRequestMatcher.patterns(
                        "/api/v1/schedule-polls/**"
                ).hasRole(Role.USER),
                SecurityRequestMatcher.patterns(
                        "/internal/v1/schedule-polls/**"
                ).permitAll(),

                //guest
                SecurityRequestMatcher.patterns(
                        "/api/v1/schedules/**"
                ).hasRole(Role.USER),

                SecurityRequestMatcher.patterns(
                        HttpMethod.POST, "/api/v1/recruit/guests/*/apply"
                ).hasRole(Role.USER),
                SecurityRequestMatcher.patterns(
                        "/api/v1/recruit/guests/**",
                        "/internal/v1/recruits/guests/**"
                ).permitAll()

        );
        return securityMatcherChain;
    }
}