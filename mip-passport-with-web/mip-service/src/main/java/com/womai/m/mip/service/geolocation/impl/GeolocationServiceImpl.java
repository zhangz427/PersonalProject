package com.womai.m.mip.service.geolocation.impl;

import com.womai.m.mip.domain.geolocation.AddressInfo;
import com.womai.m.mip.domain.geolocation.GeolocationInfo;
import com.womai.m.mip.domain.geolocation.HotspotInfo;
import com.womai.m.mip.manager.BaseManagerResponse;
import com.womai.m.mip.manager.geolocation.AddressManager;
import com.womai.m.mip.manager.geolocation.SiteManager;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.geolocation.GeolocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/3/8.
 */
@Service("geolocationService")
public class GeolocationServiceImpl implements GeolocationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Map<String, String> coordinateTypeMap = new HashMap<String, String>();

    private boolean enableHotspot = false;

    @Autowired
    private AddressManager addressManager;

    @Autowired
    private SiteManager siteManager;

    static {
        coordinateTypeMap.put(GeolocationService.COORDINATE_TYPE_STANDARD, AddressManager.COORDINATE_TYPE_STANDARD);
        coordinateTypeMap.put(GeolocationService.COORDINATE_TYPE_ANDROID, AddressManager.COORDINATE_TYPE_ANDROID);

    }

    @Override
    public BaseServiceResponse<GeolocationInfo> getGeolocation(String latitude, String longitude, String coordinateType) throws Exception {

        logger.info("latitude:{}, longitude:{}, coordinateType:{}", latitude, longitude, coordinateType);

        GeolocationInfo hotspotResult = null;

        if (enableHotspot) {
            hotspotResult = siteManager.matchHotspot(latitude, longitude);
        }

        if (hotspotResult != null) {
            return new BaseServiceResponse<GeolocationInfo>(hotspotResult);
        }

        BaseServiceResponse failResponse;

        BaseManagerResponse<AddressInfo> getAddressResponse = addressManager.getAddressByCoordinate(latitude, longitude, coordinateTypeMap.get(coordinateType));

        if (!BaseManagerResponse.RESPONSE_SUCCESS.equals(getAddressResponse.getCode())) {
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            failResponse.setMessage("fail to get address.");
            return failResponse;
        }

        BaseManagerResponse<GeolocationInfo> getSiteInfoResponse = siteManager.getGeolocationInfoByAddress(getAddressResponse.getData());

        if (SiteManager.RESPONSE_NONSUPPORT_ADDRESS.equals(getSiteInfoResponse.getCode())) {
            failResponse = new BaseServiceResponse();
            failResponse.setCode(GeolocationService.RESPONSE_NONSUPPORT_ADDRESS);
            failResponse.setMessage("address is not supported");
            return failResponse;
        } else if (!BaseManagerResponse.RESPONSE_SUCCESS.equals(getAddressResponse.getCode())) {
            failResponse = new BaseServiceResponse();
            failResponse.setCode(BaseServiceResponse.RESPONSE_SERVER_ERROR);
            failResponse.setMessage("fail to get site info.");
            return failResponse;
        }

        return new BaseServiceResponse<GeolocationInfo>(getSiteInfoResponse.getData());

    }

    @Override
    public void enableHotspot(boolean enableHotspot) {
        logger.info("enable hotspot {}", enableHotspot);
        this.enableHotspot = enableHotspot;
    }
}
