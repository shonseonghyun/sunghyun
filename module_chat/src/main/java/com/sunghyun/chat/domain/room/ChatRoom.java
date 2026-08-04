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
            if(chatParticipants.size() < 3){
                // 2명이면 PRIVATE 채팅으로 변환 X
                // 이거 변환을 어떻게 알리지? 도메인 계층에 로깅하는 건 별론데..
                // + 그냥 정책을 바꾸자. GROUP + 3명 미만은 에러 뱉자
                throw new InvalidParticipantCountException(ErrorCode.Z004); // 단체방은 3명 이상이어야 함 (정책에 따라 조절)
            }
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