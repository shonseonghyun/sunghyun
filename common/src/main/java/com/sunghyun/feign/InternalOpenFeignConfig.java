package com.sunghyun.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class InternalOpenFeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignAuthInterceptor();
    }
}
