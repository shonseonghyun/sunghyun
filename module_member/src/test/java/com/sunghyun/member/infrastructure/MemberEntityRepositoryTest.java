package com.sunghyun.member.infrastructure;

import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.adpater.out.persistence.entity.MemberEntity;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.member.adpater.out.persistence.repository.MemberRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MemberRepositoryImpl.class)
class MemberEntityRepositoryTest {
    // 상수는 대문자로 변경
    private static final String ID = "newId";
    private static final String PWD = "7895";
    private static final String EMAIL = "sunghyun7895@naver.com";
    private static final String NAME = "손성현";
    private static final String TEL = "01024168946";
    private static final String BIRTH_DT = "950204";
    private static final Gender GENDER = Gender.MAN;
    private static final Gender CHANGED_GENDER = Gender.WOMAN;
    private static final Long WRONG_MEMBER_NO = 0L;

    private Long memberNo;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void memberRepository는_Null_아님(){
        //then
        assertThat(memberRepository).isNotNull();
    }

    @BeforeEach
    void saveMember(){
        MemberEntity memberEntity = MemberEntity.builder()
                .id(ID)
                .pwd(PWD)
                .email(EMAIL)
                .name(NAME)
                .tel(TEL)
                .birthDt(BIRTH_DT)
                .gender(GENDER)
                .build();

        MemberEntity savedMemberEntity = memberRepository.save(memberEntity);
        this.memberNo = savedMemberEntity.getMemberNo();
    }

    @Test
    void getMemberByMemberByIdNo(){
        //when
        MemberEntity memberEntity = memberRepository.getMemberByMemberNo(memberNo);

        //then
        assertThat(memberEntity).isNotNull();
        assertThat(memberEntity.getName()).isEqualTo(NAME);
    }

    @Test
    void getMemberByWrongMemberByIdNo(){
        //when
        MemberEntity memberEntity = memberRepository.getMemberByMemberNo(WRONG_MEMBER_NO);

        //then
        assertThat(memberEntity).isNull();
    }

    @Test
    void updateMember(){
        //given
        MemberEntity memberEntity = memberRepository.getMemberByMemberNo(memberNo);

        //when
        memberEntity.setGender(CHANGED_GENDER);
        memberRepository.save(memberEntity);

        //then
        MemberEntity updatedMemberEntity = memberRepository.getMemberByMemberNo(memberNo);
        assertThat(updatedMemberEntity.getName()).isEqualTo(NAME);
        assertThat(updatedMemberEntity.getGender()).isEqualTo(CHANGED_GENDER);
    }

    @Test
    void delMember(){
        //when
        memberRepository.deleteMember(memberNo);

        //then
        MemberEntity memberEntity = memberRepository.getMemberByMemberNo(memberNo);
        assertThat(memberEntity).isNull();
    }

    @Test
    void isExistMemberByMemberId(){
        //when
        boolean isExistInDbFlg1 = memberRepository.isExistMemberById(ID);
        boolean isExistInDbFlg2 = memberRepository.isExistMemberById(ID + "1"); // 숫자 1을 문자열로 결합

        //then
        assertThat(isExistInDbFlg1).isTrue();
        assertThat(isExistInDbFlg2).isFalse();
    }
}