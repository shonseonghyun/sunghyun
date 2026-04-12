package com.sunghyun.plab.match.application.port.in;

import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;

public interface PlabMatchUseCase {
    PlabMatchResDto registerPlabMatch(final Long plabMatchNo);
    PlabMatchResDto getPlabMatchByPlabMatchNo(final Long plabMatchNo);
}
