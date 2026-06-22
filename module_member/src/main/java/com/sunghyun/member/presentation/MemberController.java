package com.sunghyun.member.presentation;

import com.sunghyun.dto.LoginReqDto;
import com.sunghyun.member.application.MemberService;
import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
import com.sunghyun.service.AuthService;
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
            @PathVariable("id")
            @NotBlank(message = "{common.notblank}")
            @Size(min = 6, message = "{member.id.size}")
            final String id
    )
    {
        //들어온 인자 찎기
        MemberValidIdResDto result = memberService.validMemberId(id);
        return GlobalResponse.of(ErrorCode.S00,result);
    }

    @PostMapping("/login")
    public GlobalResponse login(@RequestBody final LoginReqDto loginReqDto){
        authService.login(loginReqDto.getId(),loginReqDto.getPwd());
        return null;
    }


    @PostMapping("/register")
    public GlobalResponse<MemberResDto> registerMember(
            @Valid @RequestBody final MemberRegisterReqDto dto
    )
    {
        //들어온 인자 찎기
        final MemberResDto result = memberService.registerMember(dto);
        return GlobalResponse.of(ErrorCode.S00,result);
    }

    @GetMapping("/{memberNo}")
    public GlobalResponse<MemberResDto> getMember(
            @PathVariable("memberNo") final Long memberNo
    )
    {
        final MemberResDto result = memberService.getMemberByMemberNo(memberNo);
        return GlobalResponse.of(ErrorCode.S00,result);
    }

    @PutMapping("")
    public GlobalResponse<MemberResDto> modifyMember(
            @Valid @RequestBody final MemberModifyReqDto dto
    )
    {
        final MemberResDto result = memberService.modifyMember(dto);
        return GlobalResponse.of(ErrorCode.S00,result);
    }



}
