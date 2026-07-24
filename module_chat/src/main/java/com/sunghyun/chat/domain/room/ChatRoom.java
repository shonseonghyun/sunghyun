package com.sunghyun.chat.domain.room;

import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import com.sunghyun.chat.domain.exception.NotFoundChatParticipantException;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoom {
    private Long chatRoomNo;

    private ChatRoomType chatRoomType;

    private List<ChatParticipant> chatParticipants;

    private String createdDt;

    private String createdTm;

    public static ChatRoom createChatRoom(Long memberNo, Long friendMemberNo) {
        return ChatRoom.builder()
                .chatRoomType(ChatRoomType.PRIVATE)
                .chatParticipants(Arrays.asList(
                            ChatParticipant.createChatParticipant(memberNo),
                            ChatParticipant.createChatParticipant(friendMemberNo)
                        )
                )
                .createdDt(ApiUtils.getCurrentDt())
                .createdTm(ApiUtils.getCurrentTm())
                .build()
                ;
    }

    public void readMessageOfMember(Long memberNo, Long lastReadChatMessageNo) {
        ChatParticipant readedParticipant = this.getChatParticipants()
                .stream()
                .filter(chatParticipant -> chatParticipant.getMemberNo().equals(memberNo))
                .findFirst()
                .orElseThrow(()->new NotFoundChatParticipantException(ErrorCode.Z001));

        readedParticipant.readMessage(lastReadChatMessageNo);
    }

//    // 참여자들 중 사용자 제외하고 나머지 참여자 뽑기
//    public List<ChatParticipant> getParticipantsExcludeSender(Long senderMemberNo){
//        //보낸 사람이 해당방 실제 참여자인지 나간 참여자 아닌지 확인
//        boolean isSenderActive = this.chatParticipants.stream()
//                .anyMatch(p -> p.getMemberNo().equals(senderMemberNo) && !p.isLeft());
//
//        if (!isSenderActive) {
//            throw new NotFoundChatParticipantException(ErrorCode.Z001);
//        }
//
//        // 보낸 사람 제외 + 나가지 않은 상대방 추출
//        return this.getChatParticipants()
//                .stream()
//                .filter(chatParticipant -> !chatParticipant.isLeft()) // 안 나간 사람들에 한해서만
//                .filter(chatParticipant -> !chatParticipant.getMemberNo().equals(senderMemberNo)) // 보낸 이 제외하고 나머지 사람들
//                .toList();
//    }

    public List<Long> getReceiverMemberNos(Long senderMemberNo) {
        //보낸 사람이 해당 방의 실제 참여자인지 나간 참여자 아닌지 확인
        boolean isSenderActive = this.chatParticipants.stream()
                .anyMatch(p -> p.getMemberNo().equals(senderMemberNo) && !p.isLeft());

        if (!isSenderActive) {
            throw new NotFoundChatParticipantException(ErrorCode.Z001); // 💡 예: 참여자가 아니거나 퇴장한 경우
        }

        // 2. 보낸 이 제외 + 안 나간 사람들의 ID만 추출
        return this.chatParticipants.stream()
                .filter(p -> !p.isLeft())
                .filter(p -> !p.getMemberNo().equals(senderMemberNo))
                .map(ChatParticipant::getMemberNo)
                .toList();
    }

    public Integer getParticipantCount() {
        return chatParticipants.size();
    }
}