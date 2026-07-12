package com.sunghyun.member.adpater.out.crypto;

import com.sunghyun.member.domain.exception.EmptyNewPasswordException;
import com.sunghyun.member.domain.exception.PasswordDuplicatedException;
import com.sunghyun.member.domain.exception.PasswordMismatchException;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.service.PasswordService;
import com.sunghyun.service.CustomPasswordEncoder;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class SecurityPasswordService implements PasswordService {
    private final CustomPasswordEncoder passwordEncoder;

    @Override
    public boolean updatePwd(String currentPwd, String newRawPwd, Member member) {
        // 1. 방어 코드: 둘 다 비어있다면 비밀번호를 변경하지 않겠다는 의미이므로 false 반환
        if (!StringUtils.hasText(currentPwd) && !StringUtils.hasText(newRawPwd)) {
            return false;
        }

        // 2. 하나는 쓰고 하나는 안 썼을 때의 예외 처리 (둘 다 세트로 입력되어야 함)
        if (!StringUtils.hasText(currentPwd) || !StringUtils.hasText(newRawPwd)) {
            throw new EmptyNewPasswordException(ErrorCode.M008); // 입력 누락 예외
        }

        // 3. 현재 비밀번호 검증
        if (!matches(currentPwd, member.getPwd())) {
            throw new PasswordMismatchException(ErrorCode.M005);
        }

        // 4. 새 비밀번호가 기존 비밀번호와 같은지 검증 (동일하면 에러)
        if (matches(newRawPwd, member.getPwd())) {
            throw new PasswordDuplicatedException(ErrorCode.M006); // 기존 비밀번호 재사용 불가 예외
        }

        // 5. 안전하게 새로운 비밀번호 인코딩 후 도메인 모델에 세팅
        member.setPwd(encodePwd(newRawPwd));

        // 6. 성공적으로 변경되었으므로 true 반환
        return true;
    }

    @Override
    public boolean checkPwd(String inputRawPwd,String encodedPwd) {
        return passwordEncoder.matches(inputRawPwd, encodedPwd);
    }

    @Override
    public String encodePwd(String inputRawPwd) {
        return passwordEncoder.encode(inputRawPwd);
    }

    private boolean matches(String inputPassword, String encodedPassword) {
        if (!StringUtils.hasText(inputPassword) || !StringUtils.hasText(encodedPassword)) {
            return false;
        }
        return passwordEncoder.matches(inputPassword, encodedPassword);
    }
}
