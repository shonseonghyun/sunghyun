package com.sunghyun.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
    @Bean(name = "emailAsyncExecutor")
    public Executor getAsyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // 기본 스레드 수
        executor.setMaxPoolSize(25);    // 최대 스레드 수
        executor.setQueueCapacity(30);  // Queue 사이즈
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.setAwaitTerminationSeconds(60);
        executor.setThreadNamePrefix("Executor-Thread-");
        return executor;
    }

//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return (ex, method, params) ->
//                log.error(
//                        "Asynchronous method thrown exception... -> Method = {}, Params = {}",
//                        method,
//                        params,
//                        ex
//                );
//    }
}
