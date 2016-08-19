package com.womai.m.mip.channel.util;

import com.womai.m.mip.manager.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zheng.zhang on 2016/6/17.
 */
@Component
public class SourceValidator {

    private static final String MIP_SOURCE_SET_KEY = "MIP_SOURCE_SET";

    @Autowired
    private CacheManager cacheManager;

    public boolean isValid(String mipSourceId) {
        try {
            return cacheManager.sismember(MIP_SOURCE_SET_KEY, mipSourceId);
        } catch(Exception e) {
            return false;
        }

    }

}
