package com.sunghyun.config;

import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

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
        final SecurityUserDetails securityUserDetails = securityUserLoader.loadUserById(id)
                .map(SecurityUserDetails::new)
                .orElseThrow(()-> {
                    log.error("존재하지 않는 아이디입니다.");
                    return new MemberIdNotFoundException(ErrorCode.M00);
                })
                ;

        // 2. 비밀번호 일치 여부 검증
        if(!securityUserDetails.getPassword().equals(pwd)){
            log.error("비밀번호가 일치하지 않습니다.");
            throw new PasswordMismatchException(ErrorCode.M05);
        }

        // 인증 토큰 생성
        final UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(securityUserDetails,id, securityUserDetails.getAuthorities());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
