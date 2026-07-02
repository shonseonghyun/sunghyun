package com.sunghyun.plab.config;

import com.sunghyun.config.authorize.Role;
import com.sunghyun.config.authorize.SecurityMatcherChain;
import com.sunghyun.config.authorize.SecurityRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlabSecurityConfig {

    @Bean
    public SecurityMatcherChain securityMatcherChain(){
        SecurityMatcherChain securityMatcherChain = new SecurityMatcherChain();

        securityMatcherChain.addAll(
                SecurityRequestMatcher.patterns(
                        "/plab/match/**"
                ).hasRole(Role.ADMIN),
                SecurityRequestMatcher.patterns(
                        "/plab/**"
                ).hasRole(Role.USER)
        );

        return securityMatcherChain;
    }
}
