package com.sunghyun.plab.domain.enums;

import com.sunghyun.plab.match.domain.enums.ActiveSubType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveSubTypeTest {

    @Test
    @DisplayName("모든 서브 활성화되어있지 않은 경우, NONE 리턴한다.")
    void test(){
        //given
        final boolean isSuperSub = false;
        final boolean isManagerFree = false;

        //when
        ActiveSubType activeSubType =ActiveSubType.getSubType(isSuperSub,isManagerFree);

        //then
        assertThat(activeSubType).isEqualTo(ActiveSubType.NONE);
    }

    @Test
    @DisplayName("슈퍼서브만 활성화된 경우, SUPER_SUB 리턴한다.")
    void test1(){
        //given
        final boolean isSuperSub = true;
        final boolean isManagerFree = false;

        //when
        ActiveSubType activeSubType =ActiveSubType.getSubType(isSuperSub,isManagerFree);

        //then
        assertThat(activeSubType).isEqualTo(ActiveSubType.SUPER_SUB);
    }


    @Test
    @DisplayName("매니저 서브만 활성화된 경우, MANAGER_SUB 리턴한다.")
    void test3(){
        //given
        final boolean isSuperSub = false;
        final boolean isManagerFree = true;

        //when
        ActiveSubType activeSubType =ActiveSubType.getSubType(isSuperSub,isManagerFree);

        //then
        assertThat(activeSubType).isEqualTo(ActiveSubType.MANAGER_SUB);
    }


    @Test
    @DisplayName("모두 활성화된 경우, ALL 리턴한다.")
    void test4(){
        //given
        final boolean isSuperSub = true;
        final boolean isManagerFree = true;

        //when
        ActiveSubType activeSubType =ActiveSubType.getSubType(isSuperSub,isManagerFree);

        //then
        assertThat(activeSubType).isEqualTo(ActiveSubType.ALL);
    }
}