package com.womai.m.mip.manager.network.impl;

import com.womai.m.mip.common.utils.HttpClientUtil;
import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.common.utils.ThreeDES;
import com.womai.m.mip.domain.network.CommonData;
import com.womai.m.mip.manager.network.NetworkManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/4/19.
 */
@Component("networkManager")
public class NetworkManagerImpl implements NetworkManager {

    private static final Logger logger = LoggerFactory.getLogger(NetworkManagerImpl.class);

    @Value("${3DES.key}")
    private String threeDesKey = "qwertyuiopasdfghjkl;'zxc";

    @Value("${key.test}")
    private String keyTest = "mwomai01";

    @Override
    public String requestData(String url, CommonData commonData, Object paramsObject) throws Exception {
        long ctime = System.currentTimeMillis();
        logger.info("networkcurrentTime : "+ctime+"||url : "+ url +"||requestData : "+ trimPasswordParameter(paramsObject)+"||commonData : "+JacksonUtil.toJson(commonData));
        Map<String, String> paramsMap = buildRequestData(commonData, paramsObject);
        Map<String, String> headerMap = buildHearderMap(commonData);
        String response = HttpClientUtil.post(url, paramsMap, headerMap, 60000);
        String resultData = parseResultData(response);
        long duration = System.currentTimeMillis() - ctime;
        logger.info("duration:"+duration+"|networkcurrentTime : "+ctime+"||url : "+ url +"||responseData : "+ resultData);
        return resultData;
    }

    private String trimPasswordParameter(Object paramsObject) {
        if (paramsObject instanceof Map) {
            Map<String, String> paramMap = (Map<String, String>)paramsObject;
            Map<String, String> clonedMap = new HashMap<String, String>();
            for (String key : paramMap.keySet()) {
                if (StringUtils.contains(key.toLowerCase(), "password")) {
                    clonedMap.put(key, "encryptedPassword");
                } else {
                    clonedMap.put(key, paramMap.get(key));
                }
            }
            return JacksonUtil.toJson(clonedMap);
        } else {
            return JacksonUtil.toJson(paramsObject);
        }
    }

    private Map<String, String> buildRequestData(CommonData commonData, Object paramsObject) throws Exception {

        Map requestMap = new HashMap();
        requestMap.put("common", commonData);
        requestMap.put("data", paramsObject);
        String requestDataStr = JacksonUtil.toJson(requestMap);
        String encryptedRequestData = new String(Base64.encodeBase64(requestDataStr.getBytes()), "UTF-8");

        Map<String, String> requestDataMap = new HashMap<String, String>();
        requestDataMap.put("data", encryptedRequestData);
        return requestDataMap;
    }

    private Map<String, String> buildHearderMap(CommonData commonData) throws Exception {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("udid", commonData.getJssionId());
        headerMap.put("os",commonData.getOs());
        headerMap.put("osVersion", "2.2.3");
        headerMap.put("appVersion", "2.3.1");
        headerMap.put("ver", "1.0");
        headerMap.put("unique", commonData.getUnique());
        if(commonData.getPromotionId() != null && !"".equals(commonData.getPromotionId())){
            headerMap.put("promotionId", commonData.getPromotionId());
        }
        headerMap.put("sourceId", "2024");
        headerMap.put("test", keyTest);
        //分辨率要变化
        headerMap.put("height", commonData.getHeight());
        headerMap.put("width", commonData.getWidth());
        headerMap.put("userId", commonData.getUserId());
        headerMap.put("level", commonData.getLevel());
        headerMap.put("userIp", commonData.getUserIp());
        headerMap.put("time", System.currentTimeMillis()+"");
        headerMap.put("cpsKey", commonData.getCpsKey());
        headerMap.put("mipSourceId",commonData.getMipSourceId());
        //公共头key
        String hearder = JacksonUtil.toJson(headerMap);
        logger.info("request header : "+hearder);

        String encryptedHearder = ThreeDES.orginalEncoded(threeDesKey, hearder);
        Map<String, String> assembledHeaderMap = new HashMap<String, String>();
        assembledHeaderMap.put("headerData", encryptedHearder);
        return assembledHeaderMap;
    }

    private String parseResultData(String response) throws Exception {
        Map<String, Object> resMap = JacksonUtil.toMap(response);
        String resultCode = (String)resMap.get("code");
        String resultMessage = (String)resMap.get("message");
        String resultData = "";
        if ("00".equals(resultCode)) {
            resultData = new String(Base64.decodeBase64(resMap.get("data").toString().getBytes()), "UTF-8");
        } else {
            Map<String, String> errorMap = new HashMap<String, String>();
            errorMap.put("code", resultCode);
            errorMap.put("message", resultMessage);
            Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
            resultMap.put("error", errorMap);
            resultData = JacksonUtil.toJson(resultMap);
        }
        return resultData;
    }
}
