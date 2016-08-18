package com.womai.m.mip.dao.test;

import com.womai.m.mip.domain.test.User;

/**
 * Created by zheng.zhang on 2016/6/15.
 */
public interface UserMapper {

    public User selectUserById(int id);

    public User selectUserByName(String name);

    public void insertUser(User user);

}
