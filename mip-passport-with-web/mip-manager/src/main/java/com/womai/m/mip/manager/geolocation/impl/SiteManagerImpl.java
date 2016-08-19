package com.womai.m.mip.manager.geolocation.impl;

import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.geolocation.AddressInfo;
import com.womai.m.mip.domain.geolocation.GeolocationInfo;
import com.womai.m.mip.domain.geolocation.HotspotInfo;
import com.womai.m.mip.manager.BaseManagerResponse;
import com.womai.m.mip.manager.geolocation.SiteManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/3/10.
 */
@Component("siteManager")
public class SiteManagerImpl implements SiteManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${site.data}")
    private String siteData;

    @Value("${hotspot.data}")
    private String hotspotData;

    private List<GeolocationInfo> geolocationInfoList = new ArrayList<GeolocationInfo>();

    private static List<String> descriptionWordList = new ArrayList<String>();

    private List<HotspotInfo> hotspotInfoList = new ArrayList<HotspotInfo>();

    static {


//        descriptionWordList.add("市");
//        descriptionWordList.add("自治州");
//        descriptionWordList.add("地区");
//        descriptionWordList.add("县");
    }

    @Override
    public BaseManagerResponse<GeolocationInfo> getGeolocationInfoByAddress(AddressInfo addressInfo) throws Exception {

        GeolocationInfo geolocationInfo = matchGeolocation(addressInfo);

        if (geolocationInfo != null) {
            return new BaseManagerResponse<GeolocationInfo>(geolocationInfo);
        } else {
            logger.info("Address is not supported: {}", addressInfo.getAddress());
            BaseManagerResponse failResponse = new BaseManagerResponse();
            failResponse.setCode(SiteManager.RESPONSE_NONSUPPORT_ADDRESS);
            failResponse.setMessage("Address is not supported");
            return failResponse;
        }

    }

    @Override
    public GeolocationInfo matchHotspot(String latitude, String longitude) {
        for (HotspotInfo hotspotInfo : hotspotInfoList) {
            if (hotspotInfo.match(Double.parseDouble(latitude), Double.parseDouble(longitude))) {
                GeolocationInfo matchedResult = hotspotInfo.toGeolocationInfo();
                logger.info("Match hotspot, latitude={},longitude={},result:{}", latitude, longitude, matchedResult.toString());
                return matchedResult;
            }
        }
        return null;
    }

    @PostConstruct
    private void parseSiteData() throws Exception {

        if (StringUtils.isNotBlank(siteData)) {
            geolocationInfoList = (List<GeolocationInfo>)JacksonUtil.toList(siteData, GeolocationInfo.class);

//            Map<String, Object> siteDataMap = JacksonUtil.toMap(siteData);
//            List<Map<String, Object>> dataList = (List<Map<String, Object>>) siteDataMap.get("data");
//            for (Map<String, Object> letterMap : dataList) {
//                List<Map<String, String>> siteList = (List<Map<String, String>>) letterMap.get("value");
//                for (Map<String, String> siteMap : siteList) {
//                    GeolocationInfo geolocationInfo = new GeolocationInfo();
//                    geolocationInfo.setCityName(siteMap.get("cityName"));
//                    geolocationInfo.setCityId(siteMap.get("cityId"));
//                    geolocationInfo.setMid(siteMap.get("mid"));
//                    geolocationInfoList.add(geolocationInfo);
//                }
//            }
        }

        if (StringUtils.isNotBlank(hotspotData)) {
            Map<String, Object> hotspotDataMap = JacksonUtil.toMap(hotspotData);
            List<Map<String, Object>> hotspotDataList = (List<Map<String, Object>>)hotspotDataMap.get("data");
            for (Map<String, Object> hotspotData : hotspotDataList) {
                String cityId = (String)hotspotData.get("cityId");
                String cityName = (String)hotspotData.get("cityName");
                String mid = (String)hotspotData.get("mid");
                Double latitudeMin = (Double)hotspotData.get("latitudeMin");
                Double latitudeMax = (Double)hotspotData.get("latitudeMax");
                Double longitudeMin = (Double)hotspotData.get("longitudeMin");
                Double longitudeMax = (Double)hotspotData.get("longitudeMax");
                HotspotInfo hotspotInfo = new HotspotInfo(cityId, cityName, mid, latitudeMin, latitudeMax, longitudeMin, longitudeMax);
                hotspotInfoList.add(hotspotInfo);
            }
        }
    }

    private GeolocationInfo matchGeolocation(AddressInfo addressInfo) {
        String provinceName = addressInfo.getProvince();
        String cityName = addressInfo.getCity();
        String districtName = addressInfo.getDistrict();

        if (StringUtils.isBlank(cityName) && StringUtils.isBlank(districtName)) {
            throw new RuntimeException("Fail to get location info");
        }

        //优先匹配城市关键字
        for (GeolocationInfo geolocationInfo : geolocationInfoList) {
            String cityIndex = filterIndex(geolocationInfo.getCityName());
            if (StringUtils.contains(cityName, cityIndex) && StringUtils.contains(provinceName, geolocationInfo.getProvinceName())) {
                return geolocationInfo;
            }
        }
        //城市关键字不存在，匹配地区关键字
        for (GeolocationInfo geolocationInfo : geolocationInfoList) {
            String districtIndex = filterIndex(geolocationInfo.getCityName());
            if (StringUtils.contains(districtName, districtIndex) && StringUtils.contains(provinceName, geolocationInfo.getProvinceName())) {
                return geolocationInfo;
            }
        }

        return null;
    }

    private String filterIndex(String name) {

        for (String descriptionWord : descriptionWordList) {
            if (StringUtils.endsWith(name, descriptionWord)) {
                String index = StringUtils.substring(name, 0, StringUtils.lastIndexOf(name, descriptionWord));
                return index;
            }
        }

        return name;
    }

}
