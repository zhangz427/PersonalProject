package com.womai.m.mip.service.activity;

import com.womai.m.mip.domain.activity.ActivityInfo;
import com.womai.m.mip.manager.BaseManagerResponse;
import com.womai.m.mip.service.BaseServiceResponse;

/**
 * Created by zheng.zhang on 2016/5/4.
 */
public interface ActivityService {

    public BaseServiceResponse<ActivityInfo> syncList(String sid, int mid, String cityCode) throws Exception;

    public void cacheKxsActivity(String sid, int mid) throws Exception;
}
