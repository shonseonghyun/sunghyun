package com.sunghyun.config;

import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final SecurityUserLoader securityUserLoader;

    @Override
    public Authentication authenticate(Authentication authentication){
        final String id = authentication.getName() ;
        final String pwd = authentication.getCredentials().toString();

        //아이디,패스워드 일치 여부 검증
        // 1. id를 통해 회원 정보 가져오기(id,pwd)
        final SecurityUserDetail securityUserDetail = securityUserLoader.loadUserById(id)
                .orElseThrow(()-> {
                    log.error("존재하지 않는 아이디입니다.");
                    return new MemberIdNotFoundException(ErrorCode.M00);
                });

        // 2. 비밀번호 일치 여부 검증
        if(!securityUserDetail.getPassWord().equals(pwd)){
            log.error("비밀번호가 일치하지 않습니다.");
            throw new PasswordMismatchException(ErrorCode.M05);
        }

        // 인증 토큰 생성
        final UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(securityUserDetail,id, Collections.emptyList());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
