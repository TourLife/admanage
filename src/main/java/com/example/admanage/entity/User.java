package com.example.admanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {

    //主键id
    private Integer userId;
    //用户登录名
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String userName;
    //用户登录密码
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String userPassword;
    //是否为超管管理员
    private Integer isSuperadmin;
    //用户年龄
    @Min(value = 18,message = "您的年龄不符合要求")
    private Integer userAge;
    //用户性别
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer userSex;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , locale = "zh",timezone = "GMT+8")
    private Date createTime;

    //删除标志（1，已删除 0 未删除）
    private Integer delFlg;

    public User(){

    }

    public User(String userName, Integer userSex, String userPassword,Date createTime) {
        this.userName = userName;
        this.userSex = userSex;
        this.userPassword = userPassword;
        this.createTime = createTime;
    }

    public User(Integer userId,String userName, Integer userAge, Integer userSex) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userSex = userSex;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIsSuperadmin() {
        return isSuperadmin;
    }

    public void setIsSuperadmin(Integer isSuperadmin) {
        this.isSuperadmin = isSuperadmin;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public Map getUserSex() {
        Map map = new HashMap();
        map.put("sexCode",userSex);
        if(userSex == 0){
            map.put("sexCode",userSex);
            map.put("sexName","男");
            return map;
        }
        map.put("sexName","女");
        return map;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
