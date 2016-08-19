package com.womai.m.mip.scheduled;

import com.womai.m.mip.common.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by zheng.zhang on 2016/5/9.
 */
@Component("baseScheduledTask")
public abstract class BaseScheduledTask implements ScheduledTask {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    public void executeExcludeTask(){

        try {
            if (RedisUtil.acquireLock(redisTemplate, taskNameToLockKey(this.getTaskName()), 10000L)) {
                executeTask();
            }
        } catch(Exception e) {
            logger.error("Fail to execute task:" + this.getTaskName() + ",", e);
        }finally {
//            RedisUtil.releaseLock(redisTemplate, taskNameToLockKey(this.getTaskName()));
        }
    }

    public void executeTask() {
        try {
            logger.info("Execute task: {}", this.getTaskName());
            long startTime = System.currentTimeMillis();
            this.execute();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Execute task: {} finished, duration:{}", this.getTaskName(), duration);
        } catch(Exception e) {
            logger.error("Fail to execute task:" + this.getTaskName() + ",", e);
        }
    }

    private String taskNameToLockKey(String taskName) {
        return "ScheduledTaskLock:" + taskName;
    }

}
