package com.womai.m.mip.scheduled.activity;

import com.womai.m.mip.scheduled.BaseScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zheng.zhang on 2016/5/13.
 */
@Component("activityTestTask")
public class ActivityTestTask extends BaseScheduledTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getTaskName() {
        return "activityTestTask";
    }

    @Override
    public void execute() throws Exception {
        logger.info("This is activity test task.");
    }
}
