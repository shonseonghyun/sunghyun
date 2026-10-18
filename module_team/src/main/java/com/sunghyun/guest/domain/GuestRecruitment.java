package com.sunghyun.guest.domain;

import com.sunghyun.guest.domain.exception.*;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GuestRecruitment {
    private Long recruitmentNo;

    private String title;

    private Long memberNo; // 작성자 정보

    private RecruitmentStatus status; // 모집 상태

    private Long schedulePollNo; // 모집 올린 스케줄 번호
    private List<PositionRequirement> requirements; // 용병 구인 구직
    private LevelLimit levelLimit; // 레벨 제한
    private MatchFormat matchFormat;
    private int guestFee;

    private String playDt;
    private String playTm;

    private int durationMinute;
    private int quarterDuration;
    private int restDuration;
    private int minGuaranteedQuarter; // 최소 보장 쿼터
    private boolean isBeverageProvided;

    private List<GuestApplyer> guestApplyers;

    private String additionalNotice;

    private String createdDt;
    private String createdTm;

    private String modifiedDt;
    private String modifiedTm;
    public static GuestRecruitment create(
            Long memberNo,
            Long schedulePollNo,
            String title,
            List<PositionRequirement> requirements,
            LevelLimit levelLimit,
            MatchFormat matchFormat,
            int guestFee,
            String playDt,
            String playTm,
            int durationMinute,
            int quarterDuration,
            int restDuration,
            int minGuaranteedQuarter,
            boolean isBeverageProvided,
            String additionalNotice
    ) {
        return GuestRecruitment.builder()
                .memberNo(memberNo)
                .schedulePollNo(schedulePollNo)
                .title(title)
                .status(RecruitmentStatus.RECRUITING)
                .requirements(requirements)
                .levelLimit(levelLimit)
                .matchFormat(matchFormat)
                .guestFee(guestFee)
                .playDt(playDt)
                .playTm(playTm)
                .durationMinute(durationMinute)
                .quarterDuration(quarterDuration)
                .restDuration(restDuration)
                .minGuaranteedQuarter(minGuaranteedQuarter)
                .isBeverageProvided(isBeverageProvided)
                .additionalNotice(additionalNotice)
                .createdDt(ApiUtils.getCurrentDt())
                .createdTm(ApiUtils.getCurrentTm())
                .build();
    }

    public int getConfirmedCountByPosition(Position position) {
        if (this.guestApplyers == null) {
            return 0;
        }

        return (int) this.guestApplyers.stream()
                .filter(guestApplyer -> guestApplyer.getPosition() == position)
                .filter(guestApplyer -> guestApplyer.getApplicationStatus() == ApplicationStatus.CONFIRMED)
                .count()
                ;
    }

    public void apply(Long applyedMemberNo, Position applyedPosition) {
        // 공고 진행 상태 검증
        validateIsRecruiting();

        // 본인이 쓴 용병글에 지원 거절
        validateSelfApplyed(applyedMemberNo);
        // 속해있는 팀엔 용병 지원 거절 -> 해당 도메인엔 팀에 대한 게 정보가 없으니 애플리케이션 계층에서 port를 통해 팀도메인가져와 검증해야할듯하다.

        validatePositionRequirements(applyedPosition);
        validateGuestApplyer(applyedMemberNo);

        // 성별/레벨 등 제한 -> 이건 일단 나중
        // 동일 시간대 체크

        addGuestApplyer(applyedMemberNo,applyedPosition);

        checkAndCloseRecruitment();
    }

    private void checkAndCloseRecruitment() {
        // 모두 정원 찼다면 예외
        boolean isAllPositionFull = this.requirements.stream()
                .allMatch(positionRequirement -> getConfirmedCountByPosition(positionRequirement.getPosition())>=positionRequirement.getRequiredCount())
                ;

        if(isAllPositionFull){
            this.status = RecruitmentStatus.COMPLETED;
        }
    }

    private void validateIsRecruiting(){
        if (this.getStatus() != RecruitmentStatus.RECRUITING) {
            throw new GuestRecruitmentClosedException(ErrorCode.G001);
        }
    }

    private void validateSelfApplyed(Long applyedMemberNo){
        if(this.getMemberNo().equals(applyedMemberNo)){
            throw new SelfApplicationNotAllowedException(ErrorCode.G002);
        }
    }

    private void addGuestApplyer(Long applyedMemberNo, Position applyedPosition) {
        this.guestApplyers.add(
                GuestApplyer.createConfirmed(applyedMemberNo,applyedPosition)
        );
    }

    private void validateGuestApplyer(Long applyedMemberNo) {
        if (guestApplyers == null || guestApplyers.isEmpty()) {
            return;
        }

        // 중복 지원 검증
        validateAlreadyApplyed(applyedMemberNo);
    }

    private void validateAlreadyApplyed(Long applyedMemberNo){
        //이미 신청한 사람인지 확인
        if(isApplied(applyedMemberNo)){
            throw new AlreadyConfirmedException(ErrorCode.G003);
        }
    }

    private void validatePositionRequirements(Position applyedPosition) {
        // 1.포지션 존재 여부 검증
        PositionRequirement selectedRequirement = this.requirements.stream()
                .filter(positionRequirement -> positionRequirement.getPosition() == applyedPosition)
                .findFirst()
                .orElseThrow(()-> new NotFoundPositionRequirementException(ErrorCode.G004));

        // 2.정원 초과 검증
        validateCapacity(selectedRequirement,applyedPosition);
    }

    private void validateCapacity(PositionRequirement selectedRequirement, Position applyedPosition){
        // 포지션에 해당하는 공고모집 필요 카운트
        int confirmedCount = getConfirmedCountByPosition(applyedPosition);

        // 인원 검증
        if (confirmedCount >= selectedRequirement.getRequiredCount()) {
            throw new PositionCapacityExceededException(ErrorCode.G005);
        }
    }

    public boolean isApplied(Long applyedMemberNo){
        if(applyedMemberNo == null){
            return false;
        }

        return this.guestApplyers.stream()
                .filter(guestApplyer->guestApplyer.getApplicationStatus() == ApplicationStatus.CONFIRMED)
                .anyMatch(guestApplyer ->guestApplyer.getMemberNo().equals(applyedMemberNo))
                ;
    }
}