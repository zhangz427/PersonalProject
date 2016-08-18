package com.womai.m.mip.service.sso.impl;

import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.common.utils.MD5Util;
import com.womai.m.mip.common.utils.ThreeDES;
import com.womai.m.mip.domain.network.CommonData;
import com.womai.m.mip.domain.sso.LoginUser;
import com.womai.m.mip.domain.sso.UserInfo;
import com.womai.m.mip.manager.cache.CacheManager;
import com.womai.m.mip.manager.network.NetworkManager;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.sso.SSOService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/4/19.
 */
@Service("ssoService")
public class SSOServiceImpl implements SSOService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NetworkManager networkManager;

    @Autowired
    private CacheManager cacheManager;

    @Value("${login.url}")
    private String loginUrl;

    @Value("${userinfo.url}")
    private String userInfoUrl;

    @Value("${send.register.code.url}")
    private String sendRegisterCodeUrl;

    @Value("${fast.register.url}")
    private String fastRegisterUrl;

    @Value("${sso.cache.valid.time}")
    private long ssoCacheValidTime = 600000;

    @Override
    public BaseServiceResponse<UserInfo> login(String username, String password, String promotionId) throws Exception {

        BaseServiceResponse failResponse = null;
        Map<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("userName", username);
        loginParams.put("password", password);
        CommonData commonData = new CommonData();
        if (StringUtils.isNotBlank(promotionId)) {
            commonData.setPromotionId(promotionId);
        }

        String loginResponse = networkManager.requestData(loginUrl, commonData,loginParams);
        if (StringUtils.contains(loginResponse, "error")) {
            logger.error("fail to login: {}", loginResponse);
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            Map<String, Object> failResponseMap = JacksonUtil.toMap(loginResponse);
            Map<String, String> errorMap = (Map<String, String>)failResponseMap.get("error");
            failResponse.setMessage(errorMap.get("message"));
            return failResponse;
        }
        UserInfo userInfo = convertToUserInfo(loginResponse);
        return new BaseServiceResponse<UserInfo>(userInfo);
    }

    @Override
    public BaseServiceResponse sendRegisterCode(String mobilePhone) throws Exception {
        BaseServiceResponse failResponse = null;
        Map<String, String> sendRegisterCodeParams = new HashMap<String, String>();
        sendRegisterCodeParams.put("mobilePhone", mobilePhone);
        CommonData commonData = new CommonData();
        String sendRegisterCodeResponse = networkManager.requestData(sendRegisterCodeUrl, commonData,sendRegisterCodeParams);
        if (StringUtils.contains(sendRegisterCodeResponse, "error")) {
            logger.error("fail to send register code: {}", sendRegisterCodeResponse);
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            Map<String, Object> failResponseMap = JacksonUtil.toMap(sendRegisterCodeResponse);
            Map<String, String> errorMap = (Map<String, String>)failResponseMap.get("error");
            failResponse.setMessage(errorMap.get("message"));
            return failResponse;
        }
        Map<String, Object> sendRegisterCodeResponseMap = JacksonUtil.toMap(sendRegisterCodeResponse);
        String code = (String)sendRegisterCodeResponseMap.get("code");
        if (!"0".equals(code)) {
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            failResponse.setMessage((String)sendRegisterCodeResponseMap.get("msg"));
            return failResponse;
        }
        return new BaseServiceResponse();
    }

    @Override
    public BaseServiceResponse<UserInfo> fastRegister(String mobilePhone, String authCode, String oldUserTag, String promotionId) throws Exception {
        BaseServiceResponse failResponse = null;
        Map<String, String> registerParams = new HashMap<String, String>();
        registerParams.put("mobilePhone", mobilePhone);
        registerParams.put("authCode", authCode);
        if (StringUtils.isNotBlank(oldUserTag)) {
            registerParams.put("oldUserTag", oldUserTag);
        }
        CommonData commonData = new CommonData();
        if (StringUtils.isNotBlank(promotionId)) {
            commonData.setPromotionId(promotionId);
        }

        String registerResponse = networkManager.requestData(fastRegisterUrl, commonData, registerParams);
        if (StringUtils.contains(registerResponse, "error")) {
            logger.error("fail to register: {}", registerResponse);
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            Map<String, Object> failResponseMap = JacksonUtil.toMap(registerResponse);
            Map<String, String> errorMap = (Map<String, String>)failResponseMap.get("error");
            failResponse.setMessage(errorMap.get("message"));
            return failResponse;
        }
        UserInfo userInfo = convertToUserInfo(registerResponse);
        return new BaseServiceResponse<UserInfo>(userInfo);
    }

    private UserInfo convertToUserInfo(String loginResponse) throws Exception{
        LoginUser loginUser = JacksonUtil.toObject(loginResponse, LoginUser.class);

        logger.info("loginUserJson: {}", loginResponse);
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginData(loginUser);

        String encryptedKey =  MD5Util.md5(loginUser.getUserid() + System.currentTimeMillis());

        cacheManager.set("sso_" + encryptedKey, loginResponse, ssoCacheValidTime);
        userInfo.setUserKey(encryptedKey);
        return userInfo;
    }
}
