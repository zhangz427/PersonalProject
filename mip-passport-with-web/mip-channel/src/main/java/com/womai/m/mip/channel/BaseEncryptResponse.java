package com.womai.m.mip.channel;

/**
 * Created by zheng.zhang on 2016/3/8.
 */
public class BaseEncryptResponse extends BaseResponse {

    public BaseEncryptResponse(){}

    public BaseEncryptResponse(String data) {
        this.data = data;
    }

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
