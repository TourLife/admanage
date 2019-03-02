package com.example.admanage.service.Impl;

import com.example.admanage.dao.UserDao;
import com.example.admanage.entity.User;
import com.example.admanage.service.UserService;
import com.example.admanage.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.StyledEditorKit;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 登录验证账号密码
     */
    @Override
    public User queryLogin(String loginName,String password){
        User user = userDao.queryLogin(loginName,password);
        return user;
    }

    @Override
    public List<User> queryUser(Boolean isAll) {
        List<User> userList = userDao.queryUser(isAll);
        return userList;
    }

    @Override
    public User queryUserByCondition(User user) {
        User result = userDao.queryUserByCondition(user);
        return result;
    }

    @Transactional
    @Override
    public int insertUser(User user) {
        int flagNum = 0;
        if(StringUtils.isNotBlank(user.getUserName())){
            try {
                int result = userDao.insertUser(user);
                if (result == 1){
                    flagNum = result;
                }else{
                    throw new RuntimeException("插入用户信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("插入用户信息失败！"+e.getMessage());
            }
        }
        return flagNum;
    }

    @Transactional
    @Override
    public int updateUser(User user) {
        int flagNum = 0;
        if(StringUtils.isNotBlank(user.getUserName())){
            try {
                int result = userDao.updateUser(user);
                if (result == 1){
                    flagNum = result;
                }else{
                    throw new RuntimeException("更新用户信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("更新用户信息失败！"+e.getMessage());
            }
        }
        return flagNum;
    }

    @Override
    public int deleteUser(User user) {
        int flagNum = 0;
        if(user.getUserId() != 0){
            try {
                int result = userDao.deleteUser(user);
                if (result == 1){
                    flagNum = result;
                }else{
                    throw new RuntimeException("删除用户信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("删除用户信息失败！"+e.getMessage());
            }
        }
        return flagNum;
    }
}
