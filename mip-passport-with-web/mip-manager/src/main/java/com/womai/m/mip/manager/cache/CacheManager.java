package com.womai.m.mip.manager.cache;

import com.womai.m.mip.manager.BaseManagerResponse;

/**
 * Created by zheng.zhang on 2016/3/22.
 */
public interface CacheManager {

    public void set(String key, String value) throws Exception;
    public void set(String key, String value, long timeout) throws Exception;
    public boolean sismember(String key, String value) throws Exception;
	public Object get(String key) throws Exception;
    public void del(String key) throws Exception;

}
