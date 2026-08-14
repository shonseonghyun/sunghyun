package com.sunghyun.chat.application.service;

import com.sunghyun.chat.application.dto.req.ChatMessageSendReqDto;
import com.sunghyun.chat.application.dto.res.ChatMessageListResDto;
import com.sunghyun.chat.application.dto.res.ChatMessageResDto;
import com.sunghyun.chat.application.dto.res.ChatParticipantLastReadResDto;
import com.sunghyun.chat.application.dto.res.ChatReadResDto;
import com.sunghyun.chat.application.port.in.ChatMessageUseCase;
import com.sunghyun.chat.application.port.out.ChatEventPublisherPort;
import com.sunghyun.chat.application.port.out.dto.ChatEvent;
import com.sunghyun.chat.domain.exception.NotFoundChatRoomException;
import com.sunghyun.chat.domain.message.ChatMessage;
import com.sunghyun.chat.domain.message.repository.ChatMessageRepository;
import com.sunghyun.chat.domain.room.ChatRoom;
import com.sunghyun.chat.domain.room.repository.ChatRoomRepository;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService implements ChatMessageUseCase {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatEventPublisherPort chatEventPublisherPort;

    @Override
    public ChatMessageListResDto getChatMessages(Long chatRoomNo, Long memberNo, Long lastMessageNo, int pageSize) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .orElseThrow(() -> new NotFoundChatRoomException(ErrorCode.Z000));

        final Pageable pageable = PageRequest.of(0, pageSize);
        final Long firstViewableChatMessageNo = chatRoom.getFirstViewableChatMessageNoOfMember(memberNo);

        if(firstViewableChatMessageNo == 0L){
            // 0L이면 보여줄 게 없다.
            // 0L 의미=> 1. 생성만 된 채팅방이라 아직 채팅메시지가 없는 경우, 2. 나간 사람인 경우
            return null;
        }

        final List<ChatMessage> messages = chatMessageRepository.findMessagesByRoomNo(chatRoomNo,firstViewableChatMessageNo ,lastMessageNo, pageable);

        List<ChatMessageResDto> messageDtos = messages.stream()
                .map(message -> {
                    // 예시: chatRoom이나 별도 매퍼를 통해 해당 sender의 이름을 찾아옴
                    List<Long> receiverMembersNo = chatRoom.getReceiverMemberNos(message.getSenderMemberNo());

                    return ChatMessageResDto.fromDomain(message,receiverMembersNo);
                })
                .toList();

        List<ChatParticipantLastReadResDto> participants = chatRoom.getChatParticipants().stream()
                .map(ChatParticipantLastReadResDto::fromDomain)
                .toList();

        return new ChatMessageListResDto(messageDtos, participants);
    }

    @Override
    @Transactional
    public void createChatMessage(Long chatRoomNo,Long senderMemberNo, ChatMessageSendReqDto payload) {
//        if(payload.getContent().equals("에러1")){
//            throw new ChatException(ErrorCode.F000);
//        }
        // 채팅방 처음 메시지 입력 시,
        // 개인 채팅방은 메시지를 그대로 구독한 클라이언트에게 응답하면 되고,
        // 단체 채팅방은 "초대한 사람a님이 초대받은사람b,초대받은사람c을 초대했습니다." 클라이언트에게 글이 같이 응답되어야 합니다.
        // 채팅방 조회
        ChatRoom selectedChatRoom = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .orElseThrow(() -> new NotFoundChatRoomException(ErrorCode.Z000));

        boolean hasMessages = chatMessageRepository.existsByChatRoomNo(chatRoomNo);

        // 메시지 도메인 생성 후 영속화
        ChatMessage newChatMessage = ChatMessage.createChatMessage(chatRoomNo,senderMemberNo,payload.getContent(),payload.getMessageType());
        ChatMessage savedChatMessage = chatMessageRepository.save(newChatMessage);

        // 공통 처리
        selectedChatRoom.handleNewMessage(hasMessages,savedChatMessage.getChatMessageNo());
        chatRoomRepository.save(selectedChatRoom);

        ChatMessageResDto result = ChatMessageResDto.fromDomain(savedChatMessage, selectedChatRoom.getReceiverMemberNos(senderMemberNo));
        chatEventPublisherPort.publishChatMessageCreated(new ChatEvent.MessageCreated(chatRoomNo, result));
    }

    @Override
    @Transactional
    public ChatReadResDto readChatMessage(Long chatRoomNo, Long memberNo, Long lastReadChatMessageNo) {
        // 루트 애그리거트는 ChatRoom이고 그 안에 ChatParticipant 도메인이 있다. 따라서, 루트 애그리거트인 ChatRoom을 통해서만 업데이트되어야 한다.
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .orElseThrow(() -> new NotFoundChatRoomException(ErrorCode.Z000));

        chatRoom.readMessageOfMember(memberNo,lastReadChatMessageNo);
        chatRoomRepository.save(chatRoom);
        ChatReadResDto result = ChatReadResDto.builder()
                .chatRoomNo(chatRoomNo)
                .memberNo(memberNo)
                .lastReadChatMessageNo(lastReadChatMessageNo)
                .build();

        chatEventPublisherPort.publishReadMessageUpdated(new ChatEvent.MessageRead(chatRoomNo, result));

        return result;
    }

}
