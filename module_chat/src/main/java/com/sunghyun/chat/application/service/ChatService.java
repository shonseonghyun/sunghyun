package com.sunghyun.chat.application.service;

import com.sunghyun.chat.application.dto.req.ChatMessageSendReqDto;
import com.sunghyun.chat.application.dto.res.*;
import com.sunghyun.chat.application.port.in.ChatUseCase;
import com.sunghyun.chat.domain.message.repository.ChatMessageRepository;
import com.sunghyun.chat.domain.room.repository.ChatRoomRepository;
import com.sunghyun.chat.domain.message.ChatMessage;
import com.sunghyun.chat.domain.room.ChatParticipant;
import com.sunghyun.chat.domain.room.ChatRoom;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import com.sunghyun.chat.domain.exception.NotFoundChatRoomException;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService implements ChatUseCase {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatRoomCreateResDto getOrCreateChatRoom(Long memberNo, Long friendMemberNo) {
        return chatRoomRepository.findChatRoomByMemberNosAndChatRoomType(memberNo, friendMemberNo, ChatRoomType.PRIVATE)
                .map(existingChatRoom -> new ChatRoomCreateResDto(existingChatRoom.getChatRoomNo()))
                .orElseGet(() -> {
                    // 기존 방이 없을 때만 새 방 생성 및 저장
                    ChatRoom newChatRoom = ChatRoom.createChatRoom(memberNo, friendMemberNo);
                    ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);
                    return new ChatRoomCreateResDto(savedChatRoom.getChatRoomNo());
                });
    }

    @Override
    public ChatMessageListResDto getChatMessages(Long chatRoomNo, Long lastMessageNo, int pageSize) {
        // 페이징의 offset은 항상 0으로 고정하고, 한 번에 가져올 사이즈(LIMIT)만 지정합니다.
        Pageable pageable = PageRequest.of(0, pageSize);

        // 방 정보 조회 (참여자 목록 추출용)
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .orElseThrow(() -> new NotFoundChatRoomException(ErrorCode.Z000));

        // Domain의 참여자 정보를 DTO로 변환
        List<ChatParticipantResDto> participants = chatRoom.getChatParticipants().stream()
                .map(ChatParticipantResDto::fromDomain)
                .toList();

        List<ChatMessage> messages = chatMessageRepository.findMessagesByRoomNo(chatRoomNo, lastMessageNo, pageable);
        List<ChatMessageResDto> messageDtos = messages.stream()
                .map(ChatMessageResDto::fromDomain)
                .toList();

        return new ChatMessageListResDto(messageDtos, participants);
    }

    @Override
    @Transactional
    public ChatReadResDto readChatMessage(Long chatRoomNo, Long memberNo, Long lastReadChatMessageNo) {
        // 루트 애그리거트는 ChatRoom이고 그 안에 ChatParticipant 도메인이 있다. 따라서, 루트 애그리거트인 ChatRoom을 통해서만 업데이트되어야 한다.

        //ChatParticipant 찾기
//        ChatParticipant chatParticipant = chatRepository.findChatRoomByChatRoomNo(chatRoomNo)
//                .stream()
//                .map(ChatRoom::getChatParticipants)
//                .flatMap(List::stream) // List<ChatParticipant>를 개별 ChatParticipant 스트림으로 변환
//                .filter(cp -> cp.getMemberNo().equals(memberNo))
//                .findFirst()
//                .orElseThrow(() -> new NotFoundChatParticipantException(ErrorCode.Z001));

        ChatRoom chatRoom = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .orElseThrow(() -> new NotFoundChatRoomException(ErrorCode.Z000));

        chatRoom.readMessageOfMember(memberNo,lastReadChatMessageNo);
        chatRoomRepository.save(chatRoom);

        return ChatReadResDto.builder()
                .chatRoomNo(chatRoomNo)
                .memberNo(memberNo)
                .lastReadChatMessageNo(lastReadChatMessageNo)
                .build();
    }

    @Override
    public List<ChatRoomResDto> getMyChatRooms(Long memberNo) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findChatRoomsByMemberNoAndChatRoomType(memberNo,ChatRoomType.PRIVATE);

        if(chatRoomList.isEmpty()){
            // 상대방과 대화방이 존재하지 않는 경우 바로 return
            return Collections.emptyList();
        }

        // 조회할 방 번호 리스트 추출
        List<Long> roomNoList = chatRoomList.stream()
                .map(ChatRoom::getChatRoomNo)
                .toList();

        Map<Long, List<ChatParticipant>> roomMap = chatRoomList.stream()
                .collect(Collectors.toMap(ChatRoom::getChatRoomNo,ChatRoom::getChatParticipants));

        // 각 방 마지막 메시지 조회
        // 여기에 채팅방번호 있으니 위 애들은 필요 없다.
        List<ChatMessage> lastChatMessageList = chatMessageRepository.findLatestMessagesByRoomNos(roomNoList);

        // 각 방별 안 읽은 메시지 개수 조회 (Map으로 변환: chatRoomNo -> unreadCount)
        Map<Long, Long> unreadCountMap = chatRoomRepository.findUnreadCountsByMemberNoAndRoomNos(memberNo, roomNoList)
                .stream()
                .collect(Collectors.toMap(
                        UnreadCountMapping::getRoomNo,
                        UnreadCountMapping::getUnreadCount
                ));

        return lastChatMessageList.stream()
                .map(chatMessage -> {
                    Long roomNo = chatMessage.getChatRoomNo();

                    //상대방 누군지
                    Long friendMemberNo = roomMap.get(roomNo)
                            .stream()
                            .map(ChatParticipant::getMemberNo)
                            .filter(chatParticipant -> !chatParticipant.equals(memberNo))
                            .findFirst()
                            .orElse(null);

                    Long unreadCount = unreadCountMap.getOrDefault(roomNo, 0L);

                    return ChatRoomResDto.builder()
                            .chatRoomNo(roomNo)
                            .friendMemberNo(friendMemberNo)
                            .messageType(chatMessage.getMessageType())
                            .lastContent(chatMessage.getContent())
                            .lastSendDt(chatMessage.getSendDt())
                            .lastSendTm(chatMessage.getSendTm())
                            .lastSenderMemberNo(chatMessage.getSenderMemberNo())
                            .unreadCount(unreadCount)
                            .build();
                })
                .sorted((a, b) -> {
                    String dateTimeA = a.getLastSendDt() + a.getLastSendTm();
                    String dateTimeB = b.getLastSendDt() + b.getLastSendTm();
                    return dateTimeB.compareTo(dateTimeA); // 문자열 내림차순 비교
                })
                .toList()
                ;
    }

    @Override
    @Transactional
    public ChatMessageSendResDto createChatMessage(Long chatRoomNo, ChatMessageSendReqDto payload) {
        //이게 왜 필요할까?
        Long receiverMemberNo = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .stream()
                .map(ChatRoom::getChatParticipants)
                .flatMap(List::stream) // List<ChatParticipant>를 개별 ChatParticipant 스트림으로 변환
                .map(ChatParticipant::getMemberNo)
                .filter(memberNo -> !memberNo.equals(payload.getSenderMemberNo()))
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorCode.F000));

        ChatMessage chatMessage = ChatMessage.createChatMessage(
                chatRoomNo,
                payload.getSenderMemberNo(),
                payload.getContent(),
                payload.getMessageType()
        );

        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        return ChatMessageSendResDto.fromDomain(savedChatMessage, payload.getName(), receiverMemberNo);
    }
}