package com.sunghyun.plab.subscription.application.port.out.external;

import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;

public interface PlabMatchOutPort {
    PlabMatchResDto registerPlabMatch(final Long plabMatchNo);
    PlabMatchResDto getPlabMatchByPlabMatchNo(final Long plabMatchNo);
}
