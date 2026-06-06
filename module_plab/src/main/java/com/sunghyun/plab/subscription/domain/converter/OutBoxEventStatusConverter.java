package com.sunghyun.plab.subscription.domain.converter;

import com.sunghyun.plab.subscription.domain.enums.OutBoxEventStatus;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.InvalidEnumCodeException;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public class OutBoxEventStatusConverter implements AttributeConverter<OutBoxEventStatus,String> {
    @Override
    public String convertToDatabaseColumn(OutBoxEventStatus outBoxEventStatus) {
        return outBoxEventStatus.name();
    }

    @Override
    public OutBoxEventStatus convertToEntityAttribute(String value) {
        return Arrays.stream(OutBoxEventStatus.values())
                .filter(outBoxEventStatus -> outBoxEventStatus.name().equals(value))
                .findFirst()
                .orElseThrow(()->new InvalidEnumCodeException(ErrorCode.E00))
                ;
    }
}
