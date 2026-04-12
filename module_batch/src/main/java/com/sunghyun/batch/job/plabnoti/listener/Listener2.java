package com.sunghyun.batch.job.plabnoti.listener;

import com.sunghyun.batch.dto.NotificationTargetDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Listener2 implements ItemReadListener<NotificationTargetDto> {

    @Override
    public void beforeRead() {
        // 읽기 시작 전에는 별도 로그가 너무 많이 찍힐 수 있으므로 필요 시에만 사용
    }

    @Override
    public void afterRead(NotificationTargetDto item) {
        // 데이터 한 건을 읽을 때마다 실행 (디버깅용)
        log.debug(">>> [Reader] 데이터 읽기 성공: {}", item.getEmail());
    }

    @Override
    public void onReadError(Exception ex) {
        log.error(">>> [Reader] 읽기 중 에러 발생: {}", ex.getMessage());
    }
}