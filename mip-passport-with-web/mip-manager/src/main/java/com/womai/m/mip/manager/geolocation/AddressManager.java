package com.womai.m.mip.manager.geolocation;

import com.womai.m.mip.domain.geolocation.AddressInfo;
import com.womai.m.mip.manager.BaseManagerResponse;

/**
 * Created by zheng.zhang on 2016/3/9.
 */
public interface AddressManager {

    public static final String COORDINATE_TYPE_ANDROID = "gcj02ll";
    public static final String COORDINATE_TYPE_STANDARD = "wgs84ll";

    public BaseManagerResponse<AddressInfo> getAddressByCoordinate(String latitude, String longitude, String coordinateType) throws Exception;

}
