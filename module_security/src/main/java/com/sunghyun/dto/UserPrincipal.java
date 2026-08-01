package com.sunghyun.dto;

import java.security.Principal;

public class UserPrincipal implements Principal {

    private final AuthMemberInfo authMemberInfo;

    public UserPrincipal(AuthMemberInfo authMemberInfo) {
        this.authMemberInfo = authMemberInfo;
    }

    @Override
    public String getName() {
        return String.valueOf(authMemberInfo.getMemberNo());
    }

    public AuthMemberInfo getAuthMemberInfo() {
        return authMemberInfo;
    }
}