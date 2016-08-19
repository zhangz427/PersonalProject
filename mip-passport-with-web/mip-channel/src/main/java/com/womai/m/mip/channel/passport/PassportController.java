package com.womai.m.mip.channel.passport;

import com.womai.m.mip.channel.BaseController;
import com.womai.m.mip.channel.BaseGeneralResponse;
import com.womai.m.mip.channel.BaseResponse;
import com.womai.m.mip.channel.util.IdentifyingCodeUtil;
import com.womai.m.mip.channel.util.CookieUtil;
import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.passport.TicketInfo;
import com.womai.m.mip.domain.sso.LoginUser;
import com.womai.m.mip.service.BaseServiceResponse;
import com.womai.m.mip.service.passport.PassportService;
import com.womai.m.mip.service.sso.SSOService;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by zheng.zhang on 2016/5/18.
 */
@Controller
@RequestMapping("/passport")
public class PassportController extends BaseController {

    @Autowired
    protected PassportService passportService;

    @Value("${passport.home.module}")
    private String passportHomeModule;

    @Value("${passport.default.redirect.url}")
    private String defaultRedirectUrl;

    @Value("${passport.m.home.module}")
    private String mobileHomeRedirectUrl;

    @RequestMapping(value = "/generateIdentifyCode")
    public void generateIdentifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String codeType = request.getParameter("codeType");
        if (StringUtils.isNotBlank(codeType)) {
            IdentifyingCodeUtil.generateImageCode(response, codeType, 4);
        }
    }

    @RequestMapping(value = "/checkLogin")
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

        try {
            String redirectUrl = request.getParameter("redirectUrl");
            String ssotoken = CookieUtil.getCookieValue(request, "ssotoken");
            if (StringUtils.isBlank(ssotoken)) {
                return toLoginPage(redirectUrl, response, model, null);
            }
            //check if ssotoken is valid and get ticket info
            TicketInfo ticketInfo = passportService.checkLogin(ssotoken);
            if (ticketInfo == null) {
                return toLoginPage(redirectUrl, response, model, null);
            } else {
                storeTGCAndRedirect(ticketInfo, response, redirectUrl);
            }
            return "success";
        } catch(Exception e) {
            logger.error("Fail to check login, ", e);
            return "error";
        }
    }

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

        try{
            String username = request.getParameter("username");
            if (StringUtils.isBlank(username)) {
                return toLoginPage(null, response, model, "username is null");

            }
            String password = request.getParameter("password");
            if (StringUtils.isBlank(password)) {
                return toLoginPage(null, response, model, "password is null");
            }

            String identifyingCode = request.getParameter("identifyingCode");
            if (StringUtils.isBlank(identifyingCode)) {
                return toLoginPage(null, response, model, "identifyingCode is null");

            }

            String codeType = request.getParameter("codeType");
            if (StringUtils.isBlank(codeType)) {
                return toLoginPage(null, response, model, "codeType is null");
            }

            if (!IdentifyingCodeUtil.checkCode(identifyingCode, request, codeType)) {
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                return toLoginPage(null, response, model, "验证码错误或已失效");
            }

            String promotionId = request.getParameter("promotionId");

            String redirectUrl = request.getParameter("redirectUrl");
            if (StringUtils.isBlank(redirectUrl)) {
                redirectUrl = CookieUtil.getCookieValue(request, "redirectUrl");
            }

            logger.info("login request, username: {}", username);
            BaseServiceResponse<TicketInfo> loginResponse = passportService.login(username, password, promotionId);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(loginResponse.getCode())) {
                logger.info("Login success: {}", JacksonUtil.toJson(loginResponse.getData()));
                TicketInfo ticketInfo = loginResponse.getData();
                storeTGCAndRedirect(ticketInfo, response, redirectUrl);
                return "success";
            } else {
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                return toLoginPage(null, response, model, loginResponse.getMessage());
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            return "error";
        }

    }

    @ResponseBody
    @RequestMapping(value = "/verifyLogin",produces="application/json;charset=utf-8")
    public BaseResponse verifyLogin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            String serviceToken = request.getParameter("servicetoken");
            if (StringUtils.isNotBlank(serviceToken)) {
                LoginUser loginUser = passportService.verifyLogin(serviceToken);
                if (loginUser != null) {
                    return new BaseGeneralResponse(loginUser);
                }
            }
            return createErrorResponse("Fail to verify login");
        } catch (Exception e) {
            logger.error("Fail to verify login, ", e);
            return createErrorResponse("Fail to verify login");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String redirectUrl = request.getParameter("redirectUrl");
        String ssotoken = CookieUtil.getCookieValue(request, "ssotoken");

        logger.info("Logout ssotoken {}.", ssotoken);

        if (StringUtils.isNotBlank(ssotoken)) {
            passportService.logout(ssotoken);
        }
        if (StringUtils.isNotBlank(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect(mobileHomeRedirectUrl);
        }

        return "success";
    }

    @RequestMapping(value = "/toRegister")
    public String toRegister(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        String redirectUrl = request.getParameter("redirectUrl");
        if (StringUtils.isNotBlank(redirectUrl)) {
            CookieUtil.addCookie(response, "redirectUrl", redirectUrl, null, -1);
        }
        return toRegisterPage(response, model, null);
    }

    @RequestMapping(value = "/sendRegisterCode")
    public void sendRegisterCode(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try{
            String mobileNumber = request.getParameter("mobileNumber");
            if (StringUtils.isBlank(mobileNumber)) {
                logger.error("mobileNumber is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            String identifyingCode = request.getParameter("identifyingCode");
            if (StringUtils.isBlank(identifyingCode)) {
                logger.error("identifyingCode is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            String codeType = request.getParameter("codeType");
            if (StringUtils.isBlank(codeType)) {
                logger.error("codeType is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            if (!IdentifyingCodeUtil.checkCode(identifyingCode, request, codeType)) {
                writeOutput(response, "验证码错误或已失效");
                return;
            }

            logger.info("Send register code for mobile: {}", mobileNumber);
            BaseServiceResponse senRegisterCodeResponse = passportService.sendRegisterCode(mobileNumber);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(senRegisterCodeResponse.getCode())) {
                logger.info("Send register code Successfully");
                writeOutput(response, "success");
                return;
            } else {
                writeOutput(response, senRegisterCodeResponse.getMessage());
                return;
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            writeOutput(response, "System error");
            return;
        }
    }

    @RequestMapping(value = "/fastRegister")
    public String fastRegister(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{

            String mobileNumber = request.getParameter("mobileNumber");
            if (StringUtils.isBlank(mobileNumber)) {
                logger.error("mobileNumber is null");
                return toRegisterPage(response, model, "注册失败");
            }

            String authCode = request.getParameter("authCode");
            if (StringUtils.isBlank(authCode)) {
                logger.error("authCode is null");
                return toRegisterPage(response, model, "注册失败");
            }

            model.addAttribute("mobileNumber", mobileNumber);
            model.addAttribute("authCode", authCode);

            String redirectUrl = request.getParameter("redirectUrl");
            if (StringUtils.isBlank(redirectUrl)) {
                redirectUrl = CookieUtil.getCookieValue(request, "redirectUrl");
            }

            logger.info("Register request mobileNumber: {}", mobileNumber);
            BaseServiceResponse<TicketInfo> loginResponse = passportService.fastRegister(mobileNumber, authCode);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(loginResponse.getCode())) {
                logger.info("Register success: {}", JacksonUtil.toJson(loginResponse.getData()));
                TicketInfo ticketInfo = loginResponse.getData();
                storeTGCAndRedirect(ticketInfo, response, redirectUrl);
                return "success";
            } else {
                return toRegisterPage(response, model, loginResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("system error,", e);
            return toRegisterPage(response, model, "System error");
        }
    }

    @RequestMapping(value = "/toForgetPwd")
    public String toForgetPwd(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        return toForgetPasswordPage(response, model, null, true);
    }

    @RequestMapping(value = "/sendFindPwdCode")
    public void sendFindPwdCode(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try{
            String mobileNumber = request.getParameter("mobileNumber");
            if (StringUtils.isBlank(mobileNumber)) {
                logger.error("mobileNumber is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            String identifyingCode = request.getParameter("identifyingCode");
            if (StringUtils.isBlank(identifyingCode)) {
                logger.error("identifyingCode is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            String codeType = request.getParameter("codeType");
            if (StringUtils.isBlank(codeType)) {
                logger.error("codeType is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            if (!IdentifyingCodeUtil.checkCode(identifyingCode, request, codeType)) {
                writeOutput(response, "验证码错误或已失效");
                return;
            }

            logger.info("Send find password code for mobile: {}", mobileNumber);
            BaseServiceResponse senRegisterCodeResponse = passportService.sendFindPwdCode(mobileNumber);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(senRegisterCodeResponse.getCode())) {
                logger.info("Send find password code Successfully");
                writeOutput(response, "success");
                return;
            } else {
                writeOutput(response, senRegisterCodeResponse.getMessage());
                return;
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            writeOutput(response, "System error");
            return;
        }
    }

    @RequestMapping(value = "/forgetPwdMobile")
    public String forgetPwdMobile(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

        String mobileNumber = request.getParameter("mobileNumber");
        if (StringUtils.isBlank(mobileNumber)) {
            logger.error("mobileNumber is null");
            return toForgetPasswordPage(response, model, "找回密码失败", true);
        }

        String authCode = request.getParameter("authCode");
        if (StringUtils.isBlank(authCode)) {
            logger.error("authCode is null");
            return toForgetPasswordPage(response, model, "找回密码失败", true);
        }

        model.addAttribute("mobileNumber", mobileNumber);
        model.addAttribute("authCode", authCode);
        BaseServiceResponse verifyFindPwdCodeResponse = passportService.findPwdVerifyCode(mobileNumber, authCode);

        if (BaseServiceResponse.RESPONSE_SUCCESS.equals(verifyFindPwdCodeResponse.getCode())) {
            logger.info("Verify find password code successfully");
            return "resetPassword";
        } else {
            logger.error("Fail to verify find password code: {}", verifyFindPwdCodeResponse.getMessage());
            return toForgetPasswordPage(response, model, verifyFindPwdCodeResponse.getMessage(), true);
        }
    }

    @RequestMapping(value = "/forgetPwdEmail")
    public String forgetPwdEmail(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

        String email = request.getParameter("email");
        if (StringUtils.isBlank(email)) {
            logger.error("email is null");
            return toForgetPasswordPage(response, model, "找回密码失败", false);
        }
        model.addAttribute("email", email);
        String identifyingCode = request.getParameter("identifyingCode");
        if (StringUtils.isBlank(identifyingCode)) {
            return toForgetPasswordPage(response, model, "找回密码失败", false);
        }

        String codeType = request.getParameter("codeType");
        if (StringUtils.isBlank(codeType)) {
            return toForgetPasswordPage(response, model, "找回密码失败", false);
        }

        if (!IdentifyingCodeUtil.checkCode(identifyingCode, request, codeType)) {
            return toForgetPasswordPage(response, model, "验证码错误或已失效", false);
        }

        BaseServiceResponse findPwdByMailResponse = passportService.findPwdByMail(email);

        if (BaseServiceResponse.RESPONSE_SUCCESS.equals(findPwdByMailResponse.getCode())) {
            logger.info("Find password by mail successfully");
            model.addAttribute("successResult", "success");
            return toForgetPasswordPage(response, model, null, false);
        } else {
            logger.error("Fail to verify find password code: {}", findPwdByMailResponse.getMessage());
            return toForgetPasswordPage(response, model, findPwdByMailResponse.getMessage(), false);
        }
    }

    @RequestMapping(value = "/resetPassword")
    public String resetPassword(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

        String password = request.getParameter("password");
        if (StringUtils.isBlank(password)) {
            logger.error("password is null");
            return toResetPassowrd(response, model, "重置密码失败");
        }

        String conirmpw = request.getParameter("conirmpw");
        if (StringUtils.isBlank(conirmpw)) {
            logger.error("conirmpw is null");
            return toResetPassowrd(response, model, "重置密码失败");
        }

        String mobile = request.getParameter("mobile");
        if (StringUtils.isBlank(mobile)) {
            logger.error("mobile is null");
            return toResetPassowrd(response, model, "重置密码失败");
        }

        BaseServiceResponse resetPasswordResponse = passportService.resetPassword(mobile, password, conirmpw);

        if (BaseServiceResponse.RESPONSE_SUCCESS.equals(resetPasswordResponse.getCode())) {
            logger.info("Reset password code successfully");
            return "resetPasswordSuccess";
        } else {
            logger.error("Fail to verify find password code: {}", resetPasswordResponse.getMessage());
            return toResetPassowrd(response, model, resetPasswordResponse.getMessage());
        }
    }

    @RequestMapping(value = "/toNoPassLogin")
    public String toNoPassLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        return toNoPassLoginPage(response, model, null);
    }

    @RequestMapping(value = "/noPassLoginSendCode")
    public void noPassLoginSendCode(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try{
            String mobileNumber = request.getParameter("mobileNumber");
            if (StringUtils.isBlank(mobileNumber)) {
                logger.error("mobileNumber is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            String identifyingCode = request.getParameter("identifyingCode");
            if (StringUtils.isBlank(identifyingCode)) {
                logger.error("identifyingCode is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            String codeType = request.getParameter("codeType");
            if (StringUtils.isBlank(codeType)) {
                logger.error("codeType is null");
                writeOutput(response, "发送验证码失败");
                return;
            }

            if (!IdentifyingCodeUtil.checkCode(identifyingCode, request, codeType)) {
                writeOutput(response, "验证码错误或已失效");
                return;
            }

            logger.info("Send no pass login code for mobile: {}", mobileNumber);
            BaseServiceResponse noPassLoginSendCodeResponse = passportService.noPassLoginSendCode(mobileNumber);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(noPassLoginSendCodeResponse.getCode())) {
                logger.info("Send no pass login code Successfully");
                writeOutput(response, "success");
                return;
            } else {
                writeOutput(response, noPassLoginSendCodeResponse.getMessage());
                return;
            }

        } catch (Exception e) {
            logger.error("system error,", e);
            writeOutput(response, "System error");
            return;
        }
    }

    @RequestMapping(value = "/noPassLogin")
    public String noPassLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{

            String mobileNumber = request.getParameter("mobileNumber");
            if (StringUtils.isBlank(mobileNumber)) {
                logger.error("mobileNumber is null");
                return toNoPassLoginPage(response, model, "登录服务器异常,请重试!!!");
            }

            String authCode = request.getParameter("authCode");
            if (StringUtils.isBlank(authCode)) {
                logger.error("authCode is null");
                return toNoPassLoginPage(response, model, "登录服务器异常,请重试!!!");
            }

            model.addAttribute("mobileNumber", mobileNumber);
            model.addAttribute("authCode", authCode);

            String redirectUrl = request.getParameter("redirectUrl");
            if (StringUtils.isBlank(redirectUrl)) {
                redirectUrl = CookieUtil.getCookieValue(request, "redirectUrl");
            }

            logger.info("No pass login request mobileNumber: {}", mobileNumber);
            BaseServiceResponse<TicketInfo> loginResponse = passportService.noPassLogin(mobileNumber, authCode);

            if (BaseServiceResponse.RESPONSE_SUCCESS.equals(loginResponse.getCode())) {
                logger.info("No pass login success: {}", JacksonUtil.toJson(loginResponse.getData()));
                TicketInfo ticketInfo = loginResponse.getData();
                storeTGCAndRedirect(ticketInfo, response, redirectUrl);
                return "success";
            } else {
                return toNoPassLoginPage(response, model, loginResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("system error,", e);
            return toNoPassLoginPage(response, model, "System error");
        }
    }

    private String toNoPassLoginPage(HttpServletResponse response, Model model, String errorMsg) {
//        model.addAttribute("identifyCodeUrl", IDENTIFY_CODE_URL);
        if (StringUtils.isNotBlank(errorMsg)) {
            model.addAttribute("noPassLoginResult", errorMsg);
        }
        return "noPassLogin";
    }

    private String toResetPassowrd(HttpServletResponse response, Model model, String errorMsg) {
        if (StringUtils.isNotBlank(errorMsg)) {
            model.addAttribute("forgetPwdResultMobile", errorMsg);
        }
        return "resetPassword";
    }

    private String toForgetPasswordPage(HttpServletResponse response, Model model, String errorMsg, boolean findByMobile) throws Exception {
        if (StringUtils.isNotBlank(errorMsg)) {
            model.addAttribute("forgetPwdResult", errorMsg);
        }
        if (findByMobile) {
            if (StringUtils.isNotBlank(errorMsg)) {
                model.addAttribute("forgetPwdResultMobile", errorMsg);
            }
            model.addAttribute("findByMobile", "true");
        } else {
            if (StringUtils.isNotBlank(errorMsg)) {
                model.addAttribute("forgetPwdResultMail", errorMsg);
            }
            model.addAttribute("findByMobile", "false");
        }
        return "forgetPwd";
    }

    private void writeOutput(HttpServletResponse response, String output) throws Exception {
        OutputStream outputStream = response.getOutputStream();//获取OutputStream输出流
        response.setHeader("content-type", "text/html;charset=UTF-8");//通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
        byte[] dataByteArr = output.getBytes("UTF-8");//将字符转换成字节数组，指定以UTF-8编码进行转换
        outputStream.write(dataByteArr);//使用OutputStream流向客户端输出字节数组
    }

    private String toRegisterPage(HttpServletResponse response, Model model, String errorMsg) {

        if (StringUtils.isNotBlank(errorMsg)) {
            model.addAttribute("registerResult", errorMsg);
        }
        return "register";
    }

    protected String toLoginPage(String redirectUrl, HttpServletResponse response, Model model, String errorMsg) {
        //抓取redirectUrl并存储到cookie中
        if (StringUtils.isNotBlank(redirectUrl)) {
            CookieUtil.addCookie(response, "redirectUrl", redirectUrl, null, -1);
        }

        if (StringUtils.isNotBlank(errorMsg)) {
            model.addAttribute("loginResult", errorMsg);
        }
        return "login";
    }

    protected void storeTGCAndRedirect(TicketInfo ticketInfo, HttpServletResponse response, String redirectUrl) throws Exception{
        //应存储ssotoken到passport域名下，兼容当前单点登录方案，暂时放到根域名下
        CookieUtil.addCookie(response, "ssotoken",ticketInfo.getTicketGrantingCookie(), null, -1, true);

        logger.info("Store ssotoken {} in cookie", ticketInfo.getTicketGrantingCookie());

        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = defaultRedirectUrl;
        }
        response.sendRedirect(redirectUrl + "?servicetoken=" + ticketInfo.getServiceToken());

    }
}
