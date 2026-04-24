package com.sunghyun.plab.subscription.adapter.out.persistence;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import jakarta.persistence.AttributeConverter;

public class NotiSettingConverter implements AttributeConverter<NotiSetting,String> {
    @Override
    public String convertToDatabaseColumn(NotiSetting notiSetting) {
        return notiSetting.getCode();
    }

    @Override
    public NotiSetting convertToEntityAttribute(String code) {
        return NotiSetting.fromCode(code);
    }
}
