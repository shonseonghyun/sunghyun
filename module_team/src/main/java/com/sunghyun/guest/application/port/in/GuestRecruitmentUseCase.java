package com.sunghyun.guest.application.port.in;


import com.sunghyun.feign.dto.InternalGuestRecruitmentDto;
import com.sunghyun.guest.application.dto.*;

import java.util.List;

public interface GuestRecruitmentUseCase {
    GuestRecruitmentCreateResDto createGuestRecruitment(Long memberNo, Long schedulePollNo, GuestRecruitmentCreateReqDto reqDto);
    GuestRecruitmentDetailInfoSelectResDto getGuestRecruitmentDetailInfo(Long recruitmentNo,Long memberNo);
    List<GuestRecruitmentSimpleInfoSelectResDto> getGuestRecruitments(Long lastRecruitmentNo, int size);
    void apply(Long memberNo, Long recruitmentNo, GuestApplyReqDto reqDto);
    InternalGuestRecruitmentDto getGuestRecruitmentForServer(Long recruitmentNo);
}
