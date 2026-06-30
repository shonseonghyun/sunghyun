package com.sunghyun.config;

import com.sunghyun.dto.TokenReqDto;
import com.sunghyun.dto.TokenResDto;
import com.sunghyun.exceptions.InvalidTokenException;
import com.sunghyun.web.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${security.jwt.secret.key:sunghyun-secret-key-0123456789-abcdefg}")
    private String secretKey;

    // 액세스 토큰 만료 시간 30분
    @Value("${security.jwt.access.token.expire}")
    private Long accessTokenExpirationTime;

    // 리프레시 토큰 만료 시간: 2주
    @Value("${security.jwt.refresh.token.expire}")
    private Long refreshTokenExpirationTime;

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 인증 성공 정보를 받아 JWT Access Token을 발급합니다.
     */
    public TokenResDto generateToken(TokenReqDto tokenReqDto) {
        final String accessToken = generateAccessToken(tokenReqDto);
        final String refreshToken = generateRefreshToken(tokenReqDto);


        log.info("JWT 토큰 세트(Access & Refresh) 발급 완료 - Member Id: {}", tokenReqDto.getId());

        // 5. 공통 DTO 규격으로 래핑하여 리턴
        return TokenResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResDto reissueAccessToken(TokenReqDto tokenReqDto, String refreshToken) {
        // 1. 토큰 내부의 클레임(Claims) 내 id 추출 + 토큰 검증
        final String id = parseToken(refreshToken).getSubject();

        // refreshToken 검증
        validateRefreshToken(tokenReqDto,id);

        log.info("Refresh Token 발급 완료 - Member Id: {}", tokenReqDto.getId());

        // 공통 DTO 규격으로 래핑하여 리턴
        return TokenResDto.builder()
                .accessToken(generateAccessToken(tokenReqDto))
                .build()
                ;
    }

    private void validateRefreshToken(TokenReqDto tokenReqDto, String id){
        if(!StringUtils.hasText(id)){
            log.error("토큰 내 subject가 비어있습니다.");
            throw new InvalidTokenException(ErrorCode.T03);
        }

        if(!id.equals(tokenReqDto.getId())){
            log.error("토큰 내 정보와 요청 정보가 일치하지 않습니다. 토큰 내 id[{}] / 인입된 id[{}]", tokenReqDto.getId(),id);
            throw new InvalidTokenException(ErrorCode.T03);
        }
    }

    private String generateAccessToken(TokenReqDto tokenReqDto){
        // 1. id,memberNo,role 추출
        final String id = tokenReqDto.getId();
        final Long memberNo = tokenReqDto.getMemberNo();
        final String roles = tokenReqDto.getRoles().stream()
                .map(role -> "ROLE_"+role)
                .collect(Collectors.joining(","))
                ;

        long now = System.currentTimeMillis();
        Date accessTokenExpiresIn = new Date(now + accessTokenExpirationTime);

        // 3. 0.12.x 관례에 맞게 암호화 알고리즘에 쓰일 SecretKey 객체 생성
        SecretKey key = getSigningKey();

        // 4. JJWT 0.12.x 빌더 패턴 적용하여 토큰 생성
        String accessToken = Jwts.builder()
                .subject(id)
                .claim("memberNo", memberNo)
                .claim("roles", roles)           // 커스텀 클레임으로 권한 정보 주입
                .issuedAt(new Date(now))              // 발행 시간 (iat)
                .expiration(accessTokenExpiresIn)     // 만료 시간 (exp) -> 기존 setExpiration에서 변경됨
                .signWith(key)                        // S알고리즘을 생략해도 key 규격을 보고 안전한 알고리즘(HS256 등)을 자동 선택합니다.
                .compact();                           // 직렬화 및 컴팩트화

        return accessToken;
    }

    private String generateRefreshToken(TokenReqDto tokenReqDto){
        final String id = tokenReqDto.getId();

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

    public boolean validate(final String token) {
        parseToken(token); // 예외 터지면 throw되므로, 해당 메소드 넘어가면 true 리턴
        return true;
    }

    public Authentication getAuthentication(final String token){
        // 1. 토큰 내부의 클레임(Claims) 추출
        Claims claims = parseToken(token);

        // 2. 클레임에서 "roles"에 문자열로 압축해 둔 권한 목록 꺼내기 (ex: "ROLE_USER,ROLE_ADMIN")
        String authoritiesClaim = claims.get("roles", String.class);

        if (authoritiesClaim == null || authoritiesClaim.isEmpty()) {
            throw new InvalidTokenException(ErrorCode.T04);
        }

        // 3. 콤마(,) 기준 문자열을 시큐리티가 이해하는 GrantedAuthority 컬렉션으로 복구
        List<SimpleGrantedAuthority> roles = Arrays.stream(authoritiesClaim.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 4. Principal 자리에 넣을 유저 식별자(ID)
        final String id = claims.getSubject();
        final Long memberNo = claims.get("memberNo",Long.class);

        // 5. 시큐리티 표준 인증 객체 생성 (비밀번호는 이미 토큰 검증이 끝나서 안 담아도 되므로 null 처리)
        // 세 번째 인자인 authorities까지 반드시 넘겨줘야 시큐리티가 인증 완료 상태로 판단
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, null, roles);
        authenticationToken.setDetails(memberNo);
        return authenticationToken;
    }



    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims parseToken(final String token){
        try {
            SecretKey key = getSigningKey();
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    ;
        } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            log.error("잘못된 JWT 서명 또는 손상된 토큰입니다.");
            throw new InvalidTokenException(ErrorCode.T00);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다. (Expired)");
            throw new InvalidTokenException(ErrorCode.T00);
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            log.error("지원되지 않는 형식의 JWT 토큰입니다.");
            throw new InvalidTokenException(ErrorCode.T00);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰의 클레임이 비어있거나 올바르지 않습니다.");
            throw new InvalidTokenException(ErrorCode.T00);
        }
    }


}
