package com.sunghyun.chat.adapter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitProperties {
    private Exchange exchange = new Exchange();
    private Queue queue = new Queue();
    private Routing routing = new Routing();

    @Getter
    @Setter
    public static class Exchange{
        private String name;
    }

    @Getter
    @Setter
    public static class Queue{
        private String chat;
        private String member;
    }

    @Getter
    @Setter
    public static class Routing {
        private ChatRouting chat = new ChatRouting();
        private MemberRouting member = new MemberRouting();

        @Getter
        @Setter
        public static class ChatRouting {
            private String prefix;
            private String key;
        }

        @Getter
        @Setter
        public static class MemberRouting {
            private String prefix;
            private String key;
        }
    }

    // 💡 동적 라우팅 키 생성 헬퍼 메서드 제공
    public String getRoomRoutingKey(Long roomNo) {
        return routing.getChat().getPrefix() + "." + roomNo;
    }

    public String getMemberRoutingKey(Long memberNo) {
        return routing.getMember().getPrefix() + "." + memberNo;
    }
}