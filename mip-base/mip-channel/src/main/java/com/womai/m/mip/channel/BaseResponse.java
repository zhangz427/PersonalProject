package com.womai.m.mip.channel;

import java.io.Serializable;

/**
 * Created by zheng.zhang on 2016/3/7.
 */
public class BaseResponse implements Serializable {

    public static String RESPONSE_SUCCESS = "00";

    public static String RESPONSE_FAIL = "-91";

    private String code = RESPONSE_SUCCESS;

    private String message = "success";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
