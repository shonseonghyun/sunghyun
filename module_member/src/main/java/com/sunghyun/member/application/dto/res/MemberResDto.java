package com.sunghyun.member.application.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResDto {
    private Long memberNo;

    private String id;

    private String pwd;

    private String email;

    private String name;

    private String tel;

    private String birthDt;

    private Gender gender;

//    private String photo;

    public static MemberResDto from(Member member){
        return MemberResDto.builder()
                .memberNo(member.getMemberNo())
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .tel(member.getTel())
                .birthDt(member.getBirthDt())
                .gender(member.getGender())
                .build();
    }
}
