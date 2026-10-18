package com.sunghyun.guest.adapter.in;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.guest.application.dto.*;
import com.sunghyun.guest.application.port.in.GuestRecruitmentUseCase;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GuestController {

    private final GuestRecruitmentUseCase guestRecruitmentUseCase;

    @PostMapping
    @RequestMapping("/api/v1/schedules/{schedulePollNo}/guest/recruitment")
    public GlobalResponse createGuestRecruitment(
            @PathVariable("schedulePollNo") Long schedulePollNo,
            @AuthMember AuthMemberInfo authMemberInfo,
            @Valid @RequestBody GuestRecruitmentCreateReqDto reqDto
    ) {
        Long memberNo = authMemberInfo.getMemberNo();
        GuestRecruitmentCreateResDto result = guestRecruitmentUseCase.createGuestRecruitment(memberNo, schedulePollNo, reqDto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/api/v1/recruit/guests/{recruitmentNo}")
    public GlobalResponse getGuestRecruitmentDetailInfo(
            @AuthMember AuthMemberInfo authMemberInfo,
            @PathVariable("recruitmentNo") Long recruitmentNo
    ) {
        Long memberNo = authMemberInfo == null ? null : authMemberInfo.getMemberNo();
        GuestRecruitmentDetailInfoSelectResDto result = guestRecruitmentUseCase.getGuestRecruitmentDetailInfo(recruitmentNo,memberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PostMapping("/api/v1/recruit/guests/{recruitmentNo}/apply")
    public GlobalResponse apply(
            @AuthMember AuthMemberInfo authMemberInfo,
            @RequestBody GuestApplyReqDto reqDto,
            @PathVariable("recruitmentNo") Long recruitmentNo
    ) {
        Long memberNo = authMemberInfo == null ? null : authMemberInfo.getMemberNo();
        guestRecruitmentUseCase.apply(memberNo,recruitmentNo,reqDto);
        return GlobalResponse.of(ErrorCode.S000);
    }

    @GetMapping("/api/v1/recruits/guests")
    public GlobalResponse getGuestRecruitments(
            @RequestParam(required = false) Long lastRecruitmentNo,
            @RequestParam(defaultValue = "5") int size
    ){
        List<GuestRecruitmentSimpleInfoSelectResDto> result = guestRecruitmentUseCase.getGuestRecruitments(lastRecruitmentNo,size);
        return GlobalResponse.of(ErrorCode.S000,result);
    }
}