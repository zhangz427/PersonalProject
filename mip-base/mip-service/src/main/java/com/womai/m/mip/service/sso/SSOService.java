package com.womai.m.mip.service.sso;


import com.womai.m.mip.domain.sso.UserInfo;
import com.womai.m.mip.service.BaseServiceResponse;

/**
 * Created by zheng.zhang on 2016/4/19.
 */
public interface SSOService {

    public BaseServiceResponse<UserInfo> login(String username, String password, String promotionId) throws Exception;

    public BaseServiceResponse sendRegisterCode(String mobilePhone) throws Exception;

    public BaseServiceResponse<UserInfo> fastRegister(String mobilePhone, String authCode, String oldUserTag, String promotionId) throws Exception;

}
