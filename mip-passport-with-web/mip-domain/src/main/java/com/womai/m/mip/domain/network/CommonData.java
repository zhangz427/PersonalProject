package com.womai.m.mip.domain.network;

public class CommonData {
    private String cityCode ="";

    private String userId = "";

    private String userSession = "";

    private int mid = 0;

    private String test1 = "";

    private String jssionId = "B000002D446D6D";

    private String height = "960";

    private String width = "640";

    private String unique="";

    private String level="";

    private String promotionId="";

    private String userIp="";

    private String os ="5";//5:H5环境，6:微信环境；5为默认值

    private String cpsKey = ""; //CPS 参数列表

    private String mipSourceId = "1003";

    public String  toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("userId:"+userId+";");
        sb.append("userSession:"+userSession+";");
        sb.append("mid:"+mid+";");
        sb.append("cityCode:"+cityCode+";");
        sb.append("test1:"+test1+";");
        sb.append("jssionId:"+jssionId+";");
        sb.append("height:"+height+";");
        sb.append("width:"+width+";");
        sb.append("unique:"+unique+";");
        sb.append("level:"+level+";");
        sb.append("promotionId:"+promotionId+";");
        sb.append("os:"+os+";");
        sb.append("userIp:"+userIp+";");
        sb.append("cpsKey:"+cpsKey+";");
        sb.append("mipSourceId:"+mipSourceId+";");
     return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSession() {
        return userSession;
    }

    public void setUserSession(String userSession) {
        this.userSession = userSession;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public String getJssionId() {
        return jssionId;
    }

    public void setJssionId(String jssionId) {
        this.jssionId = jssionId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) { this.promotionId = promotionId; }

    public String getOs() {
        return os;
    }

    public String getMipSourceId() {
        return mipSourceId;
    }

    public void setMipSourceId(String mipSourceId) {
        this.mipSourceId = mipSourceId;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getCpsKey() {
        return cpsKey;
    }

    public void setCpsKey(String cpsKey) {
        this.cpsKey = cpsKey;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
