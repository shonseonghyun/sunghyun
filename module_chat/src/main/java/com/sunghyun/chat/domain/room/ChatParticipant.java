package com.sunghyun.chat.domain.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatParticipant {
    private Long chatParticipantNo;

    private Long memberNo;

    private Long firstViewableChatMessageNo;

    private Long lastReadChatMessageNo;

    private boolean isDisplayed;

    private boolean isLeft;

    public static ChatParticipant createChatParticipant(Long memberNo){
        return ChatParticipant.builder()
                .memberNo(memberNo)
                .isLeft(false)
                .isDisplayed(false) //생성 직후 숨김처리
                .lastReadChatMessageNo(0L)
                .firstViewableChatMessageNo(0L)
                .build();
    }

    public static ChatParticipant createInvitedParticipant(Long memberNo, Long chatMessageNo){
        return ChatParticipant.builder()
                .memberNo(memberNo)
                .isLeft(false)
                .isDisplayed(false)
                .lastReadChatMessageNo(chatMessageNo)
                .firstViewableChatMessageNo(chatMessageNo) //초대된 시점부터 볼 수 있음
                .build();
    }
    public void readMessage(Long lastReadChatMessageNo) {
        if (lastReadChatMessageNo == null) return;

        if (this.lastReadChatMessageNo == null || lastReadChatMessageNo > this.lastReadChatMessageNo) {
            this.lastReadChatMessageNo = lastReadChatMessageNo;
        }
    }

    public void leave() {
        this.isLeft = true;
        this.isDisplayed = false;
        this.firstViewableChatMessageNo=0L;
    }

    public void enter() {
        this.isLeft = false;
    }

    public void enter(Long chatMessageNo) {
        this.isLeft = false;
        this.firstViewableChatMessageNo = chatMessageNo;
    }

    public void reEnter(Long chatMessageNo) {
        enter(chatMessageNo);
    }

    public void reEnter() {
        enter();
    }

    public void assignFirstViewableMessage(Long chatMessageNo) {
        // 최초에 0L 이거나 아직 세팅되지 않은 경우에만 첫 메시지 번호로 확정
        if (this.firstViewableChatMessageNo == null || this.firstViewableChatMessageNo == 0L) {
            this.firstViewableChatMessageNo = chatMessageNo;

            // 첫 메시지가 보이기 시작할 때 안 읽은 상태 기준도 같이 맞춰줄 수 있습니다.
//            if (this.lastReadChatMessageNo == null || this.lastReadChatMessageNo == 0L) {
//                this.lastReadChatMessageNo = chatMessageNo;
//            }
        }
    }

    public void displayed() {
        this.isDisplayed = true;
    }
}