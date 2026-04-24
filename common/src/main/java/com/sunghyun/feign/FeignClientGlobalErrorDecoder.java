package com.sunghyun.feign;

import com.sunghyun.web.exception.ExternalResourceNotFoundException;
import com.sunghyun.web.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientGlobalErrorDecoder implements ErrorDecoder {

    /**
     * OpenFeign에서 발생하는 Status Code를 기반으로 오류를 커스텀 처리로 수행합니다.
     *
     * @param methodKey
     * @param response
     * @return
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            // common에 정의된 기본 에러 코드(예: COMMON_BAD_REQUEST) 사용
            return new ExternalResourceNotFoundException(ErrorCode.COMMON_400);
        }
        return new ErrorDecoder.Default().decode(methodKey, response);
    }
}
