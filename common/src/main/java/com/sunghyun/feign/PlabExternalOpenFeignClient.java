package com.sunghyun.feign;

import com.sunghyun.feign.dto.PlabMatchResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "plab-external-api",
        url = "${plab.api.url}",
        configuration = OpenFeignConfig.class
)
public interface PlabExternalOpenFeignClient {
    @GetMapping("/{plabMatchNo}")
    public PlabMatchResponseDto getMatch(@PathVariable("plabMatchNo") final Long plabMatchNo);
}
