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
}