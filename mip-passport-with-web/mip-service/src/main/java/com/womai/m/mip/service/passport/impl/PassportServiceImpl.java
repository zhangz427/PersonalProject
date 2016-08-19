package com.womai.m.mip.service.passport.impl;

import com.womai.m.mip.common.utils.HttpClientUtil;
import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.common.utils.MD5Util;
import com.womai.m.mip.domain.network.CommonData;
import com.womai.m.mip.domain.passport.TicketInfo;
import com.womai.m.mip.domain.sso.LoginUser;
import com.womai.m.mip.manager.cache.CacheManager;
import com.womai.m.mip.manager.network.NetworkManager;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.passport.PassportService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zheng.zhang on 2016/5/23.
 */
@Service("passportService")
public class PassportServiceImpl implements PassportService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NetworkManager networkManager;

    @Autowired
    private CacheManager cacheManager;

    @Value("${wapi.base.url}")
    private String wapiBaseUrl;

    @Value("${login.url}")
    private String loginUrl;

    @Value("${userinfo.url}")
    private String userInfoUrl;

    @Value("${union.login.url}")
    private String unionLoginUrl;

    @Value("${send.register.code.url}")
    private String sendRegisterCodeUrl;

    @Value("${register.url}")
    private String registerUrl;

    @Value("${send.find.password.code.url}")
    private String sendFindPwdCodeUrl;

    @Value("${verify.find.password.code.url}")
    private String verifyFindPwdCodeUrl;

    @Value("${reset.password.url}")
    private String resetPasswordUrl;

    @Value("${find.password.by.mail.url}")
    private String findPwdByMailUrl;

    @Value("${no.pass.login.send.code.url}")
    private String noPassLoginSendCodeUrl;

    @Value("${no.pass.login.url}")
    private String noPassLoginUrl;

    @Value("${ticket.granting.cookie.valid.time}")
    private long ticketGrantingCookieValidTime;

    @Value("${sso.cache.valid.time}")
    private long serviceTokenValidTime;

    @Override
    public BaseServiceResponse<TicketInfo> login(String username, String password, String promotionId) throws Exception {
        Map<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("userName", username);
        loginParams.put("password", password);
        CommonData commonData = new CommonData();
        if (StringUtils.isNotBlank(promotionId)) {
            commonData.setPromotionId(promotionId);
        }
        String loginRequestUrl =  wapiBaseUrl + loginUrl;
        String loginResponse = networkManager.requestData(loginRequestUrl, commonData,loginParams);
        return returnLoginResponse(loginResponse);
    }

    @Override
    public TicketInfo checkLogin(String ticketGrantingCookie) throws Exception {
        String loginResponse = (String)cacheManager.get("sso_" + ticketGrantingCookie);
        if (StringUtils.isNotBlank(loginResponse)) {
            String serviceToken = generateServiceToken(ticketGrantingCookie);
            cacheManager.set("serviceToken_" + serviceToken, ticketGrantingCookie, serviceTokenValidTime);
            return new TicketInfo(ticketGrantingCookie, serviceToken);
        }
        return null;
    }

    @Override
    public LoginUser verifyLogin(String serviceToken) throws Exception {
        String ticketGrantingCookie = (String)cacheManager.get("serviceToken_" + serviceToken);
        if (StringUtils.isNotBlank(ticketGrantingCookie)) {
            cacheManager.del("serviceToken_" + serviceToken);
            String loginResponse = (String)cacheManager.get("sso_" + ticketGrantingCookie);
            String userInfoResponse = (String)cacheManager.get("sso_userinfo_" + ticketGrantingCookie);
            logger.info("Userinfo: {}", userInfoResponse);
            if (StringUtils.isNotBlank(loginResponse)) {
                LoginUser loginUser = JacksonUtil.toObject(loginResponse, LoginUser.class);
                return loginUser;
            }
        }
        return null;
    }

    @Override
    public void logout(String ticketGrantingCookie) throws Exception {
        cacheManager.del("sso_" + ticketGrantingCookie);
    }

    @Override
    public BaseServiceResponse<TicketInfo> unionLogin(String openId, String nickName, String gender, String type) throws Exception {
        String requestUrl = wapiBaseUrl + unionLoginUrl;
        Map<String,String> paramsMap = new HashMap<String, String>();
        paramsMap.put("type",type);
        paramsMap.put("openid",openId);
        paramsMap.put("nickName",nickName);
        paramsMap.put("gender",gender);
        CommonData commonData = new CommonData();
        String loginResponse = networkManager.requestData(requestUrl, commonData,paramsMap);
        return returnLoginResponse(loginResponse);
    }

    @Override
    public BaseServiceResponse sendRegisterCode(String mobilePhone) throws Exception {
        BaseServiceResponse failResponse = null;
        Map<String, String> sendRegisterCodeParams = new HashMap<String, String>();
        sendRegisterCodeParams.put("mobilePhone", mobilePhone);
        CommonData commonData = new CommonData();
        String requestUrl = wapiBaseUrl + sendRegisterCodeUrl;
        String sendRegisterCodeResponse = networkManager.requestData(requestUrl, commonData,sendRegisterCodeParams);
        return returnBaseResponse(sendRegisterCodeResponse);
    }

    @Override
    public BaseServiceResponse<TicketInfo> fastRegister(String mobilePhone, String authCode) throws Exception {
        BaseServiceResponse failResponse = null;
        Map<String, String> registerParams = new HashMap<String, String>();
        registerParams.put("mobilePhone", mobilePhone);
        registerParams.put("authCode", authCode);
        CommonData commonData = new CommonData();
        String requestUrl = wapiBaseUrl + registerUrl;
        String registerResponse = networkManager.requestData(requestUrl, commonData, registerParams);
        return returnLoginResponse(registerResponse);
    }

    @Override
    public BaseServiceResponse sendFindPwdCode(String mobilePhone) throws Exception {
        Map<String, String> sendRegisterCodeParams = new HashMap<String, String>();
        sendRegisterCodeParams.put("mobile", mobilePhone);
        CommonData commonData = new CommonData();
        String requestUrl = wapiBaseUrl + sendFindPwdCodeUrl;
        String sendChagnePasswordCodeResponse = networkManager.requestData(requestUrl, commonData,sendRegisterCodeParams);
        return returnBaseResponse(sendChagnePasswordCodeResponse);
    }

    @Override
    public BaseServiceResponse findPwdVerifyCode(String mobilePhone, String authCode) throws Exception {
        Map<String, String> findPwdVerifyCodeParams = new HashMap<String, String>();
        findPwdVerifyCodeParams.put("mobile", mobilePhone);
        findPwdVerifyCodeParams.put("code", authCode);
        CommonData commonData = new CommonData();
        String requestUrl = wapiBaseUrl + verifyFindPwdCodeUrl;
        String findPwdVerifyCodeResponse = networkManager.requestData(requestUrl, commonData,findPwdVerifyCodeParams);
        return returnBaseResponse(findPwdVerifyCodeResponse);
    }

    @Override
    public BaseServiceResponse resetPassword(String mobilePhone, String password, String verifyPassword) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile",mobilePhone);
        params.put("password",password);
        params.put("verifyPassword",verifyPassword);
        CommonData commonData = new CommonData();
        String requestUrl = wapiBaseUrl + resetPasswordUrl;
        String resetPasswordResponse = networkManager.requestData(requestUrl, commonData,params);
        return returnBaseResponse(resetPasswordResponse);
    }

    @Override
    public BaseServiceResponse findPwdByMail(String email) throws Exception {
        BaseServiceResponse failResponse = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("email",email);
        CommonData commonData = new CommonData();
        String requestUrl = wapiBaseUrl + findPwdByMailUrl;
        String findPwdByMailResponse = networkManager.requestData(requestUrl, commonData,params);
        return returnBaseResponse(findPwdByMailResponse);
    }

    @Override
    public BaseServiceResponse noPassLoginSendCode(String mobilePhone) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobilePhone);
        CommonData commonData = new CommonData();
        String requestUrl = wapiBaseUrl + noPassLoginSendCodeUrl;
        String noPassLoginSendCodeResponse = networkManager.requestData(requestUrl, commonData,params);
        return returnBaseResponse(noPassLoginSendCodeResponse);
    }

    @Override
    public BaseServiceResponse<TicketInfo> noPassLogin(String mobilePhone, String authCode) throws Exception {
        BaseServiceResponse failResponse = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobilePhone);
        params.put("authCode", authCode);
        CommonData commonData = new CommonData();
        String requestUrl = wapiBaseUrl + noPassLoginUrl;
        String noPassLoginResponse = networkManager.requestData(requestUrl, commonData, params);
        return returnLoginResponse(noPassLoginResponse);
    }

    private BaseServiceResponse<TicketInfo> returnLoginResponse(String response) throws Exception{
        BaseServiceResponse failResponse = null;
        if (StringUtils.contains(response, "error")) {
            logger.error("fail to login: {}", response);
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            Map<String, Object> failResponseMap = JacksonUtil.toMap(response);
            Map<String, String> errorMap = (Map<String, String>)failResponseMap.get("error");
            failResponse.setMessage(errorMap.get("message"));
            return failResponse;
        }
        String userInfoResponse = getUserInfo(response);
        TicketInfo ticketInfo = storeAndGetTicketInfo(response, userInfoResponse);
        return new BaseServiceResponse<TicketInfo>(ticketInfo);
    }

    private BaseServiceResponse returnBaseResponse(String response) throws Exception{
        BaseServiceResponse failResponse = null;
        if (StringUtils.contains(response, "error")) {
            logger.error("fail response: {}", response);
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            Map<String, Object> failResponseMap = JacksonUtil.toMap(response);
            Map<String, String> errorMap = (Map<String, String>)failResponseMap.get("error");
            failResponse.setMessage(errorMap.get("message"));
            return failResponse;
        }
        Map<String, Object> responseMap = JacksonUtil.toMap(response);
        String code = (String)responseMap.get("code");
        if (!"0".equals(code)) {
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            failResponse.setMessage((String)responseMap.get("msg"));
            return failResponse;
        }
        return new BaseServiceResponse();
    }

    private TicketInfo storeAndGetTicketInfo(String loginResponse, String userInfoResponse) throws Exception{
        LoginUser loginUser = JacksonUtil.toObject(loginResponse, LoginUser.class);
        logger.info("loginUserJson: {}", loginResponse);
        UUID uuid = UUID.randomUUID();
        String ticketGrantingCookie =  MD5Util.md5(loginUser.getUserid() + uuid.toString());
        cacheManager.set("sso_" + ticketGrantingCookie, loginResponse, ticketGrantingCookieValidTime);
        if (StringUtils.isNotBlank(userInfoResponse)) {
            cacheManager.set("sso_userinfo_" + ticketGrantingCookie, userInfoResponse, ticketGrantingCookieValidTime);
        }
        String serviceToken = generateServiceToken(ticketGrantingCookie);
        cacheManager.set("serviceToken_" + serviceToken, ticketGrantingCookie, serviceTokenValidTime);
        return new TicketInfo(ticketGrantingCookie, serviceToken);
    }

    private String generateServiceToken(String ticketGrantingCookie) {
        UUID uuid = UUID.randomUUID();
        return MD5Util.md5("serviceToken_" + ticketGrantingCookie + uuid.toString());
    }

    private String getUserInfo(String loginResponse) throws Exception {
        LoginUser loginUser = JacksonUtil.toObject(loginResponse, LoginUser.class);

        String userInfoRequestUrl = wapiBaseUrl + userInfoUrl;
        Map<String, String> userInfoParams = new HashMap<String, String>();
        userInfoParams.put("userId", loginUser.getUserid());
        userInfoParams.put("userSession", loginUser.getUsersession());

        CommonData commonData = new CommonData();
        commonData.setUserId(loginUser.getUserid());
        commonData.setUserSession(loginUser.getUsersession());
        commonData.setTest1(loginUser.getTest1());

        String userInfoResponse = networkManager.requestData(userInfoRequestUrl, commonData, userInfoParams);
        if (StringUtils.contains(userInfoResponse, "error")) {
            return null;
        }
        return userInfoResponse;
    }

}
