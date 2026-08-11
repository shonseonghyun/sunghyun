package com.sunghyun.chat.domain.message;

import com.sunghyun.chat.domain.message.enums.ChatMessageType;
import com.sunghyun.utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessage {
    private Long chatMessageNo;

    //둘 중 뭘로 할까? Member모듈은 별개 모듈이라 멤버 정보를 불러오기 위해선 통신을 통해 불러와야 한다.
    // memberNo가 낫다, 만약 ChatPartitcipant가 삭제된다고 가정하면, 그 땐 해당 메시지는 누가 발송했는지 주체를 알 수 없게 되므로
    private Long senderMemberNo;
//    private Long ChatParticipantNo;

    private Long chatRoomNo;

    private ChatMessageType messageType;

    private String content;

    private String sendDt;

    private String sendTm;

    // 일반 대화 메시지 생성 팩토리 메서드
    public static ChatMessage createChatMessage(Long chatRoomNo, Long senderMemberNo, String content, ChatMessageType messageType) {
        return ChatMessage.builder()
                .chatRoomNo(chatRoomNo)
                .senderMemberNo(senderMemberNo)
                .content(content)
                .messageType(messageType)
                .sendDt(ApiUtils.getCurrentDt())
                .sendTm(ApiUtils.getCurrentTm())
                .build();
    }

    // 시스템 퇴장 메시지 생성 팩토리 메서드 ("~님이 대화를 떠났습니다")
    public static ChatMessage createLeaveMessage(Long chatRoomNo, Long leavedMemberNo) {
        return ChatMessage.builder()
                .chatRoomNo(chatRoomNo)
                .senderMemberNo(leavedMemberNo)
                .content("회원 " + leavedMemberNo + "님이 대화를 떠났습니다.")
                .messageType(ChatMessageType.LEAVE)
                .sendDt(ApiUtils.getCurrentDt())
                .sendTm(ApiUtils.getCurrentTm())
                .build();
    }

    public static ChatMessage createInviteMessage(Long chatRoomNo, Long inviteMemberNo, List<Long> invitedMemberNos) {
        String invitedMembersStr = String.join(", ", invitedMemberNos.stream().map(String::valueOf).toList());

        return ChatMessage.builder()
                .chatRoomNo(chatRoomNo)
                .senderMemberNo(inviteMemberNo)
                .content("회원 " + inviteMemberNo + "님이 회원 " + invitedMembersStr + "님을 초대했습니다.")
                .messageType(ChatMessageType.INVITE)
                .sendDt(ApiUtils.getCurrentDt())
                .sendTm(ApiUtils.getCurrentTm())
                .build();
    }
}
