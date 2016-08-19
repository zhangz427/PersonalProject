package com.womai.m.mip.domain.sso;

/**
 * Created by zheng.zhang on 2016/4/19.
 */
public class LoginUser {

    /**
     * 用户id
     */
    private String userid = "";

    /**
     * 用户会话
     */
    private String usersession = "";

    /**
     * 用户等级
     */
    private String level = "";

    /**
     * 用户提示信息
     */
    private Sendcouponcard sendcouponcard = new Sendcouponcard();

    /**
     * 响应信息
     */
    private String response = "";

    /**
     * md5校验
     */
    private String test1 = "";

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsersession() {
        return usersession;
    }

    public void setUsersession(String usersession) {
        this.usersession = usersession;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Sendcouponcard getSendcouponcard() {
        return sendcouponcard;
    }

    public void setSendcouponcard(Sendcouponcard sendcouponcard) {
        this.sendcouponcard = sendcouponcard;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }
}
