package com.womai.m.mip.web.filter;

import com.womai.common.framework.web.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 访问时间记录过滤器
 *
 * @author <a href="mailto:winstonvip@gmail.com">winston</a>
 * @version Created by IntelliJ IDEA.
 *          Date: 2010-5-4 18:22:37
 * @since 1.0
 */
public class TimerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TimerFilter.class);

    /**
     * 对于执行时间超过指定毫秒，日志级别为error <br/>
     * 单位：毫秒 <br/>
     * 默认值：200 <br/>
     */
    private int error = 3000;
    /**
     * 对于执行时间超过指定毫秒，日志级别为warn <br/>
     * 单位：毫秒 <br/>
     * 默认值：100 <br/>
     */
    private int warn = 2000;
    /**
     * 对于执行时间超过指定毫秒，日志级别为info <br/>
     * 单位：毫秒 <br/>
     * 默认值：50 <br/>
     */
    private int info = 1000;


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long procTime = System.currentTimeMillis() - startTime;
        //执行方法以后， 记录“当前使用的方法名称”和“执行时间”
        String msg = "Slow request url: " + getRequestURL((HttpServletRequest)request) +
                " - Time to execute request:  " + procTime + " milliseconds!";

        if (procTime > info) {
            logger.info(msg);
        }

    }

    private String getRequestURL(HttpServletRequest request){
        final StringBuilder buffer = new StringBuilder();
        buffer.append(request.getRequestURL().toString());
        return   buffer.toString();
    }

    public void destroy() {

    }
}
