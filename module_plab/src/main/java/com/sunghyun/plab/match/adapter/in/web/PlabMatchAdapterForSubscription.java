package com.sunghyun.plab.match.adapter.in.web;

import com.sunghyun.plab.match.application.port.in.PlabMatchRegisterFacadeUseCase;
import com.sunghyun.plab.match.application.port.in.PlabMatchUseCase;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import com.sunghyun.plab.subscription.application.port.out.external.PlabMatchOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public PlabMatchResDto getPlabMatch(final Long plabMatchNo) {
        return plabMatchUseCase.getPlabMatchByPlabMatchNo(plabMatchNo);
    }

    @Override
    public List<PlabMatchResDto> getPlabMatches(String startDt, String endDt) {
        return plabMatchUseCase.getPlabMatches(startDt,endDt);
    }
}
