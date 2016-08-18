package com.womai.m.mip.channel.util;

import com.womai.common.tool.security.MD5;
import com.womai.m.mip.common.utils.MD5Util;
import com.womai.m.mip.common.utils.ThreeDES;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by zheng.zhang on 2016/4/20.
 */
public class SourceGenerateUtil {

    public static String generateSource(String mipSourceId) throws Exception{
        String key="qwertyuiopasdfghjkl;'zxc";
        String encryptedStr = ThreeDES.orginalEncoded(key, "mipSourceId="+mipSourceId);
        return encryptedStr;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("2001:"+generateSource("2001"));
        System.out.println("3001:"+generateSource("3001"));
        System.out.println("3002:"+generateSource("3002"));
        System.out.println("4001:"+generateSource("4001"));
        System.out.println("4301:"+generateSource("4301"));
        System.out.println("5001:"+generateSource("5001"));
        System.out.println("shichi:5001:"+generateSource("5001"));
        System.out.println("wuhan:1007:"+generateSource("1007"));


        System.out.println("3001:" + MD5Util.md5("3001"));




    }




}
