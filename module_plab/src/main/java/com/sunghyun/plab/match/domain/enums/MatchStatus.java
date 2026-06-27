package com.sunghyun.plab.match.domain.enums;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.InvalidEnumCodeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MatchStatus {
    ACTIVE("O","매치 활성화"),
    INVALID("I","매치 에러"),
    CANCELED("X","매치 취소")
    ;

    private final String code;
    private final String desc;


    public static MatchStatus fromCode(String code) {
        return Arrays.stream(MatchStatus.values())
                .filter(matchStatus -> matchStatus.getCode().equals(code))
                .findFirst()
                .orElseThrow(()->new InvalidEnumCodeException(ErrorCode.C000));
    }
}
