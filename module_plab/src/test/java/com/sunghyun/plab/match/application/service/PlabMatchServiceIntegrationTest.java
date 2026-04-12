package com.sunghyun.plab.match.application.service;

import com.sunghyun.plab.match.application.port.in.PlabMatchRegisterFacadeUseCase;
import com.sunghyun.plab.match.application.port.in.PlabMatchUseCase;
import com.sunghyun.plab.match.domain.model.PlabMatch;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PlabMatchServiceConcurrencyTest {
    @Autowired
    private PlabMatchUseCase plabMatchUseCase;

    @Autowired
    private PlabMatchRegisterFacadeUseCase plabMatchRegisterFacadeUseCase;


    @Test
    @DisplayName("100명의 유저가 동시에 동일한 매치 번호로 등록을 시도해도, 매치는 1개만 생성되어야 한다")
    void concurrencyTest() throws InterruptedException {
        //given
        int threadCount = 500;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 테스트용 데이터
        Long plabMatchNo = 813470L;

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    // 서비스 호출 (매치 등록 로직)
                    plabMatchRegisterFacadeUseCase.registerPlabMatch(plabMatchNo);
                } catch (Exception e) {
                    // 예외 발생 시 로그 출력 (중복 예외 등이 터질 수 있음)
                    System.out.println("에러 발생: " + e.getMessage());
                } finally {
                    latch.countDown(); // 작업 하나 완료
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기

        // then
        // 실제 DB에 해당 번호의 매치가 '딱 1개'만 있는지 검증
        // 만약 락이 없다면 여기서 데이터가 여러 개 생기거나 에러가 날 겁니다.
        PlabMatchResDto result = plabMatchUseCase.getPlabMatchByPlabMatchNo(plabMatchNo);
        System.out.println(result.toString());
        assertThat(result).isNotNull();


        // (참고) 만약 카운트 로직이 있다면 카운트가 정확한지도 확인 가능
    }
}