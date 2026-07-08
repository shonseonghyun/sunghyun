package com.sunghyun.plab.subscription.domain.model;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;

public interface PlabMatchData {
    Long getPlabMatchNo();
    String getStadiumName();
    String getMatchDt();
    String getMatchTm();
    NotiSetting getPlayerCnt(); // 혹은 Integer
    Integer getMaxPlayerCnt(); // 혹은 Integer
    NotiSetting getSubType();
}
