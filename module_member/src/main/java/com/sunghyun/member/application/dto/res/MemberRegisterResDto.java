package com.sunghyun.member.application.dto.res;

import com.sunghyun.member.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class MemberRegisterResDto {
    private Long memberNo;

    private String id;

    private String pwd;

    private String email;

    private String name;

    private String tel;

    private String birthDt;

    private Gender gender;

//    private String photo;
}
