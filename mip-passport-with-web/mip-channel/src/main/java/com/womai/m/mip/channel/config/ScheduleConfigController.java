package com.womai.m.mip.channel.config;

import com.womai.m.mip.channel.BaseController;
import com.womai.m.mip.channel.BaseGeneralResponse;
import com.womai.m.mip.channel.BaseResponse;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zheng.zhang on 2016/3/26.
 */
@Controller
@RequestMapping("/scheduleconfig")
public class ScheduleConfigController extends BaseController {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @ResponseBody
    @RequestMapping(value = "/pauseTask",produces="application/json;charset=utf-8")
    public BaseResponse pauseTask(HttpServletRequest request) {
        try {
            String jobName = request.getParameter("jobName");
            String jobGroup = request.getParameter("jobGroup");
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
            return new BaseResponse();
        } catch(Exception e) {
            logger.error("Fail to pause job,", e);
            return createErrorResponse("Fail to pause job");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/resumeTask",produces="application/json;charset=utf-8")
    public BaseResponse resumeTask(HttpServletRequest request) {
        try {
            String jobName = request.getParameter("jobName");
            String jobGroup = request.getParameter("jobGroup");
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.resumeJob(jobKey);
            return new BaseResponse();
        } catch(Exception e) {
            logger.error("Fail to resume job,", e);
            return createErrorResponse("Fail to resume job");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/deleteTask",produces="application/json;charset=utf-8")
    public BaseResponse deleteTask(HttpServletRequest request) {
        try {
            String jobName = request.getParameter("jobName");
            String jobGroup = request.getParameter("jobGroup");
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.deleteJob(jobKey);
            return new BaseResponse();
        } catch(Exception e) {
            logger.error("Fail to delete job,", e);
            return createErrorResponse("Fail to delete job");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/triggerTask",produces="application/json;charset=utf-8")
    public BaseResponse triggerTask(HttpServletRequest request) {
        try {
            String jobName = request.getParameter("jobName");
            String jobGroup = request.getParameter("jobGroup");
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.triggerJob(jobKey);
            return new BaseResponse();
        } catch(Exception e) {
            logger.error("Fail to trigger job,", e);
            return createErrorResponse("Fail to trigger");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/rescheduleTask",produces="application/json;charset=utf-8")
    public BaseResponse rescheduleTask(HttpServletRequest request) {
        try {
            String triggerName = request.getParameter("triggerName");
            String triggerGroup = request.getParameter("triggerGroup");
            String cronExpression = request.getParameter("cronExpression");
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroup);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
            return new BaseResponse();
        } catch(Exception e) {
            logger.error("Fail to trigger job,", e);
            return createErrorResponse("Fail to trigger");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/listTask",produces="application/json;charset=utf-8")
    public BaseResponse listTask(HttpServletRequest request) {
        try {

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            List<Map<String, String>> jobList = new ArrayList<Map<String, String>>();

            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Map<String, String> jobMap = new HashMap<String, String>();
                    jobMap.put("jobName", jobKey.getName());
                    jobMap.put("jobGroup", jobKey.getGroup());

                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    jobMap.put("jobStatus", triggerState.name());

                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        jobMap.put("cronExpression", cronExpression);
                    }
                    jobList.add(jobMap);
                }
            }
            return new BaseGeneralResponse(jobList);
        } catch(Exception e) {
            logger.error("Fail to list task,", e);
            return createErrorResponse("Fail to list task");
        }
    }


}
