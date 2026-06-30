package com.sunghyun.member.adpater.in.web;

import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.application.service.MemberService;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.validation.Valid;
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

    @PostMapping("")
    public GlobalResponse<MemberResDto> registerMember(
            @Valid @RequestBody final MemberRegisterReqDto dto
    )
    {
        //들어온 인자 찎기
        final MemberResDto result = memberService.registerMember(dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/{memberNo}")
    public GlobalResponse<MemberResDto> getMember(@PathVariable final Long memberNo) {
        final MemberResDto result = memberService.getMemberByMemberNo(memberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PutMapping("/{memberNo}")
    public GlobalResponse<MemberResDto> modifyMember(
            @Valid @RequestBody final MemberModifyReqDto dto
    )
    {
        final MemberResDto result = memberService.modifyMember(dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @DeleteMapping("/{memberNo}")
    public GlobalResponse<MemberResDto> deleteMember(@PathVariable final Long memberNo) {
        memberService.deleteMember(memberNo);
        return GlobalResponse.of(ErrorCode.S000);
    }
}
