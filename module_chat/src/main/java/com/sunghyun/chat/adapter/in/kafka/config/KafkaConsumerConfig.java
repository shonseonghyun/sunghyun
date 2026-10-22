package com.sunghyun.chat.adapter.in.kafka.config;

import com.sunghyun.chat.adapter.in.kafka.serializer.CustomJsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServer;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String,Object> props = new HashMap<>();

        // 카프카 브로커 주소
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServer);

        // Consumer Gorup Id 설정
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // 키 역질렬화 방식
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // 값 역직렬화 방식
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomJsonDeserializer.class);

        // 이벤트 패키지 신뢰할 수 있도록 설정
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.sunghyun.event.*");

        // 카프카 메시지 헤더에 붙는 자바 타입 정보(__TypeId__)를 무시하고
        // 리스너 메서드가 받는 파라미터 타입(TeamCreatedEvent)으로 강제 매핑하도록 설정합니다!
//        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    // 2. 리스너 컨테이너 팩토리 설정 (@KafkaListener가 동작하도록 돕는 핵심 객체)
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // (선택) 동시에 처리할 수 있는 컨슈머 스레드 수 조절 (파티션 개수와 맞추거나 적절히 조절)
        // factory.setConcurrency(3);

        return factory;
    }
}
