package com.sunghyun.config;

import java.util.Optional;

public interface SecurityUserLoader {
    Optional<SecurityUserDetail> loadUserById(final String id);
}
