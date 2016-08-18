package com.womai.m.mip.manager.test;

import com.womai.m.mip.dao.test.UserInfoMapper;
import com.womai.m.mip.dao.test.UserMapper;
import com.womai.m.mip.domain.test.User;
import com.womai.m.mip.domain.test.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zheng.zhang on 2016/6/15.
 */
@Component
public class TestManager {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    public User getUser(int id) {
        User user = userMapper.selectUserById(id);
        return user;
    }

    public User getUser(String name) {
        User user = userMapper.selectUserByName(name);
        return user;
    }

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public void insertUserInfo(UserInfo userInfo) {
        userInfoMapper.insertUserInfo(userInfo);
    }

}
