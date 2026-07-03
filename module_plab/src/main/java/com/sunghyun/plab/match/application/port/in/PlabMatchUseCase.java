package com.sunghyun.plab.match.application.port.in;

import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;

import java.util.List;

public interface PlabMatchUseCase {
    PlabMatchResDto registerPlabMatch(final Long plabMatchNo);
    PlabMatchResDto getPlabMatchByPlabMatchNo(final Long plabMatchNo);
    List<PlabMatchResDto> getPlabMatches(String startDt, String endDt);
    List<PlabMatchResDto> getPlabMatchesByDate(final String targetDt);
}
