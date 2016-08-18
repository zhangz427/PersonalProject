package com.womai.m.mip.domain.geolocation;

/**
 * Created by zheng.zhang on 2016/3/17.
 */
public class HotspotInfo {

    private String cityId;

    private String cityName;

    private String mid;

    private double latitudeMin;

    private double latitudeMax;

    private double longitudeMin;

    private double longitudeMax;

    public HotspotInfo(String cityId,  String cityName, String mid, double latitudeMin, double latitudeMax, double longitudeMin, double longitudeMax) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.mid = mid;
        this.latitudeMin = latitudeMin;
        this.latitudeMax = latitudeMax;
        this.longitudeMin = longitudeMin;
        this.longitudeMax = longitudeMax;
    }

    public boolean match(double latitude, double longitude) {
        return (latitude < latitudeMax) && (latitude > latitudeMin) && (longitude < longitudeMax) && (longitude > longitudeMin);
    }

    public GeolocationInfo toGeolocationInfo() {
        GeolocationInfo geolocationInfo = new GeolocationInfo();
        geolocationInfo.setMid(this.mid);
        geolocationInfo.setCityId(this.cityId);
        geolocationInfo.setCityName(this.cityName);
        return geolocationInfo;
    }

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

    public double getLatitudeMin() {
        return latitudeMin;
    }

    public void setLatitudeMin(double latitudeMin) {
        this.latitudeMin = latitudeMin;
    }

    public double getLatitudeMax() {
        return latitudeMax;
    }

    public void setLatitudeMax(double latitudeMax) {
        this.latitudeMax = latitudeMax;
    }

    public double getLongitudeMin() {
        return longitudeMin;
    }

    public void setLongitudeMin(double longitudeMin) {
        this.longitudeMin = longitudeMin;
    }

    public double getLongitudeMax() {
        return longitudeMax;
    }

    public void setLongitudeMax(double longitudeMax) {
        this.longitudeMax = longitudeMax;
    }
}
