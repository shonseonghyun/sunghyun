package com.sunghyun.member.adpater.out.crypto;

import com.sunghyun.member.adpater.out.persistence.entity.MemberEntity;
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
        return false;
    }

    @Override
    public boolean checkPwd(String inputRawPwd,String encodedPwd) {
        return passwordEncoder.matches(inputRawPwd, encodedPwd);
    }

    @Override
    public String encodePwd(String inputRawPwd) {
        return passwordEncoder.encode(inputRawPwd);
    }

//    public boolean updatePwd(String currentPwd, String newRawPwd, MemberEntity memberEntity){
//        // 1. 방어 코드: 둘 다 비어있다면 비밀번호를 변경하지 않겠다는 의미이므로 아무것도 안 하고 통과
//        if (!StringUtils.hasText(currentPwd) && !StringUtils.hasText(newRawPwd)) {
//            return false;
//        }
//
//        // 2. 하나는 쓰고 하나는 안 썼을 때의 예외 처리 (둘 다 세트로 입력되어야 함)
//        if (!StringUtils.hasText(currentPwd) || !StringUtils.hasText(newRawPwd)) {
//            throw new EmptyNewPasswordException(ErrorCode.M008); // 입력 누락 예외
//        }
//
//        // 3. 현재 비밀번호 검증
//        if (!matches(currentPwd, memberEntity.getPwd())) {
//            throw new PasswordMismatchException(ErrorCode.M005);
//        }
//
//        // 4. 새 비밀번호가 기존 비밀번호와 같은지 검증 (동일하면 바꿀 필요가 없음)
//        if (matches(newRawPwd, memberEntity.getPwd())) {
//            throw new PasswordDuplicatedException(ErrorCode.M006); // (선택) 기존 비밀번호 재사용 불가 예외
//        }
//
//        // 5. 드디어 안전하게 새로운 비밀번호 세팅
//        // (만약 엔티티 내부에 changePassword() 메서드를 만들었다면 member.changePassword(...)를 호출하는 게 DDD에 맞습니다!)
//        memberEntity.setPwd(encode(newRawPwd));
//
//        return false;
//    }

    private boolean matches(String inputPassword, String encodedPassword) {
        if (!StringUtils.hasText(inputPassword) || !StringUtils.hasText(encodedPassword)) {
            return false;
        }
        return passwordEncoder.matches(inputPassword, encodedPassword);
    }
}
