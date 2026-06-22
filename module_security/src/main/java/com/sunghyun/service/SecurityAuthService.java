package com.sunghyun.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthService implements AuthService{
    private final AuthenticationManager authenticationManager;

    @Override
    public void login(final String id,final String pwd) {
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(id, pwd);
        authenticationManager.authenticate(authRequest);
    }
}
