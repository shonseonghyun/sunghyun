package com.sunghyun.dto;

import com.sunghyun.config.authorize.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TokenReqDto {
    private String id;
    private List<Role> roles;
}
