package com.sunghyun.plab.match.adapter.in.web;

import com.sunghyun.plab.match.application.port.in.PlabMatchRegisterFacadeUseCase;
import com.sunghyun.plab.match.application.port.in.PlabMatchUseCase;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/plab/match")
@RequiredArgsConstructor
public class PlabMatchController {
    private final PlabMatchRegisterFacadeUseCase plabMatchRegisterFacadeUseCase;
    private final PlabMatchUseCase plabMatchUseCase;

    //관리자만 써야한다.
    //restapi식이 맞니?
    @PostMapping("/{plabMatchNo}")
    public GlobalResponse registerPlabMatch(@PathVariable("plabMatchNo") final Long plabMatchNo){
        plabMatchRegisterFacadeUseCase.registerPlabMatch(plabMatchNo);
        return GlobalResponse.of(ErrorCode.S000);
    }
}
