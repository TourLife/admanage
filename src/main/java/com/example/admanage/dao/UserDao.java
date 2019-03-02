package com.example.admanage.dao;

import com.example.admanage.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    /**
     * 登录验证账号密码
     * @return User
     */
    public User queryLogin(@Param("loginName") String loginName, @Param("password") String password);

    /**
     * 获取所有用户
     *
     * @return userList
     */
    public List<User> queryUser(@Param("isAll")Boolean isAll);
    /**
     * 根据条件查询用户
     * @param user
     * @return num
     */
    public User queryUserByCondition(User user);
    /**
     * 插入用户
     *
     * @return num
     */
    public int insertUser(User user);
    /**
     * 更新用户信息
     *
     * @return num
     */
    public int updateUser(User user);

    /**
     * 删除用户
     *
     * @return num
     */
    public int deleteUser(User user);
}
