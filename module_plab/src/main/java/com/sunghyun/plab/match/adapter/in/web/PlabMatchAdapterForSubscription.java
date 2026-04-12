package com.sunghyun.plab.match.adapter.in.web;

import com.sunghyun.plab.match.application.port.in.PlabMatchRegisterFacadeUseCase;
import com.sunghyun.plab.match.application.port.in.PlabMatchUseCase;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import com.sunghyun.plab.subscription.application.port.out.external.PlabMatchOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlabMatchAdapterForSubscription implements PlabMatchOutPort {
    private final PlabMatchUseCase plabMatchUseCase;
    private final PlabMatchRegisterFacadeUseCase plabMatchRegisterFacadeUseCase;

    @Override
    public PlabMatchResDto registerPlabMatch(final Long plabMatchNo) {
        return plabMatchRegisterFacadeUseCase.registerPlabMatch(plabMatchNo);
    }

    @Override
    public PlabMatchResDto getPlabMatchByPlabMatchNo(final Long plabMatchNo) {
        return plabMatchUseCase.getPlabMatchByPlabMatchNo(plabMatchNo);
    }
}
