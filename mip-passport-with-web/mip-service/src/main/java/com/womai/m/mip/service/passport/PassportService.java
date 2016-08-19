package com.womai.m.mip.service.passport;

import com.womai.m.mip.domain.passport.TicketInfo;
import com.womai.m.mip.domain.sso.LoginUser;
import com.womai.m.mip.service.BaseServiceResponse;

/**
 * Created by zheng.zhang on 2016/5/23.
 */
public interface PassportService {

    public BaseServiceResponse<TicketInfo> login(String username, String password, String promotionId) throws Exception;

    public TicketInfo checkLogin(String ticketGrantingCookie) throws Exception;

    public LoginUser verifyLogin(String serviceToken) throws Exception;

    public void logout(String ticketGrantingCookie) throws Exception;

    public BaseServiceResponse<TicketInfo> unionLogin(String openId,String nickName,String gender,String type) throws Exception;

    public BaseServiceResponse sendRegisterCode(String mobilePhone) throws Exception;

    public BaseServiceResponse<TicketInfo> fastRegister(String mobilePhone, String authCode) throws Exception;

    public BaseServiceResponse sendFindPwdCode(String mobilePhone) throws Exception;

    public BaseServiceResponse noPassLoginSendCode(String mobilePhone) throws Exception;

    public BaseServiceResponse findPwdVerifyCode(String mobilePhone, String authCode) throws Exception;

    public BaseServiceResponse resetPassword(String mobilePhone, String password, String verifyPassword) throws Exception;

    public BaseServiceResponse findPwdByMail(String email) throws Exception;

    public BaseServiceResponse<TicketInfo> noPassLogin(String mobilePhone, String authCode) throws Exception;
}
