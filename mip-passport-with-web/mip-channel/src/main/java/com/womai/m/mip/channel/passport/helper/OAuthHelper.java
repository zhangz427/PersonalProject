package com.womai.m.mip.channel.passport.helper;

import com.womai.m.mip.common.utils.HttpClientUtil;
import com.womai.m.mip.common.utils.MD5Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by zheng.zhang on 2016/5/30.
 */
@Component
public class OAuthHelper {

    @Value("${qq.client.id}")
    private String qqClientId;

    @Value("${qq.client.secret}")
    private String qqClientSecret;

    @Value("${qq.grant.type}")
    private String qqGrantType;

    @Value("${qq.redirect.url}")
    private String qqRedirectUrl;

    @Value("${qq.accesstoken.url}")
    private String qqAccesstokenUrl;

    @Value("${qq.openid.url}")
    private String qqOpenIdUrl;

    @Value("${qq.userinfo.url}")
    private String qqUserInfoUrl;

    @Value("${sina.client.id}")
    private String sinaClientId;

    @Value("${sina.client.secret}")
    private String sinaClientSecret;

    @Value("${sina.grant.type}")
    private String sinaGrantType;

    @Value("${sina.redirect.url}")
    private String sinaRedirectUrl;

    @Value("${sina.accesstoken.url}")
    private String sinaAccesstokenUrl;

    @Value("${sina.userinfo.url}")
    private String sinaUserInfoUrl;

    @Value("${alipay.notify.verify.url}")
    private String alipayNotifyVerifyUrl;

    @Value("${alipay.key}")
    private String alipayKey;

    @Value("${alipay.partner}")
    private String alipayPartner;

    public String getQQAccessTokenByCode(String code) throws Exception{
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("client_id", qqClientId);
        parameterMap.put("client_secret", qqClientSecret);
        parameterMap.put("grant_type", qqGrantType);
        parameterMap.put("code", code);
        parameterMap.put("redirect_uri", qqRedirectUrl);
        String accessTokenResponse = HttpClientUtil.post(qqAccesstokenUrl, parameterMap);
        String accessToken = accessTokenResponse.split("&")[0].split("=")[1];
        return accessToken;
    }

    public String getQQOpenId(String token) throws Exception {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("access_token", token);
        String response = HttpClientUtil.post(qqOpenIdUrl, parameterMap);
        return response;
    }

    public String getQQUserInfo(String openId,String appId,String token) throws Exception {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("access_token", token);
        parameterMap.put("openid", openId);
        parameterMap.put("oauth_consumer_key", appId);
        return HttpClientUtil.get(qqUserInfoUrl, parameterMap);
    }

    public String getSinaAccessTokenData(String code) throws Exception {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("client_id", sinaClientId);
        parameterMap.put("client_secret", sinaClientSecret);
        parameterMap.put("grant_type", sinaGrantType);
        parameterMap.put("code", code);
        parameterMap.put("redirect_uri", sinaRedirectUrl);
        return HttpClientUtil.post(sinaAccesstokenUrl, parameterMap);
    }

    public String getSinaUserInfo(String accessToken, String uid) throws Exception {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("uid", uid);
        parameterMap.put("access_token", accessToken);
        return HttpClientUtil.get(sinaUserInfoUrl, parameterMap);
    }

    public Map<String, String> alipayParaFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    public String alipayCreateLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public boolean alipayVerify(Map<String, String> params) {

        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String responseTxt = "true";
        if(params.get("notify_id") != null) {
            String notify_id = params.get("notify_id");
            responseTxt = alipayVerifyResponse(notify_id);
        }
        String sign = "";
        if(params.get("sign") != null) {sign = params.get("sign");}
        boolean isSign = alipayGetSignVeryfy(params, sign);

        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean alipayGetSignVeryfy(Map<String, String> Params, String sign) {
        //过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = alipayParaFilter(Params);
        //获取待签名字符串
        String preSignStr = alipayCreateLinkString(sParaNew);
        String signStr = preSignStr + alipayKey;
        String newSign = MD5Util.md5(signStr).toLowerCase();
        if (newSign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    private String alipayVerifyResponse(String notify_id) {
        try {
            String veryfy_url = alipayNotifyVerifyUrl + "partner=" + alipayPartner + "&notify_id=" + notify_id;
            return HttpClientUtil.get(veryfy_url, null);
        } catch (Exception e) {
            return "";
        }
    }

}
