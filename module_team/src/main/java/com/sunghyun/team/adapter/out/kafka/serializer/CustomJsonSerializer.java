package com.sunghyun.team.adapter.out.kafka.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.header.internals.RecordHeader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomJsonSerializer implements Serializer<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Headers headers, Object data) { // 👈 헤더를 받을 수 있는 오버로드 메서드
        if (data == null) return null;

        try {
            // 💡 핵심: 헤더에 이 객체의 풀패키지 클래스 이름을 기록합니다!
            if (headers != null) {
                headers.add(new RecordHeader("__EventType__", data.getClass().getName().getBytes(StandardCharsets.UTF_8)));
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            log.error("ObjectMapper 변환 에러", e);
            throw new SerializationException("Error when serializing object", e);
        }
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        return serialize(topic, null, data);
    }
}