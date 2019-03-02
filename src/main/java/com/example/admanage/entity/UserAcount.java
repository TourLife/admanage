package com.example.admanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import java.util.Date;

public class UserAcount {

    //主键id
    private int id;
    //用户id
    private int userId;
    @Min(value = 0,message = "您的余额不得小于最小值0")
    private float blanance;
    //已经消耗的金额
    private float useMoney;
    //消耗速度
    private float spendSpeed;
    //是否开始运行
    private Integer isStart;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , locale = "zh",timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , locale = "zh",timezone = "GMT+8")
    private Date updateTime;

    public UserAcount(){

    }

    public UserAcount(Integer userId,Integer isStart){
        this.userId = userId;
        this.isStart = isStart;
    }

    public UserAcount(Integer userId,Float blanance,Float spendSpeed,Integer isStart){
        this.userId = userId;
        this.blanance = blanance ;
        this.spendSpeed = spendSpeed;
        this.isStart = isStart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setBlanance(float blanance) {
        this.blanance = blanance;
    }

    public float getBlanance() {
        return blanance;
    }

    public float getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(float useMoney) {
        this.useMoney = useMoney;
    }

    public Integer getIsStart() {
        return isStart;
    }

    public void setIsStart(Integer isStart) {
        this.isStart = isStart;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public float getSpendSpeed() {
        return spendSpeed;
    }

    public void setSpendSpeed(float spendSpeed) {
        this.spendSpeed = spendSpeed;
    }
}
