package com.womai.m.mip.common.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

/**
 * Created by zheng.zhang on 2016/5/9.
 */
public class RedisUtil {


    public static Long acquireLock(RedisTemplate redisTemplate, String lockKey, String lockValue){

        final byte[] lockKeyBytes = redisTemplate.getKeySerializer().serialize(lockKey);
        final byte[] lockValueBytes = redisTemplate.getValueSerializer().serialize(lockValue);
        return (Long)redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) {
                boolean locked = connection.setNX(lockKeyBytes, lockValueBytes);
                if(locked){
                    return 1L;
                }
                return 0L;
            }
        });
    }

    public static String getAndSet(RedisTemplate redisTemplate, String key, String value){
        final byte[] keyBytes = redisTemplate.getKeySerializer().serialize(key);
        final byte[] valueBytes = redisTemplate.getValueSerializer().serialize(value);
        return  (String)redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] result = connection.getSet(keyBytes,valueBytes);
                if(result!=null){
                    return new String(result);
                }
                return null;
            }
        });
    }

    public static boolean acquireLock(RedisTemplate redisTemplate, String lockKey, long expireMsecs) throws InterruptedException {

        long expires = System.currentTimeMillis() + expireMsecs + 1;
        String expiresStr = String.valueOf(expires); //锁到期时间
        if (acquireLock(redisTemplate, lockKey, expiresStr) == 1) {
            // lock acquired
            return true;
        }
        String currentValueStr = (String)redisTemplate.opsForValue().get(lockKey); //redis里的时间
        if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
            //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
            // lock is expired
            String oldValueStr = getAndSet(redisTemplate, lockKey, expiresStr);
            //获取上一个锁到期时间，并设置现在的锁到期时间，
            //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
            if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                //如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                // lock acquired
                return true;
            }
        }
        return false;
    }

    public static void releaseLock(RedisTemplate redisTemplate, String lockKey) {
        String currentValueStr = (String)redisTemplate.opsForValue().get(lockKey);
        if (currentValueStr != null && Long.parseLong(currentValueStr) > System.currentTimeMillis()) {
            redisTemplate.delete(lockKey);
        }
    }

}
