package com.sunghyun.notification.application.port.out;

import com.sunghyun.notification.domain.model.NotiHistory;

import java.util.List;

public interface NotiHistoryRepository {
    NotiHistory save(final NotiHistory notiHistory);
//    List<NotiHistory> getNotiHistoriesByMemberNo(final Long memberNo);
    List<NotiHistory> findAll();
}
