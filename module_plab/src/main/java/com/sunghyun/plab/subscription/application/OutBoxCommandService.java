package com.sunghyun.plab.subscription.application;

import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionOutBoxRepository;
import com.sunghyun.plab.subscription.domain.exception.NotExistMatchSubscriptionOutBoxException;
import com.sunghyun.plab.subscription.domain.model.MatchSubscriptionOutBox;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutBoxCommandService {
    private final MatchSubscriptionOutBoxRepository outBoxRepository;
    
    @Transactional //흠 .. 이걸 안 써도 될라나? 이거써도 어차피 호출 메소드의 트랜잭션에 묻어가지 않을까? 그걸 어떻게 하면 알 수 있을까??
    public void save(final String outBoxNo,final String topic,final String jsonPayLoad) {
        log.info("outbox save");
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("현재 활성화된 트랜잭션 이름: [{}]", currentTransactionName);

        MatchSubscriptionOutBox outBox = MatchSubscriptionOutBox.create(
                outBoxNo,
                topic,
                jsonPayLoad
        );

        outBoxRepository.save(outBox);
//        throw new RuntimeException("Sdsd");
    }
    

    @Transactional
    public void updateStatus(final String outBoxNo,final boolean isPublished) {
        log.info("updateStatus");
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("현재 활성화된 트랜잭션 이름: [{}]", currentTransactionName);

        MatchSubscriptionOutBox outBox = outBoxRepository.findById(outBoxNo)
                .orElseThrow(() -> new NotExistMatchSubscriptionOutBoxException(ErrorCode.O00));

        if (isPublished) {
            outBox.markSent();
        } else {
            outBox.markFailed();
        }

        outBoxRepository.save(outBox);
    }
}
