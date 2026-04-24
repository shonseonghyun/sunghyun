package com.sunghyun.batch.infrastructure.mapper;

import com.sunghyun.batch.dto.NotiHistoryDto;
import com.sunghyun.batch.dto.NotificationTargetDto;
import com.sunghyun.batch.dto.PlabMatchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper //MyBatis 매퍼 선언
public interface PlabNotiMapper {
    //PlabMath
    List<PlabMatchDto> getPlabMatches();
    void sync(final Long matchNo);

    //MatchSubscription
    List<NotificationTargetDto> findNotificationTargets();

    //NotiHistory
    void insertNotiHistory(final NotiHistoryDto notiHistoryDto);
}
