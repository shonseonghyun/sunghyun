package com.sunghyun.member.domain.model;

import com.sunghyun.config.authorize.Role;
import com.sunghyun.member.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Member {
    private Long memberNo;
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String tel;
    private String birthDt;
    private Gender gender;

    private List<Role> roles;

    public void setPwd(String pwd){
        this.pwd = pwd;
    }
}
