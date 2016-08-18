package com.womai.m.mip.channel.sso;

import com.womai.m.mip.channel.BaseController;
import com.womai.m.mip.channel.BaseGeneralResponse;
import com.womai.m.mip.channel.BaseResponse;

import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.sso.UserInfo;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.sso.SSOService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/sso")
public class SSOController extends BaseController {

    @Autowired
    private SSOService ssoService;

    @ResponseBody
    @RequestMapping(value = "/login",produces="application/json;charset=utf-8")
    public BaseResponse login(HttpServletRequest request) {
        try{
            String username = request.getParameter("username");
            if (StringUtils.isBlank(username)) {
                logger.error("username is null");
                return createErrorResponse("登录失败");
            }
            String password = request.getParameter("password");
            if (StringUtils.isBlank(password)) {
                logger.error("password is null");
                return createErrorResponse("登录失败");
            }

            String promotionId = request.getParameter("promotionId");

            String mipSourceId = decryptMipSource(request);
            if (!sourceValidate(mipSourceId)) {
                return createErrorResponse("登录失败");
            }

            logger.info("login request, username: {}, mipSourceId:{}, promotionId:{}", username, mipSourceId, promotionId);
            BaseServiceResponse<UserInfo> loginResponse = ssoService.login(username, password, promotionId);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(loginResponse.getCode())) {
                logger.info("Login success: {}", JacksonUtil.toJson(loginResponse.getData()));
                return new BaseGeneralResponse(convertUserInfoToMap(loginResponse.getData()));
            } else {
                return createErrorResponse(loginResponse.getMessage());
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            return createErrorResponse("System error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sendRegisterCode",produces="application/json;charset=utf-8")
    public BaseResponse sendRegisterCode(HttpServletRequest request) {
        try{
            String mobileNumber = request.getParameter("mobileNumber");
            if (StringUtils.isBlank(mobileNumber)) {
                logger.error("mobileNumber is null");
                return createErrorResponse("发送验证码失败");
            }
            String mipSourceId = decryptMipSource(request);
            if (!sourceValidate(mipSourceId)) {
                return createErrorResponse("发送验证码失败");
            }
            logger.info("Send register code for mobile: {}, mipSourceId:{}", mobileNumber, mipSourceId);
            BaseServiceResponse senRegisterCodeResponse = ssoService.sendRegisterCode(mobileNumber);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(senRegisterCodeResponse.getCode())) {
                logger.info("Send register code Successfully");
                return new BaseGeneralResponse();
            } else {
                return createErrorResponse(senRegisterCodeResponse.getMessage());
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            return createErrorResponse("System error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/fastRegister",produces="application/json;charset=utf-8")
    public BaseResponse fastRegister(HttpServletRequest request) {
        try{
            String mobileNumber = request.getParameter("mobileNumber");
            if (StringUtils.isBlank(mobileNumber)) {
                logger.error("mobileNumber is null");
                return createErrorResponse("注册失败");
            }
            String mipSourceId = decryptMipSource(request);
            if (!sourceValidate(mipSourceId)) {
                return createErrorResponse("注册失败");
            }
            String authCode = request.getParameter("authCode");
            if (StringUtils.isBlank(authCode)) {
                logger.error("authCode is null");
                return createErrorResponse("注册失败");
            }
            String oldUserTag = request.getParameter("oldUserTag");

            String promotionId = request.getParameter("promotionId");

            logger.info("Register request mobileNumber: {}, mipSourceId:{}, oldUserTag:{}, promotionId:{}", mobileNumber, mipSourceId, oldUserTag, promotionId);
            BaseServiceResponse<UserInfo> registerResponse = ssoService.fastRegister(mobileNumber, authCode, oldUserTag, promotionId);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(registerResponse.getCode())) {
                logger.info("Register success: {}", JacksonUtil.toJson(registerResponse.getData()));
                return new BaseGeneralResponse(convertUserInfoToMap(registerResponse.getData()));
            } else {
                return createErrorResponse(registerResponse.getMessage());
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            return createErrorResponse("System error");
        }
    }


    private Map<String, String> convertUserInfoToMap(UserInfo userContext) {
        Map<String, String> infoMap = new HashMap<String, String>();
        infoMap.put("ssotoken", userContext.getUserKey());
        infoMap.put("userId", userContext.getLoginData().getUserid());
        infoMap.put("userSession", userContext.getLoginData().getUsersession());
        return infoMap;
    }


}
