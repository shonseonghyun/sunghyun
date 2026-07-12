package com.sunghyun.member.adpater.in.web;

import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.application.port.usecase.MemberUseCase;
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
    private final MemberUseCase memberUseCase;

    @PostMapping("")
    public GlobalResponse<MemberResDto> registerMember(
            @Valid @RequestBody final MemberRegisterReqDto dto
    )
    {
        //들어온 인자 찎기
        final MemberResDto result = memberUseCase.registerMember(dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/{memberNo}")
    public GlobalResponse<MemberResDto> getMember(@PathVariable final Long memberNo) {
        final MemberResDto result = memberUseCase.getMemberByMemberNo(memberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PutMapping("/{memberNo}")
    public GlobalResponse<MemberResDto> modifyMember(@PathVariable final Long memberNo, @Valid @RequestBody final MemberModifyReqDto dto) {
        final MemberResDto result = memberUseCase.modifyMember(memberNo,dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @DeleteMapping("/{memberNo}")
    public GlobalResponse<MemberResDto> deleteMember(@PathVariable final Long memberNo) {
        memberUseCase.deleteMember(memberNo);
        return GlobalResponse.of(ErrorCode.S000);
    }
}
