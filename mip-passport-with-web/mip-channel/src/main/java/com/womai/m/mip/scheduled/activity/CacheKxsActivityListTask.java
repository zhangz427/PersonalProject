package com.womai.m.mip.scheduled.activity;


import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.scheduled.BaseScheduledTask;
import com.womai.m.mip.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/5/13.
 */
@Component("cacheKxsActivityListTask")
public class CacheKxsActivityListTask extends BaseScheduledTask {

    @Autowired
    private ActivityService activityService;

    @Value("${kxs.main.page}")
    private String activityJsonStr;

    @Override
    public String getTaskName() {
        return "CacheKxsActivityListTask";
    }

    @Override
    public void execute() throws Exception {

        List<Map<String, String>> activityList = (List<Map<String, String>>)JacksonUtil.toMap(activityJsonStr).get("data");

        for (Map<String, String> activity : activityList) {
            String sid = activity.get("sid");
            int mid = Integer.parseInt(activity.get("mid"));
            new Thread(new CacheKxsActivityJob(sid, mid)).start();
        }
    }

    private class CacheKxsActivityJob implements Runnable {

        private String sid;

        private int mid;

        public CacheKxsActivityJob(String sid, int mid) {
            this.sid = sid;
            this.mid = mid;
        }

        @Override
        public void run() {
            logger.info("sync kxs activity list request, sid: {}, mid:{}", sid, mid);
            try {
                activityService.cacheKxsActivity(sid, mid);
            } catch (Exception e) {
                logger.error("Fail to sync kxs activity list request", e);
            }

        }
    }
}
