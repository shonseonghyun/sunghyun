package com.sunghyun.member.adpater.in.web;

import com.sunghyun.member.application.service.MemberService;
import com.sunghyun.member.application.service.AuthService;
import com.sunghyun.member.application.dto.req.*;
import com.sunghyun.member.application.dto.res.MemberLoginResDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
import com.sunghyun.member.application.dto.res.TokenReissueResDto;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.validation.Valid;
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
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/valid-id/{id}")
    public GlobalResponse<MemberValidIdResDto> validMemberId(
            @PathVariable
            @NotBlank(message = "{common.notblank}") @Size(min = 6, message = "{member.id.size}")
            final String id
    )
    {
        MemberValidIdResDto result = authService.validMemberId(id);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PostMapping("/login")
    public GlobalResponse login(@RequestBody final MemberLoginReqDto memberLoginReqDto){
        MemberLoginResDto result = authService.login(memberLoginReqDto.getId(), memberLoginReqDto.getPwd());
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/reissue")
    public GlobalResponse reissueAccessToken(@RequestBody final TokenReissueReqDto tokenReissueReqDto){
        TokenReissueResDto result = authService.reissueAccessToken(tokenReissueReqDto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }


    @PostMapping("/register")
    public GlobalResponse<MemberResDto> registerMember(
            @Valid @RequestBody final MemberRegisterReqDto dto
    )
    {
        //들어온 인자 찎기
        final MemberResDto result = memberService.registerMember(dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/{memberNo}")
    public GlobalResponse<MemberResDto> getMember(
            @PathVariable final Long memberNo
    )
    {
        final MemberResDto result = memberService.getMemberByMemberNo(memberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PutMapping("")
    public GlobalResponse<MemberResDto> modifyMember(
            @Valid @RequestBody final MemberModifyReqDto dto
    )
    {
        final MemberResDto result = memberService.modifyMember(dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }
}
