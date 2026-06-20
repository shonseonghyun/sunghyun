package com.sunghyun.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.dto.LoginReqDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
//@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper om;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
//        if (this.postOnly && !request.getMethod().equals("POST")) {
//            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
//        }
//        LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);
        LoginReqDto loginReqDto = null;
        try {
            loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final String id = loginReqDto.getId();
        final String pwd = loginReqDto.getPwd();

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(id,pwd);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
