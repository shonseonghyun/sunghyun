package com.sunghyun.redis;


import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractRedisRepository {
    protected final RedisService redisService;

    public Optional<Object> getValue(String key){
        return Optional.ofNullable(redisService.getValueByKey(key));
    }

    public void delete(String key){
        redisService.delete(key);
    }
}
