package com.sunghyun.notification.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-server}")
    private String bootStrapServer;

    @Value("${spring.kafka.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // 💡 2. VALUE 설정 정상 복구 (클래스 타입으로 주입)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);


        // 💡 3. 프로퍼티 문자열 키값을 활용해 헤더를 무시하고 타겟 DTO 지정하기
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        // 🔥 [핵심 추가] 인스턴스를 주입할 수 없으니, 문자열 프로퍼티로 타겟 DTO 타입을 주입합니다.
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.sunghyun.notification.application.port.in.dto.NotificationRequestEventDto");


        //컨슈머 명칭
        final String uniqueConsumerName = "plab-noti-consumer";
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, uniqueConsumerName);

        // 초기 오프셋 읽기 위치 설정
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 수동 커밋
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
//            DefaultErrorHandler dlqErrorHandler
//            CommonErrorHandler commonErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        factory.setCommonErrorHandler(commonErrorHandler);
        return factory;
    }
}
