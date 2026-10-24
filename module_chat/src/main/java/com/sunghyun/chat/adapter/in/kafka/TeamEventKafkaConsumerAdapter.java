package com.sunghyun.chat.adapter.in.kafka;

import com.sunghyun.chat.application.service.ChatRoomService;
import com.sunghyun.event.team.TeamCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamEventKafkaConsumerAdapter {
    private final ChatRoomService chatRoomService;

    @KafkaListener(
            topics = "${kafka.topic.team-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleTeamCreated(TeamCreatedEvent event){
        log.info("팀 생성 이벤트 발생하여 그룹 채팅방 생성 시작[{}]",event);

        Long teamNo = event.getTeamNo();
        Long creatorMemberNo = event.getCreatorMemberNo();

        chatRoomService.createTeamChatRoom(teamNo,creatorMemberNo);
    }
}
