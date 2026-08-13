package com.sunghyun.chat.application.service;

import com.sunghyun.chat.application.dto.req.ChatMessageSendReqDto;
import com.sunghyun.chat.application.dto.req.ChatRoomCreateReqDto;
import com.sunghyun.chat.application.dto.req.ChatRoomInviteReqDto;
import com.sunghyun.chat.application.dto.res.*;
import com.sunghyun.chat.application.port.in.ChatRoomUseCase;
import com.sunghyun.chat.application.port.out.ChatEventPublisherPort;
import com.sunghyun.chat.application.port.out.dto.ChatEvent;
import com.sunghyun.chat.domain.dto.UnreadCountMapping;
import com.sunghyun.chat.domain.exception.NotFoundChatRoomException;
import com.sunghyun.chat.domain.message.ChatMessage;
import com.sunghyun.chat.domain.message.repository.ChatMessageRepository;
import com.sunghyun.chat.domain.room.ChatRoom;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import com.sunghyun.chat.domain.room.repository.ChatRoomRepository;
import com.sunghyun.web.ErrorCode;
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
public class ChatRoomService implements ChatRoomUseCase {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatEventPublisherPort chatEventPublisherPort;

    @Override
    public ChatRoomSelectResDto getChatRoom(Long chatRoomNo, Long memberNo) {
        ChatRoom selectedChatRoom = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .orElseThrow(()->new NotFoundChatRoomException(ErrorCode.Z000));

        return ChatRoomSelectResDto.fromDomain(selectedChatRoom);
    }

