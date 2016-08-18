package com.womai.m.mip.manager.geolocation.impl;


import com.womai.m.mip.common.utils.HttpClientUtil;
import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.geolocation.AddressInfo;
import com.womai.m.mip.manager.BaseManagerResponse;
import com.womai.m.mip.manager.geolocation.AddressManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by zheng.zhang on 2016/3/10.
 */
@Component("addressManager")
public class AddressManagerImpl implements AddressManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${baidu.geocoder.url}")
    private String geoCoderUrl;

    @Value("${baidu.geo.ak}")
    private String ak;

    @Override
    public BaseManagerResponse<AddressInfo> getAddressByCoordinate(String latitude, String longitude, String coordinateType) throws Exception {

        BaseManagerResponse<AddressInfo> response;

        Map<String, String> parameterMap = buildGetAddressRequestParameters(latitude, longitude, coordinateType);

        String responseBody = HttpClientUtil.get(geoCoderUrl, parameterMap);

        logger.info("Response body:{}", responseBody);

        if (isSuccess(responseBody)) {
            response = new BaseManagerResponse<AddressInfo>(convertToAddressInfo(responseBody));
        } else {
            response =new BaseManagerResponse<AddressInfo>();
            response.setCode(BaseManagerResponse.RESPONSE_SERVER_ERROR);
            response.setMessage("fail to get address");
        }

        return response;
    }

    private Map<String, String> buildGetAddressRequestParameters(String latitude, String longitude, String coordinateType) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("ak",randomAk());
        parameterMap.put("output", "json");
        parameterMap.put("coordtype", coordinateType);
        parameterMap.put("location", latitude + "," + longitude);
        return parameterMap;
    }

    private String randomAk() {
        String[] akArray = ak.split(";");
        Random r = new Random();
        int max = akArray.length;
        int randomIndex = r.nextInt(max);
        return akArray[randomIndex];
    }

    private boolean isSuccess(String responseBody) throws Exception{
        Map<String, Object> responseMap = JacksonUtil.toMap(responseBody);
        int status = (Integer)responseMap.get("status");
        if (status == 0) {
            return true;
        } {
            logger.error("fail to get address, responseBody:{}", responseBody);
            return false;
        }
    }

    private AddressInfo convertToAddressInfo(String responseBody) throws Exception {
        AddressInfo addressInfo = new AddressInfo();
        Map<String, Object> responseMap = JacksonUtil.toMap(responseBody);
        Map<String, Object> resultMap = (Map<String, Object>)responseMap.get("result");
        addressInfo.setAddress((String)resultMap.get("formatted_address"));
        Map<String, Object> addressComponentMap = (Map<String, Object>)resultMap.get("addressComponent");
        addressInfo.setCity((String)addressComponentMap.get("city"));
        addressInfo.setCountry((String) addressComponentMap.get("country"));
        addressInfo.setDistrict((String) addressComponentMap.get("district"));
        addressInfo.setProvince((String) addressComponentMap.get("province"));
        addressInfo.setStreet((String) addressComponentMap.get("street"));
        addressInfo.setStreetNumber((String) addressComponentMap.get("street_number"));
        return addressInfo;
    }

    public String getGeoCoderUrl() {
        return geoCoderUrl;
    }

    public void setGeoCoderUrl(String geoCoderUrl) {
        this.geoCoderUrl = geoCoderUrl;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }
}
