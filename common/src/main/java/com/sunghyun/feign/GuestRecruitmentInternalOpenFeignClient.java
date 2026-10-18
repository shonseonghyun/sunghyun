package com.sunghyun.feign;

import com.sunghyun.feign.dto.InternalGuestRecruitmentDto;
import com.sunghyun.web.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "guestRecruitment-internal-api",
        url = "${guest.api.url}",
        configuration = OpenFeignConfig.class
)
public interface GuestRecruitmentInternalOpenFeignClient {

    @GetMapping("/{schedulePollNo}")
    GlobalResponse<InternalGuestRecruitmentDto> getGuestRecruitmentSimpleInfo(
            @PathVariable("schedulePollNo") Long schedulePollNo
    );
}