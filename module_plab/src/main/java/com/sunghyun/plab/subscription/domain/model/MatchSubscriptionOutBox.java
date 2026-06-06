package com.sunghyun.plab.subscription.domain.model;

import com.sunghyun.plab.subscription.domain.enums.OutBoxEventStatus;
import com.sunghyun.utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchSubscriptionOutBox {
    private String outBoxNo;

    //토픽
    private String topic;

    //payload
    private String payLoad;

    //생성 일자
    private String createdDt;

    //생성 시간
    private String createdTm;

    //처리 일자
    private String processedDt;

    //처리 시간
    private String processedTm;

    private OutBoxEventStatus status;

    //이건 뭐지?
    private int retryCnt;

    public static MatchSubscriptionOutBox create(final String outBoxNo,final String topic, final String payLoad){
        return MatchSubscriptionOutBox
                .builder()
                .outBoxNo(outBoxNo)
                .topic(topic)
                .payLoad(payLoad)
                .createdDt(ApiUtils.getCurrentDt())
                .createdTm(ApiUtils.getCurrentTm())
                .status(OutBoxEventStatus.PENDING)
                .retryCnt(0)
                .build()
                ;
    }

    public void markSent(){
        this.processedDt = ApiUtils.getCurrentDt();
        this.processedTm = ApiUtils.getCurrentTm();
        this.status = OutBoxEventStatus.SENT;
    }

    public void markFailed() {
        this.processedDt = ApiUtils.getCurrentDt();
        this.processedTm = ApiUtils.getCurrentTm();
        this.status = OutBoxEventStatus.FAILED;
    }
}
