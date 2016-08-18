package com.womai.m.mip.scheduled;

/**
 * Created by zheng.zhang on 2016/5/12.
 */
public interface ScheduledTask {

    public String getTaskName();

    public void execute() throws Exception;

}
