package com.womai.m.mip.domain.sso;

/**
 * Created by zheng.zhang on 2016/4/19.
 */
public class UserInfo {

    private String userKey;

    private LoginUser loginData;

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public LoginUser getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginUser loginData) {
        this.loginData = loginData;
    }
}
