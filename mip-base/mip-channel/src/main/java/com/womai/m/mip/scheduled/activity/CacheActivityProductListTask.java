package com.womai.m.mip.scheduled.activity;

import com.womai.m.mip.channel.BaseGeneralResponse;
import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.activity.ActivityInfo;
import com.womai.m.mip.manager.cache.CacheManager;
import com.womai.m.mip.scheduled.BaseScheduledTask;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/5/16.
 */
@Component("cacheActivityProductListTask")
public class CacheActivityProductListTask extends BaseScheduledTask {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CacheManager cacheManager;

    @Value("${chihuo.activity}")
    private String activityJsonStr;

    @Override
    public String getTaskName() {
        return "CacheActivityProductListTask";
    }

    @Override
    public void execute() throws Exception {
        List<Map<String, String>> activityList = (List<Map<String, String>>)JacksonUtil.toMap(activityJsonStr).get("data");

        for (Map<String, String> activity : activityList) {
            String sid = activity.get("sid");
            String cityCode = activity.get("cityCode");
            int mid = Integer.parseInt(activity.get("mid"));
            new Thread(new CacheActivityProductListJob(sid, mid, cityCode)).start();
        }
    }

    private class CacheActivityProductListJob implements Runnable {

        private String sid;

        private int mid;

        private String cityCode;

        public CacheActivityProductListJob(String sid, int mid, String cityCode) {
            this.sid = sid;
            this.mid = mid;
            this.cityCode = cityCode;
        }

        @Override
        public void run() {

            try{
                logger.info("sync activity list request, sid: {}, mid:{}, cityCode:{}", sid, mid, cityCode);
                BaseServiceResponse<ActivityInfo> syncListResponse = activityService.syncList(sid, mid, cityCode);

                if (BaseServiceResponse.RESPONSE_SUCCESS.equals(syncListResponse.getCode())) {
                    logger.info("sync activity list success: {}", JacksonUtil.toJson(syncListResponse.getData()));
                    BaseGeneralResponse successResponse = new BaseGeneralResponse(syncListResponse.getData());

                    String cacheStr = JacksonUtil.toJson(successResponse);
                    String cacheKey = "ActivityProductList_sid_" + sid + "_mid_" + mid;
                    cacheManager.set(cacheKey, cacheStr);
                    logger.info("cache activity product list: key={}, value={}", cacheKey, cacheStr);

                } else {
                    logger.error("Fail to sync activity list:{}", syncListResponse.getMessage());
                }

            } catch (Exception e) {
                logger.error("Fail to sync activity list", e);
            }
        }
    }

}
