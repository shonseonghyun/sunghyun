package com.sunghyun.utils;

import com.sunghyun.annotation.UpdateAble;
import com.sunghyun.exception.MergeException;
import com.sunghyun.web.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApiUtilsTest {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestEntity {
        @UpdateAble(ignoreNull = true) // null이면 업데이트 안 함
        private String name;

        @UpdateAble(ignoreNull = false) // null이어도 업데이트 함 (기존값 삭제용)
        private String description;

        private String secret; // 어노테이션 없어서 머지 안 됨
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestEntity2 {
        private String name;
        private String description;
        private String secret;
    }

    private final static String NAME = "NAME";
    private final static String DESC = "DESC";
    private final static String SECRET = "SECRET";

    private final static String MOD_NAME = "MOD_NAME";
    private final static String MOD_DESC = "MOD_DESC";
    private final static String MOD_SECRET = "MOD_SECRET";


    @Test
    @DisplayName("target이 전부 null인 경우, ignoreNull true는 기존 값유지하고 false는 null로 업데이트하고 없는 경우 기존값 유지한고 true 리턴한다")
    void merge_ShouldApplyUpdateBasedOnIgnoreNullOption(){
        //given
        TestEntity source = new TestEntity(null,null,null);
        TestEntity target = new TestEntity(NAME,DESC,SECRET);

        //when
        boolean result = ApiUtils.merge(source,target);

        //then
        assertThat(target.getName()).isEqualTo(NAME);
        assertThat(target.getDescription()).isEqualTo(null);
        assertThat(target.getSecret()).isEqualTo(SECRET);
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("target이 source와 전부 다른 경우, 어노테이션 없는 필드 제외하고 전부 업데이트되고 true 리턴한다")
    void merge_ShouldApplyUpdate(){
        //given
        TestEntity source = new TestEntity(MOD_NAME,MOD_DESC,MOD_SECRET);
        TestEntity target = new TestEntity(NAME,DESC,SECRET);

        //when
        boolean result = ApiUtils.merge(source,target);

        //then
        assertThat(target.getName()).isEqualTo(MOD_NAME);
        assertThat(target.getDescription()).isEqualTo(MOD_DESC);
        assertThat(target.getSecret()).isEqualTo(SECRET);
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("변경 사항이 없는 경우, false 리턴한다.")
    void merge_ShouldReturnFalse_WhenNoDataChanges(){
        //given
        TestEntity source = new TestEntity(NAME,DESC,SECRET);
        TestEntity target = new TestEntity(NAME,DESC,SECRET);

        //when
        boolean result = ApiUtils.merge(source,target);

        //then
        assertThat(target.getName()).isEqualTo(NAME);
        assertThat(target.getDescription()).isEqualTo(DESC);
        assertThat(target.getSecret()).isEqualTo(SECRET);
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("동일한 인스턴스 들어올 경우, false 리턴한다.")
    void merge_ShouldReturnFalse_WhenSameInstance(){
        //given
        TestEntity source = new TestEntity(NAME,DESC,SECRET);
        TestEntity target = new TestEntity(NAME,DESC,SECRET);

        //when
        boolean result = ApiUtils.merge(source,source);

        //then
        assertThat(target.getName()).isEqualTo(NAME);
        assertThat(target.getDescription()).isEqualTo(DESC);
        assertThat(target.getSecret()).isEqualTo(SECRET);
        assertThat(result).isEqualTo(false);
    }


    @Test
    @DisplayName("클래스 타입이 다를 경우 MergeException 던진다")
    void testMergeException(){
        //given
        TestEntity source = new TestEntity(NAME,DESC,SECRET);
        TestEntity2 target = new TestEntity2(NAME,DESC,SECRET);

        //when,then
        assertThatThrownBy(()->ApiUtils.merge(source,target))
                .isInstanceOf(MergeException.class)
                .extracting("errorCode") // ErrorCode 필드 추출
                .isEqualTo(ErrorCode.G02);
                ;
    }

    @Test
    @DisplayName("2026-03-30T20:00:00+09:00 형식을 20260330, 2000 으로 파싱한다.")
    void testDateParsing() {
        //given
        String schedule = "2026-03-30T20:00:00+09:00";

        //when
        String date = ApiUtils.parseDate(schedule);
        String time = ApiUtils.parseTime(schedule);

        //then
        assertThat(date).isEqualTo("20260330");
        assertThat(time).isEqualTo("2000");
    }

}