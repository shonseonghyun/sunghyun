package com.sunghyun.member.application.dto;

import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.adpater.out.persistence.entity.MemberEntity;
import com.sunghyun.utils.ApiUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberEntityModifyReqDtoTest {

    @Test
    @DisplayName("null 무시하는 필드 경우 기존 존재하는 필드를 null로 세팅 시 기존 필드값 유지한다")
    void test1() throws IllegalAccessException {
        //given
        final String email = "sunghyun7895@naver.com";
        MemberEntity targetMemberEntity = MemberEntity.builder()
                .email(email)
                .build()
                ;

        MemberEntity sourceReqDtoToDomain = MemberModifyReqDto.builder()
                .email(null)
                .build()
                .toDomain()
                ;

        //when
        ApiUtils.merge(sourceReqDtoToDomain, targetMemberEntity);

        //then
        assertThat(targetMemberEntity.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("null 무시하는 필드 경우 기존 존재하는 필드를 새로운 값으로 세팅 시 새로운 필드값 세팅한다")
    void test2() throws IllegalAccessException {
        //given
        final String oldEmail = "sunghyun7895@naver.com";
        final String newEmail = "john7895@naver.com";
        MemberEntity targetMemberEntity = MemberEntity.builder()
                .email(oldEmail)
                .build()
                ;

        MemberEntity sourceReqDtoToDomain = MemberModifyReqDto.builder()
                .email(newEmail)
                .build()
                .toDomain()
                ;

        //when
        ApiUtils.merge(sourceReqDtoToDomain, targetMemberEntity);

        //then
        assertThat(targetMemberEntity.getEmail()).isEqualTo(newEmail);
    }

    @Test
    @DisplayName("null 무시하지 않는 필드 경우 null 요청 시엔 null값이 세팅된다")
    void test3() throws IllegalAccessException {
        //given
        final String tel = "7957895";

        MemberEntity targetMemberEntity = MemberEntity.builder()
                .tel(tel)
                .build()
                ;

        MemberEntity sourceReqDtoToDomain = MemberModifyReqDto.builder()
                .tel(null)
                .build()
                .toDomain()
                ;

        //when
        ApiUtils.merge(sourceReqDtoToDomain, targetMemberEntity);

        //then
        assertThat(targetMemberEntity.getTel()).isEqualTo(null);
    }

    @Test
    @DisplayName("null 무시하지 않는 필드 경우 새로운 값 세팅 시 새로운 값 세팅된고, true 리턴된다")
    void test4() throws IllegalAccessException {
        //given
        final String tel = "7957895";

        MemberEntity targetMemberEntity = MemberEntity.builder()
                .tel(null)
                .build()
                ;

        MemberEntity sourceReqDtoToDomain = MemberModifyReqDto.builder()
                .tel(tel)
                .build()
                .toDomain()
                ;

        //when
        boolean updateFlg = ApiUtils.merge(sourceReqDtoToDomain, targetMemberEntity);

        //then
        assertThat(targetMemberEntity.getTel()).isEqualTo(tel);
        assertThat(updateFlg).isEqualTo(true);
    }

    @Test
    @DisplayName("변경할 게 없는 경우 false 리턴")
    void test7() throws IllegalAccessException {
        MemberEntity targetMemberEntity = MemberEntity.builder()
                .tel("123456789")
                .build()
                ;

        MemberEntity sourceMemberEntity = MemberEntity.builder()
                .tel("123456789")
                .build()
                ;

        boolean updateFlg = ApiUtils.merge(sourceMemberEntity, targetMemberEntity);

        assertThat(updateFlg).isEqualTo(false);
    }
}