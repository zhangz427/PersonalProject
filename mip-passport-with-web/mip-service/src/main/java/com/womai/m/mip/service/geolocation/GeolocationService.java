package com.womai.m.mip.service.geolocation;

import com.womai.m.mip.domain.geolocation.GeolocationInfo;
import com.womai.m.mip.service.BaseServiceResponse;

/**
 * Created by zheng.zhang on 2016/3/8.
 */
public interface GeolocationService {

    public static final String RESPONSE_NONSUPPORT_ADDRESS = "non_support_address";

    public static final String COORDINATE_TYPE_STANDARD = "standard";
    public static final String COORDINATE_TYPE_ANDROID = "android";

    public BaseServiceResponse<GeolocationInfo> getGeolocation(String latitude, String longitude, String coordinateType) throws Exception;


    public void enableHotspot(boolean enableHotspot);

}
