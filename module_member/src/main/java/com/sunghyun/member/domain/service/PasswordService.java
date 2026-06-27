package com.sunghyun.member.domain.service;

import com.sunghyun.member.domain.exception.EmptyNewPasswordException;
import com.sunghyun.member.domain.exception.PasswordMismatchException;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class PasswordService {

    public boolean updatePwd(String currentPwd, String newPwd, Member member){
        if(StringUtils.hasText(currentPwd) || StringUtils.hasText(newPwd)){
            if(!member.getPwd().equals(currentPwd)){
                throw new PasswordMismatchException(ErrorCode.M005);
            }

            if(!StringUtils.hasText(newPwd)){
                throw new EmptyNewPasswordException(ErrorCode.M006);
            }

            else{
                if(!member.getPwd().equals(newPwd)){
                    //새로운 비밀번호 세팅
                    member.setPwd(newPwd);
                    return true;
                }
            }
        }

        return false;
    }
}
