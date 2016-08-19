package com.womai.m.mip.channel;

/**
 * Created by zheng.zhang on 2016/3/24.
 */
public class BaseGeneralResponse extends BaseResponse {

    private Object data;

    public BaseGeneralResponse() {}

    public BaseGeneralResponse(Object data){
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
