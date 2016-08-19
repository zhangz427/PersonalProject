package com.womai.m.mip.channel.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtil {
    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    public static String getCookieValue(HttpServletRequest servletRequest, String name) {

        Cookie[] cookies = servletRequest.getCookies();
        if(null==cookies)
            return "";
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(name)){
                try{
                    return URLDecoder.decode(cookie.getValue(), "UTF-8");
                }catch (Exception e){
                    logger.error("Fail to get cookie", e);
                    return "";
                }
            }
        }
        return "";
    }


    public static void addCookie(HttpServletResponse response,String name,String value, String domain, int maxAge) {
        addCookie(response, name, value, domain, maxAge, false);
    }

    public static void addCookie(HttpServletResponse response,String name,String value, String domain, int maxAge, boolean isSecure){
        try{
            if (isSecure) {
                addHttpOnlyAndSecureCookie(response, name, value, domain, maxAge);
            } else {
                Cookie cookie = new Cookie(name,value);
                cookie.setPath("/");
                cookie.setMaxAge(maxAge);
                if (StringUtils.isNotBlank(domain)) {
                    cookie.setDomain(domain);
                }
                cookie.setValue(URLEncoder.encode(value, "UTF-8"));
                response.addCookie(cookie);
            }

        }catch (Exception e){
            logger.error("Fail to add cookie",e);
        }
    }

    private static void addHttpOnlyAndSecureCookie(HttpServletResponse response, String name,String value, String domain, int maxAge) {
        //依次取得cookie中的名称、值、最大生存时间、路径、域和是否为安全协议信息
        StringBuffer strBufferCookie = new StringBuffer();
        strBufferCookie.append(name + "=" + value +  ";");
        if (maxAge >= 0) {
            strBufferCookie.append("Max-Age=" + maxAge + ";");
        }

        if(StringUtils.isNotBlank(domain)){
            strBufferCookie.append("domain=" + domain + ";");
        }
        strBufferCookie.append("path=/;");
        strBufferCookie.append("secure;HTTPOnly");
        response.addHeader("Set-Cookie",strBufferCookie.toString());
    }






}
