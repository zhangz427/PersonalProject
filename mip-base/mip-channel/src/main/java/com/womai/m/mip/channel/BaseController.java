package com.womai.m.mip.channel;

import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.common.utils.ThreeDES;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zheng.zhang on 2016/3/8.
 */

public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${3DES.key}")
    protected String originalKey;

    @Resource
    private Set<String> legalSourceSet;

    public BaseEncryptResponse convertToBaseEncryptResponse(Object o) {

        try {
            String data = JacksonUtil.toJson(o);
            String encryptedData = new String(Base64.encodeBase64(data.getBytes()), "UTF-8");
            logger.debug("response data:{}", data);
            logger.debug("encrypted response data:{}", encryptedData);

            return new BaseEncryptResponse(encryptedData);
        } catch(Exception e) {
            logger.error("Fail to encrypt response: ", e);
            return new BaseEncryptResponse();
        }


    }

    public Map<String, Object> decryptRequestData(String encrypedRequestData) throws Exception {

        String decryptedString = new String(Base64.decodeBase64(encrypedRequestData.getBytes()), "UTF-8");

        logger.info("Decrypted request data:{}", decryptedString);

        Map<String, Object> dataMap = JacksonUtil.toMap(decryptedString);

        Map<String, Object> requestMap = (Map<String, Object>)dataMap.get("data");

        return requestMap;
    }

    public Map<String, Object> decryptHeaderData(String encryptedHeaderData) throws Exception {
        String decryptHeaderDataStr = ThreeDES.orginalDecryptMode(originalKey, encryptedHeaderData);
        logger.info("Decrypted header data:{}", decryptHeaderDataStr);
        Map<String, Object> headerMap = JacksonUtil.toMap(decryptHeaderDataStr);
        return headerMap;
    }

    public BaseResponse createErrorResponse(String errorMsg) {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponse.RESPONSE_FAIL);
        response.setMessage(errorMsg);
        return response;
    }

    public String getOriginalKey() {
        return originalKey;
    }

    public void setOriginalKey(String originalKey) {
        this.originalKey = originalKey;
    }

    protected boolean sourceValidate(String mipSourceId) {

        if (StringUtils.isBlank(mipSourceId)) {
            logger.error("mipSourceId is null");
            return false;
        } else if (!isValidMipSource(mipSourceId)) {
            logger.error("Invalid mipSourceId");
            return false;
        }
        return true;
    }

    protected String decryptMipSource(HttpServletRequest request) {
        String encryptedMipSource = request.getParameter("source");
        if (StringUtils.isNotBlank(encryptedMipSource)) {
            String decryptMipSource = ThreeDES.orginalDecryptMode(originalKey, encryptedMipSource);
            logger.debug("Mip Source: {}", decryptMipSource);
            String mipSourceId = StringUtils.substring(decryptMipSource, StringUtils.indexOf(decryptMipSource, "=") + 1);
            return mipSourceId;
        } else {
            return null;
        }


    }

    protected boolean isValidMipSource(String mipSourceId) {
        return legalSourceSet.contains(mipSourceId);
    }

    protected void setLegalSourceSet(Set<String> legalSourceSet) {
        this.legalSourceSet = legalSourceSet;
    }
}
