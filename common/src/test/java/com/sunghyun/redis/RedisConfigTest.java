package com.sunghyun.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
        (
        classes = {RedisConfig.class, RedisService.class},
        properties = {
                "spring.data.redis.host=localhost",
                "spring.data.redis.port=6379",
                "spring.data.redis.password=385tjdgus"
        }
)
class RedisConfigTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // --- 테스트용 공통 상수 정의 ---
    private static final String ID = "testId";
    private static final String PWD = "password1234";
    private static final String WRONG_PWD = " ";
    private static final String EMAIL = "test@naver.com";
    private static final String WRONG_EMAIL = "test@";
    private static final String NAME = "홍길동";
    private static final String TEL = "01012345678";
    private static final String BIRTH_DT = "950204";
    private static final Long MEMBER_ID = 1L;

    @AfterEach
    void clean(){
        redisTemplate.delete(ID);
    }

    @Test
    void redisBaseTest(){
        redisTemplate.opsForValue().set(ID,PWD);
        String value = (String) redisTemplate.opsForValue().get(ID);
        assertThat(value).isEqualTo(PWD);
    }
}