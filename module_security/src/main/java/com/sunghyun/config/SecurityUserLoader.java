package com.sunghyun.config;

import com.sunghyun.dto.SecurityMemberDto;

import java.util.Optional;

public interface SecurityUserLoader {
    Optional<SecurityMemberDto> loadUserById(final String id);
}
