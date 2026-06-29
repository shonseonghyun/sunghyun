package com.sunghyun.member.domain.service;

import com.sunghyun.member.domain.model.Member;

public interface PasswordService {
    boolean updatePwd(String currentPwd, String newRawPwd, Member member);
    boolean checkPwd(final String inputRawPwd,final String encodedPwd);
    String encodePwd(final String inputRawPwd);
}
