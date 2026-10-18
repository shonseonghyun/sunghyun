package com.sunghyun.schedule.domain;

import com.sunghyun.schedule.domain.enums.AttendanceStatus;
import com.sunghyun.schedule.domain.enums.MatchType;
import com.sunghyun.schedule.domain.enums.MinPlayerStatusChange;
import com.sunghyun.schedule.domain.exception.AlreadySameAttendanceStatusException;
import com.sunghyun.schedule.domain.exception.PlayerLimitExceededException;
import com.sunghyun.schedule.domain.exception.SchedulePollClosedException;
import com.sunghyun.web.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SchedulePoll {
    private Long schedulePollNo;

    //팀
    private Long teamNo;

    // 제목
    private String title;

    // 경기 정보
    private MatchInfo matchInfo;

    // 예상 비용
    private CostInfo costInfo;

    //투표 참석 여부
    private List<PollAttendance> attendances;

    // 인원 제한
    private PlayerLimit playerLimit;

    private String closedDt;
    private String closedTm;

    private boolean isClosed;

    /*
    * 용병(Guest)이 포함되는 순간, "이 투표/참석자는 팀원(TeamMember)일 수도 있고, 팀에 속하지 않은 외부 용병(Guest)일 수도 있다"는 새로운 요구사항이 생기게 됩니다.
    * 다형성(Polymorphism)을 통해 처리해보자.
    * */
    // 용병
//    private GuestRequirement guestRequirement;

    public static SchedulePoll create(
            Long teamNo,
            String title,
            MatchType matchType,
            String playDt,
            String playTm,
            Location location,
            Integer expectedAmount,
            Integer minPlayer,
            Integer maxPlayer,
            String closedDt,
            String closedTm,
            List<Long> teamMembersNo
    ) {
        MatchInfo matchInfo = MatchInfo.builder()
                .matchType(matchType)
                .playDt(playDt)
                .playTm(playTm)
                .location(location)
                .build();

        CostInfo costInfo = new CostInfo(expectedAmount != null ? expectedAmount : 0);

        PlayerLimit playerLimit = new PlayerLimit(minPlayer, maxPlayer);

        List<PollAttendance> pollAttendances = new ArrayList<>(
                teamMembersNo.stream()
                        .map(PollAttendance::create)
                        .toList()
        );

        return SchedulePoll.builder()
                .teamNo(teamNo)
                .title(title)
                .matchInfo(matchInfo)
                .costInfo(costInfo)
                .playerLimit(playerLimit)
                .closedDt(closedDt)
                .closedTm(closedTm)
                .isClosed(false)
                .attendances(pollAttendances)
                .build();
    }

    public AttendanceStatus getAttendanceStatusOf(Long myTeamMemberNo) {
        return attendances.stream()
                .filter(pollAttendance -> pollAttendance.getTeamMemberNo().equals(myTeamMemberNo))
                .findFirst()
                .map(PollAttendance::getAttendanceStatus)
                .orElse(AttendanceStatus.UNDECIDED)
                ;
    }

    public int getAttendanceCountByStatus(AttendanceStatus status) {
        if (attendances == null || status == null) {
            return 0;
        }

        return (int) attendances.stream()
                .filter(pollAttendance -> pollAttendance.getAttendanceStatus()== status)
                .count();
    }

    private boolean isFull(){
        // 제한없음인 경우는 넘어감
        if(isMaxUnlimited()){
            return false;
        }

        // 최대 인원수, 투표 전 참석자수
        Integer max = this.playerLimit.getMax();
        int currentAttendCount = this.getAttendanceCountByStatus(AttendanceStatus.ATTENDING);

        // 최대인원 찼는지 체크
        return currentAttendCount>=max;
    }

    private boolean isMaxUnlimited() {
        return this.playerLimit.getMax() == null;
    }

    //false -> 알림 보낼 거 없음
    //true -> 알림 보낼 거 있음
    /**
     * 팀원의 투표 상태를 변경하고, 최소 인원 충족/미달 상태 변화(알림 대상 여부)를 반환합니다.
     */
    public MinPlayerStatusChange vote(Long myTeamMemberNo, AttendanceStatus newStatus) {
        //첫 투표인 경우 -> 팀회원들은 첫투표가 팀일정 생성 시 강제되므로 반드시 pollAttendance가 존재한다.

        // 투표 가능 여부 사전 검증
        validateVotable(myTeamMemberNo,newStatus);

        int beforeAttendCount = getAttendanceCountByStatus(AttendanceStatus.ATTENDING);

        // 업데이트
        updateAttendanceStatus(myTeamMemberNo,newStatus);

        int afterAttendCount = getAttendanceCountByStatus(AttendanceStatus.ATTENDING);

        return hasMinPlayerThresholdChanged(beforeAttendCount,afterAttendCount);
    }

    private void validateVotable(Long myTeamMemberNo, AttendanceStatus newStatus){
        // 동일한 상태값을 투표한 경우
        if(this.getAttendanceStatusOf(myTeamMemberNo)==newStatus){
            throw new AlreadySameAttendanceStatusException(ErrorCode.P004);
        }

        // 마감 검증
        if(this.isClosed){
            throw new SchedulePollClosedException(ErrorCode.P002);
        }

        // 참석 누른 경우 최대인원 검증
        if(AttendanceStatus.ATTENDING == newStatus && isFull()){
            throw new PlayerLimitExceededException(ErrorCode.P003);
        }

    }

    private void updateAttendanceStatus(Long myTeamMemberNo,AttendanceStatus newStatus){
        // 투표 도메인 조회
        PollAttendance pollAttendance = attendances.stream()
                .filter(attendance -> attendance.getTeamMemberNo().equals(myTeamMemberNo))
                .findFirst()
                .orElse(null)
                ;

        // 구조 상 없을 수가 없음
        // 혹시라도 없는 경우 생성
        if(pollAttendance == null){
            PollAttendance newPollAttendance = PollAttendance.create(myTeamMemberNo);
            newPollAttendance.changeStatus(newStatus);
            this.attendances.add(newPollAttendance);
        }
        // 있는 경우
        else{
            // 상태 변경
            pollAttendance.changeStatus(newStatus);
        }
    }

    private MinPlayerStatusChange hasMinPlayerThresholdChanged(int beforeAttendCount,int afterAttendCount){
        Integer min = playerLimit.getMin();

        // 미달->충족
        if(beforeAttendCount<min && afterAttendCount>=min){
            return MinPlayerStatusChange.MET;
        }
        // 충족->미달
        else if (beforeAttendCount>=min && afterAttendCount < min ) {
            return MinPlayerStatusChange.UNMET;
        }
        return MinPlayerStatusChange.NO_CHANGE;
    }

    public boolean isMinPlayerMet() {
        return getAttendanceCountByStatus(AttendanceStatus.ATTENDING) >= playerLimit.getMin();
    }

    public void setLocation(Location location) {
        matchInfo.setLocation(location);
    }
}
