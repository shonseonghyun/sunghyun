package com.sunghyun.plab.match.application.port.out.repository;

public interface LockRepository {
    boolean getLock(final Long plabMatchNo);
    void unlock(final Long plabMatchNo);
}
