package com.womai.m.mip.channel.util;

import com.github.cage.Cage;
import com.womai.m.mip.common.utils.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


public class IdentifyingCodeUtil {
    private static Log log = LogFactory.getLog(IdentifyingCodeUtil.class);
    public static final String VERIFY_CODES = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String CODE = "CODE";
    public static final String CREATE_TIME="CREATE_TIME";
    public static final String IDENTIFYING_CODE = "_IDENTIFYING_CODE";
    public static final String ERROR_COUNT = "_ERROR_COUNT";
    public static final String ERROR_TIME = "_ERROR_TIME";
    public static final String NEED_CODE = "_NEED_CODE";
    /**
     * 生成验证sessionkey
     * @param type
     * @return
     */
    private static String createSessionKeyName(IdentifyingCodeTypeEnum type){
        return type.toString()+IDENTIFYING_CODE;
    }
    /**
     * 删除验证码缓存
     * @param session
     */
    public static void removeCode(HttpSession session,IdentifyingCodeTypeEnum type){
        if(session!=null && session.getAttribute(createSessionKeyName(type))!=null) {
            session.removeAttribute(createSessionKeyName(type));
            session.removeAttribute(type.toString()+ERROR_COUNT);
            session.removeAttribute(type.toString()+ERROR_TIME);
            session.removeAttribute(type.toString()+NEED_CODE);
        }
    }

    /**
     * 校验验证码
     * @param code
     * @return
     */
    public static boolean checkCode(String code, HttpServletRequest request, String codeType){
        //压力测试专用验证码，压力测试结束后注释
        //if("428y".equalsIgnoreCase(code)){
        //    return true;
        //}
        if(StringUtils.isBlank(code)){
            return false;
        }
        IdentifyingCodeTypeEnum type = IdentifyingCodeTypeEnum.getValue(codeType);
        String encryptedIdentifyCode = CookieUtil.getCookieValue(request, createSessionKeyName(type));

        if (StringUtils.isNotBlank(encryptedIdentifyCode) && encryptedIdentifyCode.equals(encryptIdentifyCode(type, code.toLowerCase()))) {
            return true;
        }
        return false;
    }

    public static void generateImageCode(HttpServletResponse response, String codeType, int codeSize) throws Exception{
        if(response==null){
            return;
        }
        OutputStream os = response.getOutputStream();
        IdentifyingCodeTypeEnum type = IdentifyingCodeTypeEnum.getValue(codeType);
        try {
            String code = generateCode(codeSize, VERIFY_CODES);
            CookieUtil.addCookie(response, createSessionKeyName(type), encryptIdentifyCode(type, code.toLowerCase()), null, 60);
            Cage cage = new Cage();
            cage.draw(code, os);

        } catch(Exception e){
            log.error("Drawing image error", e);
        }finally {
            if(os!=null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.error("Closing os error", e);
                }
            }
        }
    }
    /**
     * 使用指定源生成验证码
     * @param verifySize
     * @param sources
     * @return
     */
    public static String generateCode(int verifySize, String sources){
        if(sources == null || sources.length() == 0){
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for(int i = 0; i < verifySize; i++){
            verifyCode.append(sources.charAt(rand.nextInt(codesLen-1)));
        }
        return verifyCode.toString();
    }

    private static String encryptIdentifyCode(IdentifyingCodeTypeEnum type, String code) {
        return MD5Util.md5(type.toString()+code);
    }
}

enum IdentifyingCodeTypeEnum {
    LOGIN("登录","0"),REGISTER("注册","1"),PWDSENDCODE("找回密码手机验证码","2"),FORGETPWDBYEMAIL("邮箱找回密码","3"),UPDATEPWD("修改密码","4"),SETPAYPWD("设置支付密码","5"),
    BINDPHONE("绑定手机","6"),UPDATEPHONE("修改绑定手机","7"),COUPONACTIVITY("领券活动验证码","8"),COUPONACTIVITYCHUNKUN("春困领券活动验证码","9"), NOPASSLOGIN("免密登录验证码","11");

    private String name;
    private String value;

    private IdentifyingCodeTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static IdentifyingCodeTypeEnum getValue(String key){
        IdentifyingCodeTypeEnum [] enums = IdentifyingCodeTypeEnum.values();
        for(int i=0;i<enums.length;i++){
            if(enums[i].getValue().equals(key)){
                return enums[i];
            }
        }
        return null;
    }
}

