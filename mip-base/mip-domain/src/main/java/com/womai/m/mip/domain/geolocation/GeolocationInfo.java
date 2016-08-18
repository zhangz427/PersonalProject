package com.womai.m.mip.domain.geolocation;


/**
 * Created by zheng.zhang on 2016/3/7.
 */
public class GeolocationInfo {

    private String provinceId;

    private String provinceName;

    private String cityId;

    private String cityName;

    private String mid;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder()
                            .append("provinceId=").append(provinceId)
                            .append(",provinceName=").append(provinceName)
                            .append(",cityId=").append(cityId)
                            .append(",cityName=").append(cityName)
                            .append(",mid=").append(mid);
        return sb.toString();
    }
}
