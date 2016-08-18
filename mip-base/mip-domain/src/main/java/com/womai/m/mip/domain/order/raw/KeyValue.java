package com.womai.m.mip.domain.order.raw;

import java.io.Serializable;

/**
 * 金额
 *
 */
public class KeyValue implements Serializable {

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 值
     */
    private String value = "";

    /**
     * 主键
     */
    private String key = "";

}
