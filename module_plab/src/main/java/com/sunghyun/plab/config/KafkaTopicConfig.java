package com.sunghyun.plab.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${plab.kafka.topics.subscription-noti}")
    private String topic;

    @Bean
    public NewTopic subscriptionNotiTopic(){
        return TopicBuilder.name(topic)
                .partitions(3)
                .replicas(1)
                .config("min.insync.replicas", "1")
                .build()
                ;
    }
}
