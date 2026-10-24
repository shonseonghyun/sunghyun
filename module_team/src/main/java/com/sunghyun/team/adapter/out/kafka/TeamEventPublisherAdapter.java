package com.sunghyun.team.adapter.out.kafka;

import com.sunghyun.event.team.TeamCreatedEvent;
import com.sunghyun.team.application.port.out.TeamEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamEventPublisherAdapter implements TeamEventPublisher {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Value("${kafka.topic.team-created}")
    private String teamCreatedTopic;

    @Override
    public void publishTeamCreated(TeamCreatedEvent event) {
        final String key = String.valueOf(event.getTeamNo());

        kafkaTemplate.send(
                teamCreatedTopic,
                key,
                event
        );
    }
}
