package com.womai.m.mip.channel.passport;

import com.womai.m.mip.channel.passport.helper.OAuthHelper;
import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.common.utils.MD5Util;
import com.womai.m.mip.domain.passport.TicketInfo;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.channel.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/5/30.
 */
@Controller
@RequestMapping("/union")
public class UnionLoginController extends PassportController {

    @Autowired
    private OAuthHelper oAuthHelper;

    @Value("${qq.login.url}")
    private String qqLoginUrl;

    @Value("${sina.login.url}")
    private String sinaLoginUrl;

    @Value("${alipay.service}")
    private String alipayService;

    @Value("${alipay.partner}")
    private String alipayPartner;

    @Value("${alipay.target.service}")
    private String alipayTargetService;

    @Value("${alipay.input.charset}")
    private String alipayInputCharset;

    @Value("${alipay.sign.type}")
    private String alipaySignType;

    @Value("${alipay.return.url}")
    private String alipayReturnUrl;

    @Value("${alipay.request.url}")
    private String alipayRequestUrl;

    @Value("${alipay.key}")
    private String alipayKey;

    @RequestMapping(value = "/unionLoginList")
    public String unionLoginList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return "unionList";
    }

    @RequestMapping(value = "/qq/login")
    public void unionQQLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.sendRedirect(qqLoginUrl);
    }

    @RequestMapping(value = "/qq/callback")
    public String unionQQLoginCallback(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String code = request.getParameter("code");
        if (StringUtils.isBlank(code)) {
            return "error";
        }
        String accessToken = oAuthHelper.getQQAccessTokenByCode(HtmlUtils.htmlEscape(code));
        logger.info("accessToken: {}", accessToken);

        String openIdCallback = oAuthHelper.getQQOpenId(accessToken).replace("callback", "").replace("(", "").replace(");", "").trim();
        logger.info("openId callback: {}", openIdCallback);
        Map<String, Object> openIdCallbackMap = JacksonUtil.toMap(openIdCallback);
        String appId = (String)openIdCallbackMap.get("client_id");
        String openId = (String)openIdCallbackMap.get("openid");
        logger.info("appId:{}, openId:{}", appId, openId);
        String userInfoResponse = oAuthHelper.getQQUserInfo(openId, appId, accessToken);
        logger.info("userInfoResponse:{}", userInfoResponse);
        Map<String, Object> userInfoMap = JacksonUtil.toMap(userInfoResponse);
        String nickName = (String)userInfoMap.get("nickname");
        String gender = (String)userInfoMap.get("gender");

        String redirectUrl = CookieUtil.getCookieValue(request, "redirectUrl");
        BaseServiceResponse<TicketInfo> loginResponse = passportService.unionLogin(openId, nickName, gender, "96");

        if (BaseServiceResponse.RESPONSE_SUCCESS.equals(loginResponse.getCode())) {
            logger.info("Login success: {}", JacksonUtil.toJson(loginResponse.getData()));
            TicketInfo ticketInfo = loginResponse.getData();
            storeTGCAndRedirect(ticketInfo, response, redirectUrl);
            return "success";
        } else {
            return toLoginPage(null, response, model, loginResponse.getMessage());
        }
    }

    @RequestMapping(value = "/alipay/login")
    public void unionAlipayLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", alipayService);
        sParaTemp.put("partner", alipayPartner);
        sParaTemp.put("_input_charset", alipayInputCharset);
        sParaTemp.put("target_service", alipayTargetService);
        sParaTemp.put("return_url", alipayReturnUrl);

        String prestr = oAuthHelper.alipayCreateLinkString(sParaTemp);
        String signStr = prestr + alipayKey;
        String mysign = MD5Util.md5(signStr).toLowerCase();
        String reqAlipay = alipayRequestUrl + "?" + prestr + "&sign=" + mysign;
        logger.info("reqAlipay : " + reqAlipay);
        response.sendRedirect(reqAlipay);
    }

    @RequestMapping(value = "/alipay/callback")
    public String unionAlipayLoginCallback(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Object name : requestParams.keySet()) {
            String[] valueStr = (String[])(requestParams.get(name));
            params.put((String)name, valueStr[0]);
        }
        logger.info("Callback parameter: {}", params);
        String openId = request.getParameter("user_id");
        String isSuccess = request.getParameter("is_success");
        String nickName = request.getParameter("real_name");

        boolean verify_result = oAuthHelper.alipayVerify(params);
        if (!verify_result) {
            return toLoginPage(null, response, model, "illegal sign");
        }

        if("T".equals(isSuccess)){
            String gender = "男";
            BaseServiceResponse<TicketInfo> loginResponse = passportService.unionLogin(openId, nickName, gender, "93");
            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(loginResponse.getCode())) {
                logger.info("Login success: {}", JacksonUtil.toJson(loginResponse.getData()));
                TicketInfo ticketInfo = loginResponse.getData();
                String redirectUrl = CookieUtil.getCookieValue(request, "redirectUrl");
                storeTGCAndRedirect(ticketInfo, response, redirectUrl);
                return "success";
            } else {
                return toLoginPage(null, response, model, loginResponse.getMessage());
            }
        }
        return toLoginPage(null, response, model, "fail to login");
    }

    @RequestMapping(value = "/sina/login")
    public void unionSinaLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(sinaLoginUrl);
    }

    @RequestMapping(value = "/sina/callback")
    public String unionSinaLoginCallback(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        String code = request.getParameter("code");
        logger.info("Sina login code is {}", code);
        String accessTokenData = oAuthHelper.getSinaAccessTokenData(HtmlUtils.htmlEscape(code));
        logger.info("Sina login accessTokenData is {}", accessTokenData);
        Map<String, Object> accessTokenDataMap = JacksonUtil.toMap(accessTokenData);
        String accessToken = (String)accessTokenDataMap.get("access_token");
        String uid = (String)accessTokenDataMap.get("uid");

        String userInfoData = oAuthHelper.getSinaUserInfo(accessToken, uid);
        logger.info("Sina login user info: {}", userInfoData);
        Map<String, Object> userInfoDataMap = JacksonUtil.toMap(userInfoData);
        String gender = "f".equals(userInfoDataMap.get("gender")) ? "女":"男";
        String openId = Integer.toString((Integer)userInfoDataMap.get("id"));
        String nickName = (String)userInfoDataMap.get("name");

        BaseServiceResponse<TicketInfo> loginResponse = passportService.unionLogin(openId, nickName, gender, "95");
        if (BaseServiceResponse.RESPONSE_SUCCESS.equals(loginResponse.getCode())) {
            logger.info("Login success: {}", JacksonUtil.toJson(loginResponse.getData()));
            TicketInfo ticketInfo = loginResponse.getData();
            String redirectUrl = CookieUtil.getCookieValue(request, "redirectUrl");
            storeTGCAndRedirect(ticketInfo, response, redirectUrl);
            return "success";
        } else {
            return toLoginPage(null, response, model, loginResponse.getMessage());
        }
    }
}
