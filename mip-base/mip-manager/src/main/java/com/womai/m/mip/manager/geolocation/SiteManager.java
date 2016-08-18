package com.womai.m.mip.manager.geolocation;

import com.womai.m.mip.domain.geolocation.AddressInfo;
import com.womai.m.mip.domain.geolocation.GeolocationInfo;
import com.womai.m.mip.manager.BaseManagerResponse;

/**
 * Created by zheng.zhang on 2016/3/9.
 */
public interface SiteManager {

    public static final String RESPONSE_NONSUPPORT_ADDRESS = "non_support_address";

    public BaseManagerResponse<GeolocationInfo> getGeolocationInfoByAddress(AddressInfo addressInfo) throws Exception;

    public GeolocationInfo matchHotspot(String latitude, String longitude) throws Exception;
}
