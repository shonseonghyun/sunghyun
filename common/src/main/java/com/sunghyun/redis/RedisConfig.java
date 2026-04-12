package com.sunghyun.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

//    @Value("${spring.data.redis.password}")
//    private String password;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host,port);
//        config.setPassword(password);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        //String 타입의 키와 Object 타입의 값을 저장할 수 있또록 설정
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();

        // RedisConnectionFactory 설정
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Key-Value 형태로 직렬화를 수행합니다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // Value를 JSON 형태로 직렬화하여 객체를 저장할 수 있게 함
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash Key-Value 형태로 직렬화를 수행합니다.
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        // 기본적으로 직렬화를 수행합니다.
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
