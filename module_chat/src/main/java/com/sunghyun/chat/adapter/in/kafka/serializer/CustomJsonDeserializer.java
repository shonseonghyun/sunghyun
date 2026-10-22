package com.sunghyun.chat.adapter.in.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomJsonDeserializer implements Deserializer<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data) {
        if (data == null) return null;

        try {
            // 1. 헤더에서 이벤트 타입 클래스 이름을 꺼냅니다.
            String targetClassName = null;
            if (headers != null && headers.lastHeader("__EventType__") != null) {
                targetClassName = new String(headers.lastHeader("__EventType__").value(), StandardCharsets.UTF_8);
            }

            if (targetClassName == null) {
                throw new SerializationException("Missing __EventType__ header in Kafka message");
            }

            // 2. 문자열로 되어 있는 클래스 명을 실제 Class 객체로 로딩합니다.
            Class<?> targetClass = Class.forName(targetClassName);

            // 3. 해당 클래스 타입으로 JSON 바이트를 곧바로 역직렬화합니다!
            return objectMapper.readValue(data, targetClass);

        } catch (Exception e) {
            log.error("동적 역직렬화 실패", e);
            throw new SerializationException("Error when deserializing byte[] to object", e);
        }
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        return deserialize(topic, null, data);
    }
}