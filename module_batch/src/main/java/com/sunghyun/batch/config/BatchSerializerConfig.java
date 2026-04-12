//package com.sunghyun.batch.config;
//
//import org.springframework.batch.core.repository.ExecutionContextSerializer;
//import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class BatchSerializerConfig {
//
//    @Bean
//    public ExecutionContextSerializer executionContextSerializer() {
//        // 자바 직렬화 대신 Jackson을 이용해 JSON 문자열로 저장하게 합니다.
//        return new Jackson2ExecutionContextStringSerializer();
//    }
//}