package com.sunghyun.plab.match.adapter.out.persistence;

import com.sunghyun.plab.match.domain.enums.MatchStatus;
import jakarta.persistence.AttributeConverter;

public class MatchStatusConverter implements AttributeConverter<MatchStatus,String> {
    @Override
    public String convertToDatabaseColumn(MatchStatus matchStatus) {
        return matchStatus.getCode();
    }

    @Override
    public MatchStatus convertToEntityAttribute(String code) {
        return MatchStatus.fromCode(code);
    }
}
