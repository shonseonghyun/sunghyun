package com.sunghyun.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlabMatchResponseDto {

    private Long id;                          // 매치 ID (id)

//        @JsonProperty("apply_status")
//        private String applyStatus;               // 매치 상태 (apply_status: "hurry")

    private String schedule;                  // 매치 시간 (schedule: "2026-03-30T20:00:00+09:00")

    @JsonProperty("area_name")
    private String areaName;                  // 지역명 (area_name: "수원시")

    @JsonProperty("label_stadium")
    private String labelStadium;               // 구장명 (label_stadium: "수원 HK 풋살파크 2구장")

    @JsonProperty("stadium_group_id")
    private Integer StadiumGroupId;            //구장번호(stadium_group_id:"43)

//        @JsonProperty("fee")
//        private Integer fee;                      // 참가비 (fee: 11000)

    @JsonProperty("is_super_sub")
    private boolean isSuperSub;               // 슈퍼서브 여부 (is_super_sub)

    @JsonProperty("is_manager_free")
    private boolean isManagerFree;            // 매니저 프리 여부 (is_manager_free)

    @JsonProperty("confirm_cnt")
    private Integer confirmCnt;               // 매치 실제 신청 인원 (confirm_cnt

    @JsonProperty("waiting_cnt")
    private Integer waitingCnt;             //신청 대기 인원(waiting_cnt)

    @JsonProperty("total_apply_cnt")
    private Integer totalApplyCnt;          // 총 신청 인원(total_apply_cnt) == waiting_cnt + confirm_cnt

//        @JsonProperty("player_cnt")
//        private Integer playerCnt;                // 잔여 인원 (player_cnt)

    @JsonProperty("max_player_cnt")
    private Integer maxPlayerCnt;             // 최대 인원 (max_player_cnt)

//        @JsonProperty("min_player_cnt")
//        private Integer minPlayerCnt;             // 최소 성원 인원 (min_player_cnt)

//        @JsonProperty("inout_door")
//        private String inoutDoor;                 // 실내외 (inout_door: "OUTDOOR")

//        @JsonProperty("parking_fee")
//        private String parkingFee;                // 주차비 (parking_fee: "무료")

    // --- 불리언 필드들 ---
//        @JsonProperty("is_parking")
//        private Boolean isParking;

//        @JsonProperty("is_shower")
//        private Boolean isShower;

//        @JsonProperty("is_shoes")
//        private Boolean isShoes;

//        @JsonProperty("is_toilet")
//        private Boolean isToilet;

    // --- 리스트 데이터 ---
    // @JsonProperty("applys")
    // private List<PlabApplyDto> applys;      // 신청자 목록 (필요 시 PlabApplyDto 클래스 생성 후 주석 해제)
}
