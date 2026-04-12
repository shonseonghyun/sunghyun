package com.sunghyun.plab.match.application.service;

import com.sunghyun.feign.dto.PlabMatchResponseDto;
import com.sunghyun.plab.match.application.port.in.PlabMatchUseCase;
import com.sunghyun.plab.match.application.port.out.feign.PlabOpenFeignClient;
import com.sunghyun.plab.match.application.port.out.repository.PlabMatchRepository;
import com.sunghyun.plab.match.domain.model.PlabMatch;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PlabMatchService implements PlabMatchUseCase {
    private final PlabMatchRepository plabMatchRepository;
    private final PlabOpenFeignClient plabOpenFeignClient;

    @Transactional
    //concurrency 여러 유저가 동시에 동일한 매치를 등록하려고 할 때, 동시성은 어떻게 잡을 것 인가?
    public PlabMatchResDto registerPlabMatch(final Long plabMatchNo){
        return plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo)
                .map(PlabMatchResDto::from)
                .orElseGet(() -> {
                    //Plab Api 검증 처리
                    PlabMatchResponseDto result = plabOpenFeignClient.getMatch(plabMatchNo);

                    //PlabMatch 도메인 생성
                    PlabMatch plabMatch = PlabMatch.create(plabMatchNo,result);

                    //도메인 영속화
                    PlabMatch savedPlabMatch = plabMatchRepository.save(plabMatch);

                    //ResDto 변환
                    return PlabMatchResDto.from(savedPlabMatch);
                });
//        Optional<PlabMatch> selectedPlabMatch = plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo);
//        if(selectedPlabMatch.isEmpty()){
//            PlabMatch plabMatch = PlabMatch.create(plabOpenFeignClient,plabMatchNo);
//            return PlabMatchResDto.from(plabMatchRepository.save(plabMatch));
//        }
//        else{
//            return PlabMatchResDto.from(selectedPlabMatch.get());

//        plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo)
//                .orElseGet(()->{
//                    PlabMatch plabMatch = PlabMatch.create(plabOpenFeignClient,plabMatchNo);
//                    return plabMatchRepository.save(plabMatch);
//                });
    }

    @Transactional(readOnly = true)
    public PlabMatchResDto getPlabMatchByPlabMatchNo(final Long plabMatchNo) {
//        PlabMatch selectedPlabMatch = plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo)
//                .orElse(null);
//        return PlabMatchResDto.from(selectedPlabMatch);
        return plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo)
                .map(PlabMatchResDto::from)
                .orElse(null); // 혹은 필요시 예외 처리
    }
}
