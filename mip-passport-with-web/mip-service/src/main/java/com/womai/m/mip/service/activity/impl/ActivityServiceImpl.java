package com.womai.m.mip.service.activity.impl;

import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.activity.ActivityInfo;
import com.womai.m.mip.domain.activity.Sku;
import com.womai.m.mip.domain.activity.SubActivity;
import com.womai.m.mip.domain.network.CommonData;
import com.womai.m.mip.manager.cache.CacheManager;
import com.womai.m.mip.manager.network.NetworkManager;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.activity.ActivityService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zheng.zhang on 2016/5/4.
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NetworkManager networkManager;

    @Autowired
    private CacheManager cacheManager;

    @Value("${wapi.base.url}")
    private String wapiBaseUrl;

    @Value("${sync.activity.list.url}")
    private String syncActivityListUrl;

    @Value("${sync.kxs.activity.list.url}")
    private String syncKxsActivityListUrl;

    @Override
    public BaseServiceResponse<ActivityInfo> syncList(String sid, int mid, String cityCode) throws Exception {

        BaseServiceResponse failResponse = null;
        Map<String, String> syncListParams = new HashMap<String, String>();
        syncListParams.put("sid", sid);
        CommonData commonData = new CommonData();
        commonData.setCityCode(cityCode);
        commonData.setMid(mid);

        String requestUrl = wapiBaseUrl + syncActivityListUrl;

        String syncListResponse = networkManager.requestData(requestUrl, commonData,syncListParams);
        if (StringUtils.contains(syncListResponse, "error")) {
            logger.error("Fail to get activity list: {}", syncListResponse);
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            failResponse.setMessage("Fail to get activity list");
            return failResponse;
        }
        ActivityInfo activityInfo = convertToActivityInfo(syncListResponse);
        return new BaseServiceResponse<ActivityInfo>(activityInfo);

    }

    @Override
    public void cacheKxsActivity(String sid, int mid) throws Exception {

        Map<String, String> syncListParams = new HashMap<String, String>();
        syncListParams.put("sid", sid);
        CommonData commonData = new CommonData();
        commonData.setMid(mid);
        commonData.setCityCode(Integer.toString(mid));

        String requestUrl = wapiBaseUrl + syncKxsActivityListUrl;

        String syncListResponse = networkManager.requestData(requestUrl, commonData,syncListParams);

        if (!StringUtils.contains(syncListResponse, "error")) {
            String cacheKey = "KXSActivity_mid_" + mid + "_sid_" + sid;
            logger.info("get kxs activity, key={}, value={}", cacheKey, syncListResponse);
            cacheManager.set(cacheKey, syncListResponse);
        }

    }

    private ActivityInfo convertToActivityInfo(String syncListResponse) throws Exception{
        ActivityInfo activityInfo = JacksonUtil.toObject(syncListResponse,ActivityInfo.class);
        removeSoldOutSku(activityInfo);
        return activityInfo;
    }

    private void removeSoldOutSku(ActivityInfo activityInfo){
        if(activityInfo==null){
            return;
        }
        List<SubActivity> subActivities = activityInfo.getSubActivity();
        if(subActivities==null||subActivities.isEmpty()){
            return;
        }
        Map<Long, Sku> skus = activityInfo.getSkulist();
        if(skus==null || skus.isEmpty()){
            return;
        }
        Iterator it = skus.entrySet().iterator();
        Sku sku;
        Map.Entry<Long, Sku> entry;
        List<Long> removedSkuIds = null;
        //清除售罄的商品信息
        while(it.hasNext()){
            entry = (Map.Entry<Long,Sku>)it.next();
            if(entry==null){
                continue;
            }
            sku=entry.getValue();
            if(sku!=null && !sku.isSellable()){
                it.remove();
                if(removedSkuIds==null){
                    removedSkuIds=new ArrayList<Long>();
                }
                //将售罄商品的id存储进removedSkuIds中
                removedSkuIds.add(entry.getKey());
            }
        }
        if(removedSkuIds==null){
            return;
        }
        List<SubActivity> activitiesHaveNoSku = null;
        //将skuIds集合中的售罄商品id清除
        for(SubActivity subActivity : subActivities){
            subActivity.getSkuIds().removeAll(removedSkuIds);
            //取出商品全部售罄的活动
            if(subActivity.getSkuIds()==null || subActivity.getSkuIds().isEmpty()){
                if(activitiesHaveNoSku==null){
                    activitiesHaveNoSku = new ArrayList<SubActivity>();
                }
                activitiesHaveNoSku.add(subActivity);
            }
        }
        //删除商品全部售罄的活动
        if(activitiesHaveNoSku!=null) {
            subActivities.removeAll(activitiesHaveNoSku);
        }
    }

}
