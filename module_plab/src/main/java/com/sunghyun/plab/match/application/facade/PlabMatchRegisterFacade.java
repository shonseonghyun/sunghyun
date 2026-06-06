package com.sunghyun.plab.match.application.facade;

import com.sunghyun.plab.match.application.port.in.PlabMatchRegisterFacadeUseCase;
import com.sunghyun.plab.match.application.port.in.PlabMatchUseCase;
import com.sunghyun.plab.match.application.port.out.repository.LockRepository;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlabMatchRegisterFacade implements PlabMatchRegisterFacadeUseCase {
    // 같은 계층이더라도 구현체(PlabMatchService)가 아닌 인터페이스(UseCase)를 주입받음
    private final PlabMatchUseCase plabMatchUseCase;
    private final LockRepository lockRepository;

    public PlabMatchResDto registerPlabMatch(final Long plabMatchNo){
        //스핀락
        //lock 잡기
        //무한정 대기 보단 횟수 제한 둬도 괜찮아 보인다.
        while(!lockRepository.getLock(plabMatchNo)){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            // 2. 실제 트랜잭션이 걸린 비즈니스 로직 호출
            // 반드시 해당 메소드가 내부적으로 commit된 이후 finally절의 락 해제가 이루어져야 한다.
            return plabMatchUseCase.registerPlabMatch(plabMatchNo);
        } finally {
            // 3. 어떤 예외가 발생하더라도 락은 반드시 해제
            lockRepository.unlock(plabMatchNo);
        }
    }
}
