package com.womai.m.mip.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zheng.zhang on 2016/6/28.
 */
@Component("commonModelInterceptor")
public class CommonModelInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${passport.home.module}")
    private String passportHomeModule;

    @Value("${passport.m.home.module}")
    private String mobileHomeModule;

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {

        if (!response.isCommitted()) {
            if (modelAndView != null) {
                modelAndView.addObject("homeModule", passportHomeModule);
                modelAndView.addObject("mobileHomeModule", mobileHomeModule);
            }
        }
    }

}
