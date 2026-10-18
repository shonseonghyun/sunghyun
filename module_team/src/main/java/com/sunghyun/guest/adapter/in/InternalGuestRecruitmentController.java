package com.sunghyun.guest.adapter.in;

import com.sunghyun.feign.dto.InternalGuestRecruitmentDto;
import com.sunghyun.guest.application.port.in.GuestRecruitmentUseCase;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/recruits/guests")
public class InternalGuestRecruitmentController {

    private final GuestRecruitmentUseCase guestRecruitmentUseCase;

    /**
     * 타 서버 연동용: 스케줄 내 등록한 용병 모집 정보 조회
     */
    @GetMapping("/{schedulePollNo}")
    public GlobalResponse<InternalGuestRecruitmentDto> getGuestRecruitmentForServer(
            @PathVariable Long schedulePollNo
    ) {
        InternalGuestRecruitmentDto result = guestRecruitmentUseCase.getGuestRecruitmentForServer(schedulePollNo);
        return GlobalResponse.of(ErrorCode.S000, result);
    }
}