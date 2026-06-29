package com.sunghyun.member.infrastructure;

import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.member.infrastructure.repository.MemberRepositoryImpl;
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
class MemberRepositoryTest {
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
        Member member = Member.builder()
                .id(ID)
                .pwd(PWD)
                .email(EMAIL)
                .name(NAME)
                .tel(TEL)
                .birthDt(BIRTH_DT)
                .gender(GENDER)
                .build();

        Member savedMember = memberRepository.save(member);
        this.memberNo = savedMember.getMemberNo();
    }

    @Test
    void getMemberByMemberByIdNo(){
        //when
        Member member = memberRepository.getMemberByMemberNo(memberNo);

        //then
        assertThat(member).isNotNull();
        assertThat(member.getName()).isEqualTo(NAME);
    }

    @Test
    void getMemberByWrongMemberByIdNo(){
        //when
        Member member = memberRepository.getMemberByMemberNo(WRONG_MEMBER_NO);

        //then
        assertThat(member).isNull();
    }

    @Test
    void updateMember(){
        //given
        Member member = memberRepository.getMemberByMemberNo(memberNo);

        //when
        member.setGender(CHANGED_GENDER);
        memberRepository.save(member);

        //then
        Member updatedMember = memberRepository.getMemberByMemberNo(memberNo);
        assertThat(updatedMember.getName()).isEqualTo(NAME);
        assertThat(updatedMember.getGender()).isEqualTo(CHANGED_GENDER);
    }

    @Test
    void delMember(){
        //when
        memberRepository.delMember(memberNo);

        //then
        Member member = memberRepository.getMemberByMemberNo(memberNo);
        assertThat(member).isNull();
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