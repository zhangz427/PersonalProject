package com.womai.m.mip.domain.test;

import java.io.Serializable;

/**
 * Created by zheng.zhang on 2016/6/15.
 */
public class UserInfo implements Serializable {

    private int id;

    private int age;

    private int gender;

    private String company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
