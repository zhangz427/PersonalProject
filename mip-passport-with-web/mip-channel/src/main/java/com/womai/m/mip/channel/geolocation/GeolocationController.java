package com.womai.m.mip.channel.geolocation;



import com.womai.m.mip.channel.BaseController;

import com.womai.m.mip.channel.BaseGeneralResponse;
import com.womai.m.mip.channel.BaseResponse;
import com.womai.m.mip.common.utils.ThreeDES;
import com.womai.m.mip.domain.geolocation.GeolocationInfo;

import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.geolocation.GeolocationService;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zheng.zhang on 2016/3/4.
 */
@Controller
@RequestMapping("/geo")
public class GeolocationController extends BaseController {

    public static final String RESPONSE_NONSUPPORT_ADDRESS = "-92";

    @Autowired
    private GeolocationService geolocationService;

    @ResponseBody
    @RequestMapping(value = "/getGeolocation",produces="application/json;charset=utf-8")
    public BaseResponse getGeolocation(HttpServletRequest request) {

        try{
            String encryptedRequestData = request.getParameter("data");
            logger.debug("encryptedRequestData:{}", encryptedRequestData);
            if (StringUtils.isBlank(encryptedRequestData)) {
                logger.error("request data is null");
                return convertToBaseEncryptResponse(createErrorResponse("定位失败"));
            }

            String headerEncryptedString = request.getHeader("headerData");
            logger.debug("headerEncryptedString:{}", headerEncryptedString);
            if (StringUtils.isBlank(headerEncryptedString)) {
                logger.error("header data is null");
                return convertToBaseEncryptResponse(createErrorResponse("定位失败"));
            }

            Map<String, Object> requestMap = decryptRequestData(encryptedRequestData);

            Map<String, Object> headerMap = decryptHeaderData(headerEncryptedString);

            logger.debug("Request data:{}", requestMap);

            logger.debug("Header data:{}", headerMap);

            String latitude = (String)requestMap.get("latitude");
            if (StringUtils.isBlank(latitude)) {
                logger.error("latitude is null in request data");
                return convertToBaseEncryptResponse(createErrorResponse("定位失败"));
            }

            String longitude = (String)requestMap.get("longitude");
            if (StringUtils.isBlank(longitude)) {
                logger.error("longitude is null in request data");
                return convertToBaseEncryptResponse(createErrorResponse("定位失败"));
            }

            String mipSourceId = (String)headerMap.get("mipSourceId");
            if (!sourceValidate(mipSourceId)) {
                return convertToBaseEncryptResponse(createErrorResponse("定位失败"));
            }

            return convertToBaseEncryptResponse(getGeolocation(latitude, longitude, mipSourceId));
        } catch (Exception e) {
            logger.error("system error,", e);
            return convertToBaseEncryptResponse(createErrorResponse("定位失败"));
        }

    }

    @ResponseBody
    @RequestMapping(value = "/getLocation",produces="application/json;charset=utf-8")
    public BaseResponse getLocation(HttpServletRequest request) {
        try{

            String latitude = request.getParameter("latitude");
            if (StringUtils.isBlank(latitude)) {
                logger.error("latitude is null in request data");
                return createErrorResponse("定位失败");
            }

            String longitude = request.getParameter("longitude");
            if (StringUtils.isBlank(longitude)) {
                logger.error("longitude is null in request data");
                return createErrorResponse("定位失败");
            }

            String mipSourceId = decryptMipSource(request);
            if (!sourceValidate(mipSourceId)) {
                return convertToBaseEncryptResponse(createErrorResponse("定位失败"));
            }

            return getGeolocation(latitude, longitude, mipSourceId);
        } catch (Exception e) {
            logger.error("system error,", e);
            return createErrorResponse("定位失败");
        }
    }

    private BaseResponse getGeolocation(String latitude,String longitude, String mipSourceId) {
        try {

            //Android未获取经纬度情况下传递
            if("1002".equals(mipSourceId)) {
                if ("4.9E-324".equals(latitude) && "4.9E-324".equals(longitude)) {
                    logger.error("Android invalid input");
                    return createErrorResponse("定位失败");
                }
            }

            String coordinateType = getCoordinateType(mipSourceId);
            BaseServiceResponse<GeolocationInfo> geolocationServiceResponse = geolocationService.getGeolocation(latitude, longitude, coordinateType);
            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(geolocationServiceResponse.getCode())) {
                logger.info("Get geolocation info mipSourceId={},latitude={},longitude={},result:{}", mipSourceId, latitude, longitude,geolocationServiceResponse.getData().toString());
                return new BaseGeneralResponse(convertGeolocationInfoToMap(geolocationServiceResponse.getData()));
            } else if (GeolocationService.RESPONSE_NONSUPPORT_ADDRESS.equals(geolocationServiceResponse.getCode())) {
                logger.info("Address is not supported mipSourceId={},latitude={},longitude={}", mipSourceId, latitude, longitude);
                BaseResponse response = new BaseResponse();
                response.setCode(RESPONSE_NONSUPPORT_ADDRESS);
                response.setMessage("当前位置未开通站点");
                return response;
            } else {
                logger.error(geolocationServiceResponse.getMessage());
                return createErrorResponse("定位失败");
            }
        } catch (Exception e) {
            logger.error("system error,", e);
            return createErrorResponse("定位失败");
        }
    }

    private Map<String, String> convertGeolocationInfoToMap(GeolocationInfo geolocationInfo) {
        Map<String, String> infoMap = new HashMap<String, String>();
        infoMap.put("cityId", geolocationInfo.getCityId());
        infoMap.put("cityName", geolocationInfo.getCityName());
        infoMap.put("mid", geolocationInfo.getMid());
        return infoMap;
    }


    private String getCoordinateType(String osCode) {

        if ("1002".equals(osCode)) {
            return GeolocationService.COORDINATE_TYPE_ANDROID;
        } else {
            return GeolocationService.COORDINATE_TYPE_STANDARD;
        }

    }


}
