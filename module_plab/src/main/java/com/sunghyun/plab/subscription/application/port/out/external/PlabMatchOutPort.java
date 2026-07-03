package com.sunghyun.plab.subscription.application.port.out.external;

import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;

import java.util.List;

public interface PlabMatchOutPort {
    PlabMatchResDto registerPlabMatch(final Long plabMatchNo);
    PlabMatchResDto getPlabMatch(final Long plabMatchNo);
    List<PlabMatchResDto> getPlabMatches(final String startDt, final String endDt);

    List<PlabMatchResDto> getPlabMatchesByDate(String targetDate);
}
