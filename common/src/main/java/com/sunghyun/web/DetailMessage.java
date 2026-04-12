package com.sunghyun.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DetailMessage {
    private String field;
    private String value;
    private String reason;
}
