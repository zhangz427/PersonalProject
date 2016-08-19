package com.womai.m.mip.manager;

/**
 * Created by zheng.zhang on 2016/3/9.
 */
public class BaseManagerResponse<T> {

    public static String RESPONSE_SERVER_ERROR = "ServerError";
    public static String RESPONSE_SUCCESS = "Success";

    private String code = RESPONSE_SUCCESS;

    private String message = "success";

    private T t;

    public BaseManagerResponse(){}

    public BaseManagerResponse(T t){
        this.t = t;
    }

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

    public T getData() {
        return t;
    }

}
