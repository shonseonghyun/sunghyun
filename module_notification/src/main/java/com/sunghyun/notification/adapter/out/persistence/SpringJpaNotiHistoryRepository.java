package com.sunghyun.notification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaNotiHistoryRepository extends JpaRepository<NotiHistoryEntity,Long> {
//    List<NotiHistoryEntity> findAllByMemberNo(final Long memberNo);
}
