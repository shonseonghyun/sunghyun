package com.sunghyun.plab.match.adapter.out.feign;

import com.sunghyun.web.exception.ExternalResourceNotFoundException;
import com.sunghyun.feign.PlabExternalOpenFeignClient;
import com.sunghyun.feign.dto.PlabMatchResponseDto;
import com.sunghyun.plab.match.application.port.out.feign.PlabOpenFeignClient;
import com.sunghyun.plab.match.domain.exception.InvalidPlabMatchException;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlabOpenFeignClientAdapter implements PlabOpenFeignClient {
    private final PlabExternalOpenFeignClient feignClient;

    @Override
    public PlabMatchResponseDto getMatch(final Long plabMatchNo) {
        PlabMatchResponseDto result ;
        try{
            result = feignClient.getMatch(plabMatchNo);
        }catch (ExternalResourceNotFoundException e){
            throw new InvalidPlabMatchException(ErrorCode.P02);
        }
        return result;
    }
}