    @Override
    public ChatRoomCreateResDto getOrCreateChatRoom(Long memberNo, ChatRoomCreateReqDto reqDto) {
        // 아예 처음 이면, 새로 생성
        // 있던 방인데 나간거면. 이전 대화 기록과 함께 다시 가져오고, 채팅방 입장시키키

        final Long targetMemberNo = reqDto.getTargetMemberNo();
        final ChatRoomType chatRoomType = ChatRoomType.PRIVATE;

        ChatRoom selectedChatRoom = chatRoomRepository.findChatRoomByMemberNosAndChatRoomType(memberNo, targetMemberNo, chatRoomType)
                .orElse(null);

        // 기존 방이 없을 때만 새 방 저장
        if(selectedChatRoom==null){
            ChatRoom newChatRoom = ChatRoom.createPrivateChatRoom(memberNo,targetMemberNo);
            ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);
            return new ChatRoomCreateResDto(savedChatRoom.getChatRoomNo());
        }
        else{
            // 있었던 경우, 입장시키기
            // + 근데 채팅을 쳐야 입장시켜야 할듯? 주석처리하자
//            selectedChatRoom.enteredMember(memberNo);
//            ChatRoom savedChatRoom = chatRoomRepository.save(selectedChatRoom);
            return new ChatRoomCreateResDto(selectedChatRoom.getChatRoomNo());
        }
    }

    @Override
    public List<ChatRoomResDto> getMyChatRooms(Long memberNo) {
        // 1.회원 참여한 모든 채팅방 추출
        // + 회원이 나간 채팅방 제외
        List<ChatRoom> activeChatRooms = chatRoomRepository.findChatRoomsByMemberNo(memberNo)
                .stream()
                .filter(chatRoom->!chatRoom.isMemberLeft(memberNo))
                .toList()
                ;

        // 2.참여한 채팅방 존재하지 않으면 바로 return
        if(activeChatRooms.isEmpty()){
            return Collections.emptyList();
        }

        //3. 각 방 별로 마지막 메시지를 추출한다.
        List<Long> roomNoList = activeChatRooms.stream()
                .map(ChatRoom::getChatRoomNo)
                .toList();

        // 각 방 별 마지막 메시지(도메인) 담은 map
        Map<Long,ChatMessage> lastChatMessageMap = chatMessageRepository.findLatestMessagesByRoomNos(roomNoList)
                .stream()
                .collect(Collectors.toMap(
                                ChatMessage::getChatRoomNo,
                                chatMessage -> chatMessage
                        )
                );

        //4. 각 방 별 안 읽은 메시지 개수를 담은 map
        Map<Long,Integer> unreadCountMap = chatRoomRepository.findUnreadCountsByMemberNoAndRoomNos(memberNo, roomNoList)
                .stream()
                .collect(Collectors.toMap(
                        UnreadCountMapping::getChatRoomNo,
                        UnreadCountMapping::getUnreadCount
                ));

        return activeChatRooms.stream()
                .map(chatRoom -> {
                    Long chatRoomNo = chatRoom.getChatRoomNo();
                    ChatMessage lastChatMessage = lastChatMessageMap.get(chatRoomNo);

                    //회원 정보 핸들링
                    List<ChatMemberInfo> chatMembers = chatRoom.getChatParticipants().stream()
                            .map(chatParticipant -> ChatMemberInfo.builder()
                                    .memberNo(chatParticipant.getMemberNo())
                                    .isLeft(chatParticipant.isLeft())
                                    .name("회원명은아직임")
                                    .build()
                            )
                            .toList();

                    // 마지막 메시지가 있는 경우 DTO 생성
                    LastMessageInfoResDto lastMessageInfo = (lastChatMessage != null)
                            ? new LastMessageInfoResDto(
                            lastChatMessage.getContent(),
                            lastChatMessage.getMessageType(),
                            lastChatMessage.getSendDt(),
                            lastChatMessage.getSendTm())
                            : null;

                    return ChatRoomResDto.builder()
                            .chatRoomNo(chatRoomNo)
                            .chatRoomType(chatRoom.getChatRoomType())
                            .chatMembers(chatMembers)
                            .participantCount(chatRoom.getParticipantCount())
                            .lastMessageInfo(lastMessageInfo)
                            .unreadCount(unreadCountMap.getOrDefault(chatRoomNo, 0))
                            .build();
                })
                // 채팅방 들간 순서 정렬은 최신 메시지 순으로 정렬한다.
                .sorted((a, b) -> { // 주석의 요구사항 반영: 최신 메시지 날짜/시간 내림차순 정렬
                    if (a.getLastMessageInfo() == null) return 1;  // 메시지 없는 방은 뒤로
                    if (b.getLastMessageInfo() == null) return -1;

                    String dateTimeA = a.getLastMessageInfo().getSendDt() + a.getLastMessageInfo().getSendTm();
                    String dateTimeB = b.getLastMessageInfo().getSendDt() + b.getLastMessageInfo().getSendTm();

                    return dateTimeB.compareTo(dateTimeA); // 최신순(내림차순)
                })
                .toList();
    }





    @Override
    @Transactional
    public void leaveMember(Long chatRoomNo, Long leavedMemberNo) {
        // 채팅 도메인 조회
        ChatRoom selectedChatRoom = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .orElseThrow(()->new NotFoundChatRoomException(ErrorCode.Z000));


        // 채팅방에서 회원 나가기
        selectedChatRoom.leaveMember(leavedMemberNo);  //isLeft, 0L

        //단체 채팅방은 누가 나갈 경우, 상대방들에게 나갔다고 알림해야한다.
        if(selectedChatRoom.isGroupRoom()){
            ChatMessage chatMessage = ChatMessage.createLeaveMessage(chatRoomNo,leavedMemberNo);
            ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
            ChatMessageResDto result = ChatMessageResDto.fromDomain(savedChatMessage, selectedChatRoom.getReceiverMemberNos(leavedMemberNo));
            chatEventPublisherPort.publishLeavedChatRoom(new ChatEvent.ChatRoomLeaved(chatRoomNo,result));
        }

        // 영속화
        ChatRoom savedChatRoom = chatRoomRepository.save(selectedChatRoom);
    }

    @Override
    @Transactional
    public ChatRoomCreateResDto inviteMember(Long chatRoomNo, Long memberNo, ChatRoomInviteReqDto reqDto) {
        ChatRoom selectedChatRoom = chatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
                .orElseThrow(()->new NotFoundChatRoomException(ErrorCode.Z000));

        // 개인방인 경우, 새롭게 그룹방 생성
        if(selectedChatRoom.isPrivateRoom()){
            // 단체방 도메인 생성
            ChatRoom newChatRoom = ChatRoom.createChatRoom(ChatRoomType.GROUP,memberNo,reqDto.getTargetMemberNos());
            ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);

            // 새롭게 만들었으면 chatRoomNo를 응답해야 한다.
            return new ChatRoomCreateResDto(savedChatRoom.getChatRoomNo());
        }

        // 단체방인 경우, 초대
        else if(selectedChatRoom.isGroupRoom()){
            ChatMessage invitedChatMessage = ChatMessage.createInviteMessage(chatRoomNo,memberNo,selectedChatRoom.getReceiverMemberNos(memberNo));
            ChatMessage savedInvitedChatMessage = chatMessageRepository.save(invitedChatMessage);

            selectedChatRoom.inviteMember(reqDto.getTargetMemberNos(),savedInvitedChatMessage.getChatMessageNo());
            chatRoomRepository.save(selectedChatRoom);

            ChatMessageResDto result = ChatMessageResDto.fromDomain(savedInvitedChatMessage,selectedChatRoom.getReceiverMemberNos(memberNo));
            chatEventPublisherPort.publishInvitedChatRoom(new ChatEvent.ChatRoomInvited(chatRoomNo, result));
        }

        return new ChatRoomCreateResDto(selectedChatRoom.getChatRoomNo());
    }


}