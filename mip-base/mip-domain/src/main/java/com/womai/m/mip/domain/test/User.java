package com.womai.m.mip.domain.test;

import java.io.Serializable;

/**
 * Created by zheng.zhang on 2016/6/15.
 */
public class User implements Serializable {

    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
