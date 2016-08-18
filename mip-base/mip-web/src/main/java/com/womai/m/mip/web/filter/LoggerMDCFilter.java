package com.womai.m.mip.web.filter;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/3/8.
 */
public class LoggerMDCFilter extends OncePerRequestFilter implements Filter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            MDC.put("timestamp", ((Long)System.currentTimeMillis()).toString());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }finally {
            MDC.clear();
        }
    }
}
