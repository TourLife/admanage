package com.example.admanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
/**
 * 数据统计类
 */
public class DataStatistics {

    //自增主键
    private Integer Id;
    //用户id
    private Integer userId;
    //统计花费的金额
    private Float totalMoney;
    //消耗的时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date spendDate;
    //数据入库的时间
    private Date CreateTime;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Date getSpendDate() {
        return spendDate;
    }

    public void setSpendDate(Date spendDate) {
        this.spendDate = spendDate;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }
}
