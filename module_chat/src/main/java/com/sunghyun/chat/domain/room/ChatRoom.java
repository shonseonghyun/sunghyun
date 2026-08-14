package com.sunghyun.chat.domain.room;

import com.sunghyun.chat.domain.exception.InvalidParticipantCountException;
import com.sunghyun.chat.domain.exception.NotFoundChatParticipantException;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
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

    public static ChatRoom createPrivateChatRoom(Long memberNo, Long targetMemberNo) {
        List<ChatParticipant> chatParticipants = new ArrayList<>();
        chatParticipants.add(ChatParticipant.createChatParticipant(memberNo));
        chatParticipants.add(ChatParticipant.createChatParticipant(targetMemberNo));

        return ChatRoom.builder()
                .chatRoomType(ChatRoomType.PRIVATE)
                .chatParticipants(chatParticipants)
                .createdDt(ApiUtils.getCurrentDt())
                .createdTm(ApiUtils.getCurrentTm())
                .build()
                ;
    }

    public static ChatRoom createChatRoom(ChatRoomType roomType, Long memberNo, List<Long> targetMemberNos) {
        // ChatParticipant List로 만드는 로직 도메인 메소드로 빼야하나?
        // + 뺐다.
        // 참여자 목록 생성 위임
        List<ChatParticipant> chatParticipants = createParticipants(memberNo, targetMemberNos);

        // 검증
        if(chatParticipants.size()<2){
            throw new InvalidParticipantCountException(ErrorCode.Z003);
        }

        if(roomType.equals(ChatRoomType.PRIVATE)){
            // 개인 채팅인 경우
            if(chatParticipants.size() != 2){
                // 2명이 아닌 경우
                throw new InvalidParticipantCountException(ErrorCode.Z002);
            }
        }

        if(roomType.equals(ChatRoomType.GROUP)){
            // 그룹 채팅인 경우
//            if(chatParticipants.size() < 3){
//                // 2명이면 PRIVATE 채팅으로 변환 X
//                // 이거 변환을 어떻게 알리지? 도메인 계층에 로깅하는 건 별론데..
//                // + 그냥 정책을 바꾸자. GROUP + 3명 미만은 에러 뱉자
//                throw new InvalidParticipantCountException(ErrorCode.Z004); // 단체방은 3명 이상이어야 함 (정책에 따라 조절)
//            }
        }

        if(roomType.equals(ChatRoomType.TEAM)){
            // 팀 채팅인 경우
        }

        return ChatRoom.builder()
                .chatRoomType(roomType)
                .chatParticipants(chatParticipants)
                .createdDt(ApiUtils.getCurrentDt())
                .createdTm(ApiUtils.getCurrentTm())
                .build()
                ;
    }

    // 참여자 리스트를 조립하는 책임을 도메인 내부로 격리
    private static List<ChatParticipant> createParticipants(Long creatorMemberNo, List<Long> targetMemberNos) {
        List<ChatParticipant> participants = new ArrayList<>();

        // 생성자(나) 추가
        participants.add(ChatParticipant.createChatParticipant(creatorMemberNo));

        // 타겟 멤버들 추가 (null 체크 포함)
        if (targetMemberNos != null) {
            targetMemberNos.forEach(targetMemberNo ->
                    participants.add(ChatParticipant.createChatParticipant(targetMemberNo))
            );
        }

        return participants;
    }

    public void readMessageOfMember(Long memberNo, Long lastReadChatMessageNo) {
        ChatParticipant readedParticipant = this.getChatParticipants()
                .stream()
                .filter(chatParticipant -> chatParticipant.getMemberNo().equals(memberNo))
                .findFirst()
                .orElseThrow(()->new NotFoundChatParticipantException(ErrorCode.Z001));

        readedParticipant.readMessage(lastReadChatMessageNo);
    }

    public List<Long> getReceiverMemberNos(Long senderMemberNo) {
        // 개인 채팅방은 상대방이 나가도 그대로 회원번호 및 이름 응답
        // 단체(팀,그룹) 채팅방은 상대방이 나가면 '알수없음'으로 응답
        if(this.chatRoomType == ChatRoomType.PRIVATE){
            return this.chatParticipants.stream()
                    .map(ChatParticipant::getMemberNo)
                    .toList();
        }

        else {
            return this.chatParticipants.stream()
                    .filter(p -> !p.isLeft())
                    .filter(p -> !p.getMemberNo().equals(senderMemberNo))
                    .map(ChatParticipant::getMemberNo)
                    .toList();
        }

    }

    public Integer getParticipantCount() {
        // 개인 채팅방은 1
        // 단체(팀,그룹) 채팅방은 상대방이 나가면 그만큼 빼고 응답

        //떠나지 않은 사람만
        return chatParticipants.stream()
                .filter(p-> !p.isLeft())
                .toList().size()
                ;
    }

    public void leaveMember(Long targetMemberNo) {
        //채팅방 나가는 대상 회원
        ChatParticipant targetChatParticipant = this.chatParticipants.stream()
                .filter(p->p.getMemberNo().equals(targetMemberNo))
                .findFirst()
                .orElseThrow(()->new NotFoundChatParticipantException(ErrorCode.Z001))
                ;

        //채팅방 대상 나가기
        targetChatParticipant.leave();
    }

    public boolean isDisplayedTo(Long memberNo) {
        return this.chatParticipants.stream()
                .filter(p -> p.getMemberNo().equals(memberNo))
                .findFirst()
                .map(ChatParticipant::isDisplayed) // 💡 해당 회원의 노출(isDisplayed) 상태를 그대로 반환
                .orElse(false); // 💡 참여자가 아예 없거나 찾을 수 없으면 안 보여주는 것(false)이 안전함
    }

    public void inviteMember(List<Long> targetMemberNos,Long chatMessageNo) {
        if (targetMemberNos == null || targetMemberNos.isEmpty()) return;

        for (Long targetMemberNo : targetMemberNos) {
            ChatParticipant existingParticipant = this.chatParticipants.stream()
                    .filter(p -> p.getMemberNo().equals(targetMemberNo))
                    .findFirst()
                    .orElse(null);

            //채팅방 한 번이라도 참여했던 사람이라면
            if (existingParticipant != null) {
                // 나간 사람이였다면
                if(existingParticipant.isLeft()){
                    // 재참가 처리 및 최신 메시지 번호 세팅
                    existingParticipant.enter(chatMessageNo);
                }
            } else {
                // 아예 새로운 사람이면 추가
                ChatParticipant newChatParticipant = ChatParticipant.createInvitedParticipant(targetMemberNo,chatMessageNo);
                this.chatParticipants.add(newChatParticipant);
            }
        }
    }

    public void handleNewMessage(boolean hasMessage, Long chatMessageNo) {
        // 개인 채팅방(PRIVATE)일 때만, 나갔던 상대방의 상태를 다시 활성화(reenter) 시킴
        // a.첫 채팅 시작 또는 b.초대된(채팅 시작) 경우, 읽기 시작한 메시지번호 세팅

        // 그룹 채팅방
        // 첫 채팅 시작 또는 초대된 경우, 읽기 시작한 메시지번호 세팅

        if (this.chatRoomType == ChatRoomType.PRIVATE) {
            this.chatParticipants.forEach(participant -> {
                participant.assignFirstViewableMessage(chatMessageNo);
                participant.displayed();
                // 누구든 나간 경우, 재입장시키기
                // 근데 첫 채팅인데 상대방이 나간 경우는 없다.
                if (participant.isLeft()) {
                    participant.reEnter(chatMessageNo);
                }
            });
        }

        else if (this.chatRoomType == ChatRoomType.GROUP) {
            // inviteMember에서 초대 시점 기준으로 최신 메시지 번호 세팅하므로 패스
            this.chatParticipants.forEach(participant -> {
                if (!participant.isLeft()) {
                    participant.displayed();
                }
            });
        }
    }


    public boolean isPrivateRoom() {
        return this.chatRoomType == ChatRoomType.PRIVATE;
    }

    public boolean isGroupRoom() {
        return this.chatRoomType == ChatRoomType.GROUP;
    }


    public Long getFirstViewableChatMessageNoOfMember(Long memberNo) {
        return this.chatParticipants.stream()
                .filter(chatParticipant->chatParticipant.getMemberNo().equals(memberNo))
                .map(ChatParticipant::getFirstViewableChatMessageNo)
                .findFirst()
                .orElse(null)
                ;
    }

    public void setInvitedMessageAllChatParticipant(Long chatMessageNo) {
        for(ChatParticipant chatParticipant:this.chatParticipants){
            chatParticipant.assignFirstViewableMessage(chatMessageNo);
        }
    }
}