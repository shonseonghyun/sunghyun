package com.sunghyun.chat.adapter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "chat.websocket.topic")
public class WebSocketTopicProperties {

    private String roomPrefix;   // yml에서 주입받음 (기본값 없음)
    private String memberPrefix; // yml에서 주입받음 (기본값 없음)

    public String getRoomTopic(Long chatRoomNo) {
        return roomPrefix + "/" + chatRoomNo;
    }

    public String getMemberTopic(Long memberNo) {
        return memberPrefix + "/" + memberNo;
    }
}