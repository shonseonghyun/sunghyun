package com.sunghyun.member.application.port;

public interface MemberIdPendingRepository {
    boolean lock(final String id,final String value);
    void unlock(final String id);
    void deletePendingId(final String id);
    Object getPendingValue(final String id);
    void deleteAllPendingIds(final String id);

}
