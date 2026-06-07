package com.sunghyun.plab.subscription.adapter.out.persistence.entity;

import com.sunghyun.plab.subscription.adapter.out.persistence.converter.OutBoxEventStatusConverter;
import com.sunghyun.plab.subscription.domain.enums.OutBoxEventStatus;
import com.sunghyun.plab.subscription.domain.model.MatchSubscriptionOutBox;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "subscription_out_box")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MatchSubscriptionOutBoxEntity {
    //구분값 pk(uuid?)
    @Id
    @Column(length = 36)
    private String outBoxNo;

    //토픽
    @Column
    private String topic;

    //paylod
    @Column
    private String payLoad;

    //생성 일자
    @Column
    private String createdDt;

    //생성 시간
    @Column
    private String createdTm;

    //처리 일자
    @Column
    private String processedDt;

    //처리 시간
    @Column
    private String processedTm;

    @Column
    @Convert(converter = OutBoxEventStatusConverter.class)
    private OutBoxEventStatus status;

    //이건 뭐지?
    @Column
    private int retryCnt;

    public static MatchSubscriptionOutBoxEntity from(MatchSubscriptionOutBox domain) {
        return MatchSubscriptionOutBoxEntity.builder()
                .outBoxNo(domain.getOutBoxNo())
                .topic(domain.getTopic())
                .payLoad(domain.getPayLoad())
                .createdDt(domain.getCreatedDt())
                .createdTm(domain.getCreatedTm())
                .processedDt(domain.getProcessedDt())
                .processedTm(domain.getProcessedTm())
                .status(domain.getStatus())
                .retryCnt(domain.getRetryCnt())
                .build();
    }

    public MatchSubscriptionOutBox toDomain() {
        return MatchSubscriptionOutBox.builder()
                .outBoxNo(this.outBoxNo)
                .topic(this.topic)
                .payLoad(this.payLoad)
                .createdDt(this.createdDt)
                .createdTm(this.createdTm)
                .processedDt(this.processedDt)
                .processedTm(this.processedTm)
                .status(this.status)
                .retryCnt(this.retryCnt)
                .build();
    }
}
