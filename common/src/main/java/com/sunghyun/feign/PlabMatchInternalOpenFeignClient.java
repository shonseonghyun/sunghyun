package com.sunghyun.feign;

import com.sunghyun.web.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "plab-match-api",
        url = "${match.api.url:http://localhost/plab/match}",
        configuration = {OpenFeignConfig.class,InternalOpenFeignConfig.class}
)
public interface PlabMatchInternalOpenFeignClient {

    @PostMapping("/{plabMatchNo}")
    //이거 인터페이스타입 정해주어야 하는데
//    public GlobalResponse<PlabMatchResDto> registerPlabMatch(@PathVariable("plabMatchNo") final Long plabMatchNo);
    <T> GlobalResponse<T> registerPlabMatch(@PathVariable final Long plabMatchNo);
}
