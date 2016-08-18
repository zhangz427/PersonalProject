package com.womai.m.mip.manager.network;

import com.womai.m.mip.domain.network.CommonData;


/**
 * Created by zheng.zhang on 2016/4/19.
 */
public interface NetworkManager {

    public String requestData(String url,CommonData commonData,Object params) throws Exception;

}
