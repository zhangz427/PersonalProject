package com.womai.m.mip.channel.activity;

import com.womai.m.mip.channel.BaseController;
import com.womai.m.mip.channel.BaseGeneralResponse;
import com.womai.m.mip.channel.BaseResponse;
import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.activity.ActivityInfo;
import com.womai.m.mip.domain.sso.UserInfo;
import com.womai.m.mip.manager.cache.CacheManager;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.activity.ActivityService;
import com.womai.m.mip.service.sso.SSOService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/4/19.
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = "/productList",produces="application/json;charset=utf-8")
    public BaseResponse syncList(HttpServletRequest request) {
        try{

            String mipSourceId = decryptMipSource(request);
            if (!sourceValidate(mipSourceId)) {
                return createErrorResponse("同步活动列表失败");
            }
            String sid = request.getParameter("sid");
            if (StringUtils.isBlank(sid)) {
                logger.error("sid is null");
                return createErrorResponse("同步活动列表失败");
            }
            String cityCode = request.getParameter("cityCode");
            if (StringUtils.isBlank(cityCode)) {
                logger.error("cityCode is null");
                return createErrorResponse("同步活动列表失败");
            }
            String midStr = request.getParameter("mid");
            if (StringUtils.isBlank(midStr)) {
                logger.error("mid is null");
                return createErrorResponse("同步活动列表失败");
            }
            int mid = Integer.parseInt(midStr);

            logger.info("sync activity list request, sid: {}, mid:{}, cityCode:{}, mipSourceId:{}", sid, mid, cityCode, mipSourceId);
            BaseServiceResponse<ActivityInfo> syncListResponse = activityService.syncList(sid, mid, cityCode);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(syncListResponse.getCode())) {
                logger.info("sync activity list success: {}", JacksonUtil.toJson(syncListResponse.getData()));
                return new BaseGeneralResponse(syncListResponse.getData());
            } else {
                return createErrorResponse(syncListResponse.getMessage());
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            return createErrorResponse("System error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/cacheProductList",produces="application/json;charset=utf-8")
    public BaseResponse cacheActivityProductList(HttpServletRequest request) {
        try{

            String sid = request.getParameter("sid");
            if (StringUtils.isBlank(sid)) {
                logger.error("sid is null");
                return createErrorResponse("同步活动列表失败");
            }
            String cityCode = request.getParameter("cityCode");
            if (StringUtils.isBlank(cityCode)) {
                logger.error("cityCode is null");
                return createErrorResponse("同步活动列表失败");
            }
            String midStr = request.getParameter("mid");
            if (StringUtils.isBlank(midStr)) {
                logger.error("mid is null");
                return createErrorResponse("同步活动列表失败");
            }
            int mid = Integer.parseInt(midStr);

            logger.info("sync activity list request, sid: {}, mid:{}, cityCode:{}", sid, mid, cityCode);
            BaseServiceResponse<ActivityInfo> syncListResponse = activityService.syncList(sid, mid, cityCode);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(syncListResponse.getCode())) {
                logger.info("sync activity list success: {}", JacksonUtil.toJson(syncListResponse.getData()));
                BaseGeneralResponse successResponse = new BaseGeneralResponse(syncListResponse.getData());

                String cacheStr = JacksonUtil.toJson(successResponse);
                String cacheKey = "ActivityProductList_sid_" + sid + "_mid_" + mid;
                cacheManager.set(cacheKey, cacheStr);
                logger.info("cache activity product list: key={}, value={}", cacheKey, cacheStr);
                return new BaseGeneralResponse();
            } else {
                return createErrorResponse(syncListResponse.getMessage());
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            return createErrorResponse("System error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/cacheKxsActivityList",produces="application/json;charset=utf-8")
    public BaseResponse cacheKxsActivityList(HttpServletRequest request) {
        try{

            String sid = request.getParameter("sid");
            if (StringUtils.isBlank(sid)) {
                logger.error("sid is null");
                return createErrorResponse("获取活动列表失败");
            }
            String midStr = request.getParameter("mid");
            if (StringUtils.isBlank(midStr)) {
                logger.error("mid is null");
                return createErrorResponse("获取活动列表失败");
            }
            int mid = Integer.parseInt(midStr);

            logger.info("sync kxs activity list request, sid: {}, mid:{}", sid, mid);
            activityService.cacheKxsActivity(sid, mid);

            return new BaseGeneralResponse();

        } catch (Exception e) {
            logger.error("system error,", e);
            return createErrorResponse("System error");
        }
    }


}
