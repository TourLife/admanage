package com.example.admanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Plan {

    //主键id
    private int planId;
    //创建人Id
    private int planCreateId;
    //创建人姓名
    private String planCreateName;
    //计划标题
    private String planTitle;
    //计划大分类
    private String planBtype;
    //计划单价
    private float planPrice;
    //计划限额
    private float planMaxprice;
    //计划小分类
    private String planStype;
    //计划logo
    private String planLogo;
    //计划锁定标志
    private int planStatus;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    public Plan(){}

    public Plan(Integer planCreateId,String planCreateName,String planTitle,String planBtype,String planStype,float planPrice,float planMaxprice){
        this.planCreateId = planCreateId;
        this.planCreateName = planCreateName;
        this.planTitle = planTitle;
        this.planBtype = planBtype;
        this.planStype = planStype;
        this.planPrice = planPrice;
        this.planMaxprice = planMaxprice;
    }

    public int getPlanCreateId() {
        return planCreateId;
    }

    public void setPlanCreateId(int planCreateId) {
        this.planCreateId = planCreateId;
    }

    public String getPlanCreateName() {
        return planCreateName;
    }

    public void setPlanCreateName(String planCreateName) {
        this.planCreateName = planCreateName;
    }

    public String getPlanLogo() {
        return planLogo;
    }

    public void setPlanLogo(String planLogo) {
        this.planLogo = planLogo;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanBtype(String planBtype) {
        this.planBtype = planBtype;
    }

    public String getPlanBtype() {
        return planBtype;
    }

    public void setPlanPrice(float planPrice) {
        this.planPrice = planPrice;
    }

    public float getPlanPrice() {
        return planPrice;
    }

    public void setPlanMaxprice(float planMaxprice) {
        this.planMaxprice = planMaxprice;
    }

    public float getPlanMaxprice() {
        return planMaxprice;
    }

    public void setPlanStype(String planStype) {
        this.planStype = planStype;
    }

    public String getPlanStype() {
        return planStype;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
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
}
