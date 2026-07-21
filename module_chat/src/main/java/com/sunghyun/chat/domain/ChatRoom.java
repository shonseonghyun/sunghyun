package com.sunghyun.chat.domain;

import com.sunghyun.chat.domain.enums.ChatRoomType;
import com.sunghyun.utils.ApiUtils;
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
}