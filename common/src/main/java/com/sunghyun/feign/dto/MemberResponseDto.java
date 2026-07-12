package com.sunghyun.feign.dto;

import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long memberNo;
    private String id;
    private String name;
    private String email;
    private String tel;
}
