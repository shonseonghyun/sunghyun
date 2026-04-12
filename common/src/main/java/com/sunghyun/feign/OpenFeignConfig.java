package com.sunghyun.feign;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@Configuration
public class OpenFeignConfig {
    /**
     * 공통 FeignClient Logging 정책 설정
     *
     * @return
     */
    @Bean
    Logger.Level customFeignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecode(){
        return new FeignClientGlobalErrorDecoder();
    }
}
