package com.example.admanage.service;

import com.example.admanage.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 登录验证账号密码
     */
    User queryLogin(String loginName,String password);

    /**
     * 获取所有用户
     *
     * @return userList
     */
    List<User> queryUser(Boolean isAll);
    /**
     * 根据条件查询用户
     * @param user
     * @return num
     */
    User queryUserByCondition(User user);
    /**
     * 插入用户
     *
     * @return num
     */
    int insertUser(User user);
    /**
     * 更新用户信息
     *
     * @return num
     */
    int updateUser(User user);

    /**
     * 删除用户
     *
     * @return num
     */
    int deleteUser(User user);
}
