package com.womai.m.mip.service.test;


import com.womai.m.mip.domain.test.User;
import com.womai.m.mip.domain.test.UserInfo;
import com.womai.m.mip.manager.test.TestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zheng.zhang on 2016/6/15.
 */
@Component
public class TestService {

    @Autowired
    private TestManager testManager;

    public User getUser(int id) {
        return testManager.getUser(id);
    }

    public void insertUser(User user) {
        testManager.insertUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertUser(User user, UserInfo userInfo) {
        testManager.insertUser(user);
        userInfo.setId(user.getId());
        testManager.insertUserInfo(userInfo);
    }

    public void insertUserInfo(UserInfo userInfo) {
        testManager.insertUserInfo(userInfo);
    }

}
