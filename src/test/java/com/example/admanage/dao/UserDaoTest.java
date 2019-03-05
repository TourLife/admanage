package com.example.admanage.dao;

import com.example.admanage.entity.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static javafx.scene.input.KeyCode.L;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void queryUser() throws Exception {
        List<User> userList = userDao.queryUser(true);
        assertEquals(2,userList.size());
    }

    @Test
    @Ignore
    public void queryUserByCondition() throws Exception {
        User users = new User();
        users.setUserId(2);
        User user = userDao.queryUserByCondition(users);
        assertEquals("张三三",user.getUserName());
    }

    @Test
    @Ignore
    public void insertUser() throws Exception {
        User user = new User();
        user.setUserName("张三");
        user.setUserAge(23);
        user.setUserSex(0);
        int i = userDao.insertUser(user);
        assertEquals(3,i);
    }

    @Test
    @Ignore
    public void updateUser() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUserName("李四四");
        int i = userDao.updateUser(user);
        System.out.print(i);
        assertEquals(1,i);
    }

    @Test
    @Ignore
    public void deleteUser() throws Exception {
        User user = new User();
        user.setUserId(3);
        int i = userDao.deleteUser(user);
        System.out.println("***************************"+i);
        assertEquals(1,i);
    }

    public static void main(String[] args) {
        Float account = 49996508.00f;
        Float speed = 0.3f;
        BigDecimal b1 = new BigDecimal(Double.toString(account));

        BigDecimal b2 = new BigDecimal(Double.toString(speed));

        double result = b1.subtract(b2).doubleValue();
        System.out.println(result);
    }
}