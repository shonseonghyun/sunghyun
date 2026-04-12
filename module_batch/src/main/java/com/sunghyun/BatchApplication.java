package com.sunghyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BatchApplication {
    public static void main(String[] args) {
        // 실행 시 전해지는 인자(Job Name 등)를 함께 넘겨줍니다.
        SpringApplication.run(BatchApplication.class, args);
    }
}
