package com.sunghyun.chat.adapter.config;

import com.sunghyun.config.authorize.Role;
import com.sunghyun.config.authorize.SecurityMatcherChain;
import com.sunghyun.config.authorize.SecurityRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatSecurityConfig {
    @Bean
    public SecurityMatcherChain securityMatcherChain(){
        SecurityMatcherChain securityMatcherChain = new SecurityMatcherChain();

        securityMatcherChain.addAll(
                SecurityRequestMatcher.patterns(
                        "/ws/chat/**"
                ).permitAll(),
                SecurityRequestMatcher.patterns(
                        "/chat/**"
                ).hasRole(Role.USER)
        );

        return securityMatcherChain;
    }
}
