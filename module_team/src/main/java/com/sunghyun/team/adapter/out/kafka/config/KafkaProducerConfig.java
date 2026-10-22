package com.sunghyun.team.adapter.out.kafka.config;

import com.sunghyun.team.adapter.out.kafka.serializer.CustomJsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServer;

    @Bean
    public ProducerFactory<String,Object> producerFactory(){
        Map<String,Object> props = new HashMap<>();

        // 카프카 브로커 주소
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);

        // 키 직렬화
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 값 직렬화
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate(ProducerFactory<String,Object> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }
}
