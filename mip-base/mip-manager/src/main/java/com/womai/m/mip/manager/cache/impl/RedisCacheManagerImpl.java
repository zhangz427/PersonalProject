package com.womai.m.mip.manager.cache.impl;

import com.womai.m.mip.manager.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by zheng.zhang on 2016/3/22.
 */
@Component("redisCacheManager")
public class RedisCacheManagerImpl implements CacheManager {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, String value) throws Exception {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long timeout) throws Exception {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }
}
