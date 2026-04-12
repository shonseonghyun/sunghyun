package com.sunghyun.plab.match.domain.model;

import com.sunghyun.feign.dto.PlabMatchResponseDto;
import com.sunghyun.plab.match.domain.exception.InvalidPlabMatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlabMatchTest {

    @Test
    @DisplayName("플랩 매치 도메인 생성 시 이미 시작된 매치인 경우 InvalidPlabMatchException 예외 던진다.")
    void should_throw_InvalidPlabMatchException_when_match_is_past(){
        //given
        final Long plabMatchNo = 1L;
        // 현재 시간보다 1일 전으로 설정 (항상 과거)
        String pastSchedule = OffsetDateTime.now().minusDays(1).toString();

        PlabMatchResponseDto result = PlabMatchResponseDto.builder()
                .id(plabMatchNo)
                .schedule(pastSchedule)
                .build()
                ;

        //when,then
        assertThatThrownBy(()->PlabMatch.create(plabMatchNo,result))
                .isInstanceOf(InvalidPlabMatchException.class)
                ;
    }

    @Test
    @DisplayName("플랩 매치 도메인 생성 시 시작되지 않은 매치인 경우 생성된다.")
    void should_create_match_when_match_is_future() {
        //given
        final Long plabMatchNo = 1L;
        // 현재 시간보다 1일 후로 설정 (항상 미래)
        String futureSchedule = OffsetDateTime.now().plusDays(1).toString();

        PlabMatchResponseDto result = PlabMatchResponseDto.builder()
                .id(plabMatchNo)
                .schedule(futureSchedule)
                .build();

        //when
        PlabMatch plabMatch = PlabMatch.create(plabMatchNo,result);

        //then
        assertThat(plabMatch.getPlabMatchNo()).isEqualTo(plabMatchNo);
        assertThat(plabMatch.getMatchDt()).isNotNull();
    }

}