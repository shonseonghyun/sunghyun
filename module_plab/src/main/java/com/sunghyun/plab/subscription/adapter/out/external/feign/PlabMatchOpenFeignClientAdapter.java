package com.sunghyun.plab.subscription.adapter.out.external.feign;

import com.sunghyun.feign.PlabMatchInternalOpenFeignClient;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import com.sunghyun.plab.subscription.application.port.out.external.PlabMatchOutPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

//@Component
@RequiredArgsConstructor
public class PlabMatchOpenFeignClientAdapter implements PlabMatchOutPort {
    private final PlabMatchInternalOpenFeignClient plabMatchInternalOpenFeignClient;
    @Override
    public PlabMatchResDto registerPlabMatch(final Long plabMatchNo) {
        //null체크 안해도 될라나?
        PlabMatchResDto result = plabMatchInternalOpenFeignClient.<PlabMatchResDto>registerPlabMatch(plabMatchNo).getData();
        return result;

        // 1. 응답 전체를 Optional로 감싸서 null 방지
//        return Optional.ofNullable(plabMatchInternalOpenFeignClient.registerPlabMatch(plabMatchNo))
//                // 2. 응답은 왔으나 데이터(getData)가 null인 경우 체크
//                .map(GlobalResponse::getData)
//                // 3. 데이터가 없으면 우리가 정의한 커스텀 예외 던지기
//                .orElseThrow(() -> new RuntimeException("플랩 매치 정보를 가져오는 데 실패했습니다. 매치 번호: " + plabMatchNo));
    }

    @Override
    public PlabMatchResDto getPlabMatch(Long plabMatchNo) {
        return null;
    }

    @Override
    public List<PlabMatchResDto> getPlabMatches(String startDt, String endDt) {
        return List.of();
    }

    @Override
    public List<PlabMatchResDto> getPlabMatchesByDate(String targetDate) {
        return List.of();
    }
}
