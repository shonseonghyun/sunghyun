//package com.sunghyun.service;
//
//import com.sunghyun.config.JwtProvider;
//import com.sunghyun.config.MemberLoginResDto;
//import com.sunghyun.config.SecurityUserDetails;
//import com.sunghyun.config.SecurityUserLoader;
//import com.sunghyun.dto.TokenReissueReqDto;
//import com.sunghyun.dto.TokenResponseDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class SecurityAuthService implements AuthService{
//    private final JwtProvider jwtProvider;
//    private final SecurityUserLoader securityUserLoader;
//
//    @Override
//    public MemberLoginResDto authenticate(final String id,final String pwd) {
//        final UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(id, pwd);
//
//        securityUserLoader.loadUserById(id);
//        final Authentication authentication = authenticationManager.authenticate(authRequest);
//        final SecurityUserDetails userDetail = (SecurityUserDetails) authentication.getPrincipal();
//
//        //JWT 토큰 생성 후 발급
//        final TokenResponseDto tokenResponseDto = jwtProvider.createToken(userDetail);
//
//        return MemberLoginResDto.builder()
//                .memberNo(userDetail.getMemberNo())
//                .name(userDetail.getName())
//                .id(userDetail.getUsername())
//                .tokens(tokenResponseDto)
//                .build()
//                ;
//    }
//
//    @Override
//    public void reissueAccessToken(TokenReissueReqDto tokenReissueReqDto) {
////        jwtProvider.reissueAcessToken();
//
//    }
//}
