package com.sunghyun.batch.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchUpdateEvent implements Serializable {
    // 3. 직렬화 버전 ID (권장사항, 없어도 돌아는 가지만 나중에 클래스 구조 바뀔 때 에러 방지용)
    @Serial
    private static final long serialVersionUID = 1L;

    private Long plabMatchNo;
    private String notiType;
    private String notiValue;
}
