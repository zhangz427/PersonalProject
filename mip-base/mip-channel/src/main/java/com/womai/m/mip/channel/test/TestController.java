package com.womai.m.mip.channel.test;

import com.womai.m.mip.channel.BaseController;
import com.womai.m.mip.channel.BaseGeneralResponse;
import com.womai.m.mip.channel.BaseResponse;
import com.womai.m.mip.domain.test.User;
import com.womai.m.mip.domain.test.UserInfo;
import com.womai.m.mip.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zheng.zhang on 2016/6/15.
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    private TestService testService;

    @ResponseBody
    @RequestMapping(value = "/username",produces="application/json;charset=utf-8")
    public BaseResponse getUsername(HttpServletRequest request) {
        String id = request.getParameter("id");
        User user = testService.getUser(Integer.parseInt(id));
        return new BaseGeneralResponse(user);
    }

    @ResponseBody
    @RequestMapping(value = "/createuser",produces="application/json;charset=utf-8")
    public BaseResponse createUser(HttpServletRequest request) {
        String username = request.getParameter("username");
        User user = new User();
        user.setName(username);
        String age = request.getParameter("age");
        String gender = request.getParameter("gender");
        String company = request.getParameter("company");
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(Integer.parseInt(age));
        userInfo.setGender(Integer.parseInt(gender));
        userInfo.setCompany(company);
        testService.insertUser(user, userInfo);
        return new BaseResponse();
    }


    @ResponseBody
    @RequestMapping(value = "/adduserinfo",produces="application/json;charset=utf-8")
    public BaseResponse addUserInfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        String age = request.getParameter("age");
        String gender = request.getParameter("gender");
        String company = request.getParameter("company");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(Integer.parseInt(id));
        userInfo.setAge(Integer.parseInt(age));
        userInfo.setGender(Integer.parseInt(gender));
        userInfo.setCompany(company);
        testService.insertUserInfo(userInfo);
        return new BaseResponse();
    }
}
