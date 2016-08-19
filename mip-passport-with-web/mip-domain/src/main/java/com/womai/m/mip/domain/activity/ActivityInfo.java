package com.womai.m.mip.domain.activity;


import com.womai.m.mip.domain.ResError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityInfo implements Serializable {
    /**
     * 活动标题
     */
    private String title;
    /**
     * 响应信息
     */
    private String response = "";

    /**
     * 错误信息
     */
    private ResError error;
    /**
     * 活动id
     */
    private long sid;
    /**
     * 站点
     */
    private int mid;
    /**
     * 子标题
     */
    private List<SubActivity> subActivity = new ArrayList<SubActivity>();

    private Map<Long,Sku> skulist = new HashMap<Long,Sku>();


    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public List<SubActivity> getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(List<SubActivity> subActivity) {
        this.subActivity = subActivity;
    }

    public Map<Long, Sku> getSkulist() {
        return skulist;
    }

    public void setSkulist(Map<Long, Sku> skulist) {
        this.skulist = skulist;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ResError getError() {
        return error;
    }

    public void setError(ResError error) {
        this.error = error;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
