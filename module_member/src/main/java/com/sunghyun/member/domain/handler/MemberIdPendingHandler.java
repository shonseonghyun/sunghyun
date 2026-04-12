package com.sunghyun.member.domain.handler;

public interface MemberIdPendingHandler {
    boolean lock(String key,String value,Long timeOut);
    void unlock(String id);
    void deletePendingId(String key);
    Object getPendingValue(String key);
    void deleteAllPendingIds(final String key);

}
