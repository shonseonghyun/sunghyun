package com.sunghyun.guest.application.dto;

import com.sunghyun.guest.domain.Position;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GuestApplyReqDto {
    @NotNull(message = "지원할 포지션은 필수입니다.")
    private Position position;
}
