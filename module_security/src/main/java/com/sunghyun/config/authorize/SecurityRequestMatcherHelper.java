package com.sunghyun.config.authorize;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityRequestMatcherHelper {
    private final List<SecurityMatcherChain> securityMatcherChains;

    public AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry setAuthorizedRequest(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth){
        for(SecurityMatcherChain securityMatcherChain:securityMatcherChains){
            for(SecurityRequestMatcher requestMatcher: securityMatcherChain.getMatchers()){
                toAuthorizedRequestFrom(auth,requestMatcher);
            }
        }

        return auth.anyRequest().authenticated();
    }

    private void toAuthorizedRequestFrom(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth, SecurityRequestMatcher requestMatcher){
        if(requestMatcher.isAnyRequest()){
            setAuthorized(auth.anyRequest(),requestMatcher);
        }
        else if (requestMatcher.isUrlOnly()) {
            setAuthorized(auth.requestMatchers(requestMatcher.getPatterns()),requestMatcher);
        }
        else{
            setAuthorized(auth.requestMatchers(requestMatcher.getHttpMethod(),requestMatcher.getPatterns()),requestMatcher);
        }
    }

    private void setAuthorized(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl patterns, SecurityRequestMatcher requestMatcher){
        switch (requestMatcher.getRequestHookType()) {
            case AUTHENTICATED:
                patterns.authenticated();
                break;
            case PERMIT_ALL:
                patterns.permitAll();
                break;
            case DENY_ALL:
                patterns.denyAll();
                break;
            case HAS_ROLE:
                patterns.hasRole(requestMatcher.getRole());
                break;
        }
    }
}
