package com.sunghyun.config;

import com.sunghyun.dto.TokenResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${security.jwt.secret.key:sunghyun-secret-key-0123456789-abcdefg}")
    private String secretKey;

    // 액세스 토큰 만료 시간 30분
    @Value("${security.jwt.access.token.expire:1800000}")
    private Long accessTokenExpirationTime;

    // 리프레시 토큰 만료 시간: 2주
    @Value("${security.jwt.refresh.token.expire:1209600000}")
    private Long refreshTokenExpirationTime;

    /**
     * 인증 성공 정보를 받아 JWT Access Token을 발급합니다.
     */
    public TokenResponseDto createToken(SecurityUserDetail securityUserDetail) {
        final String accessToken = createAccessToken(securityUserDetail);
        final String refreshToken = createRefreshToken(securityUserDetail);


        log.info("JWT 토큰 세트(Access & Refresh) 발급 완료 - User: {}", securityUserDetail.getId());

        // 5. 공통 DTO 규격으로 래핑하여 리턴
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String  createAccessToken(SecurityUserDetail securityUserDetail){
        // 1. 유저 식별자(ID) 추출
        final String id = securityUserDetail.getName();

        // 2. 권한 목록을 콤마(,) 기준으로 파싱하여 문자열로 가공 (ex: "ROLE_USER,ROLE_ADMIN")
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date accessTokenExpiresIn = new Date(now + accessTokenExpirationTime);

        // 3. 0.12.x 관례에 맞게 암호화 알고리즘에 쓰일 SecretKey 객체 생성
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 4. JJWT 0.12.x 빌더 패턴 적용하여 토큰 생성
        String accessToken = Jwts.builder()
                .subject(id)                    // 토큰 주인 명시 (sub)
//                .claim("auth", authorities)           // 커스텀 클레임으로 권한 정보 주입
                .issuedAt(new Date(now))              // 발행 시간 (iat)
                .expiration(accessTokenExpiresIn)     // 만료 시간 (exp) -> 기존 setExpiration에서 변경됨
                .signWith(key)                        // S알고리즘을 생략해도 key 규격을 보고 안전한 알고리즘(HS256 등)을 자동 선택합니다.
                .compact();                           // 직렬화 및 컴팩트화

        return accessToken;
    }

    private String createRefreshToken(SecurityUserDetail securityUserDetail){
        final String id = securityUserDetail.getName();

        long now = System.currentTimeMillis();
        Date refreshTokenExpiresIn = new Date(now + refreshTokenExpirationTime);
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 리프레시 토큰은 권한(auth claim) 정보를 담지 않고 최소한의 정보(sub)만 보관하여 무겁지 않게 관리합니다.
        return Jwts.builder()
                .subject(id)
                .issuedAt(new Date(now))
                .expiration(refreshTokenExpiresIn)
                .signWith(key)
                .compact();
    }

}
