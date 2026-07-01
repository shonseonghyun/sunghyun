package com.sunghyun.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>컨트롤러 레이어 전용 인증 유저 정보 주입 애노테이션</h3>
 * * <p>
 * HTTP 요청의 Authorization 헤더(JWT)를 통해 인증된 회원의 정보를
 * 컨트롤러 메서드의 파라미터로 마법처럼 자동 주입해 주는 역할을 합니다.
 * </p>
 *
 * <h4>💡 핵심 장점 및 도입 목적</h4>
 * <ul>
 * <li><b>시큐리티 의존성 제거:</b> 비즈니스 모듈(예: plab)의 컨트롤러가 무거운 Spring Security 라이브러리 클래스를 직접 임포트하지 않도록 격리합니다.</li>
 * <li><b>코드 중복 제거:</b> SecurityContextHolder나 HttpServletRequest를 통해 유저 정보를 수동으로 뜯어내는 반복 코드를 제거합니다.</li>
 * <li><b>타입 안정성:</b> 단순 문자열 ID가 아닌, 규격화된 {@link com.sunghyun.dto.AuthMemberInfo} 객체로 안전하게 데이터를 받아옵니다.</li>
 * </ul>
 * * <h4>🛠️ 내부 작동 원리</h4>
 * <ol>
 * <li>인증 필터(JwtAuthenticationFilter)가 토큰을 검증한 뒤 {@code SecurityContextHolder}에 인증 객체를 심습니다.</li>
 * <li>Spring MVC 진입 시 {@link com.sunghyun.config.AuthMemberArgumentResolver}가 본 애노테이션을 감지합니다.</li>
 * <li>인증 객체의 {@code getDetails()}에서 추출한 {@code AuthMemberInfo}를 컨트롤러 파라미터에 자동으로 바인딩합니다.</li>
 * </ol>
 *
 * <h4>💻 사용 예시</h4>
 * <pre>
 * &#64;GetMapping("/api/v1/my-profile")
 * public ResponseEntity&lt;ProfileResponse&gt; getMyProfile(&#64;AuthMember AuthMemberInfo memberInfo) {
 * Long memberNo = memberInfo.getId(); // 시큐리티 라이브러리 없이 안전하게 PK 획득
 * ...
 * }
 * </pre>
 *
 * @author sunghyun
 * @see com.sunghyun.config.AuthMemberArgumentResolver
 * @see com.sunghyun.dto.AuthMemberInfo
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
//@JsonIgnore
public @interface AuthMember {
}
