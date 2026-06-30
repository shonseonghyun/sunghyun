package com.sunghyun.member.adpater.in.web;

import com.sunghyun.member.application.dto.req.MemberLoginReqDto;
import com.sunghyun.member.application.dto.req.TokenReissueReqDto;
import com.sunghyun.member.application.dto.res.MemberLoginResDto;
import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
import com.sunghyun.member.application.dto.res.TokenReissueResDto;
import com.sunghyun.member.application.service.AuthService;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/valid-id/{id}")
    public GlobalResponse<MemberValidIdResDto> validMemberId(
            @PathVariable
            @NotBlank(message = "{common.notblank}") @Size(min = 6, message = "{member.id.size}")
            final String id
    )
    {
        MemberValidIdResDto result = authService.validMemberId(id);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PostMapping("/auth")
    public GlobalResponse login(@RequestBody final MemberLoginReqDto memberLoginReqDto){
        MemberLoginResDto result = authService.login(memberLoginReqDto.getId(), memberLoginReqDto.getPwd());
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PostMapping("/reissue")
    public GlobalResponse reissueAccessToken(@RequestBody final TokenReissueReqDto tokenReissueReqDto){
        TokenReissueResDto result = authService.reissueAccessToken(tokenReissueReqDto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }
}
