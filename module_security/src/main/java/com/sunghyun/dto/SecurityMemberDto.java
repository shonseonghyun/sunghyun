package com.sunghyun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SecurityMemberDto {
    private Long memberNo;
    private String id;
    private String pwd;
    private String name;
    private List<String> roles;
}
