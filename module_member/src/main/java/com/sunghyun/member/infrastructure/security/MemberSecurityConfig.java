package com.sunghyun.member.infrastructure.security;

import com.sunghyun.config.authorize.Role;
import com.sunghyun.config.authorize.SecurityMatcherChain;
import com.sunghyun.config.authorize.SecurityRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberSecurityConfig {

    @Bean
    public SecurityMatcherChain securityMatcherChain(){
        SecurityMatcherChain securityMatcherChain = new SecurityMatcherChain();

        securityMatcherChain.addAll(
                SecurityRequestMatcher.patterns(
                        "/member/login",
                        "/member/register",
                        "/member/valid-id/**"
                ).permitAll(),
                SecurityRequestMatcher.patterns(
                        "/member/**"
                ).hasRole(Role.USER)
        );

        return securityMatcherChain;
    }
}
