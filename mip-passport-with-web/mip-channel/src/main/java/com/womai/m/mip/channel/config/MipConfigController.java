package com.womai.m.mip.channel.config;

import com.womai.m.mip.channel.BaseResponse;
import com.womai.m.mip.service.geolocation.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zheng.zhang on 2016/3/26.
 */
@Controller
@RequestMapping("/config")
public class MipConfigController {

    @Autowired
    private GeolocationService geolocationService;

    @ResponseBody
    @RequestMapping(value = "/enableHotspot",produces="application/json;charset=utf-8")
    public BaseResponse enableHotspot(HttpServletRequest request) {

        String enableHotspotStr = request.getParameter("enable");

        Boolean enableHotspot = Boolean.parseBoolean(enableHotspotStr);

        geolocationService.enableHotspot(enableHotspot);

        return new BaseResponse();
    }
}
