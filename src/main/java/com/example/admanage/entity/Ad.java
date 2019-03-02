package com.example.admanage.entity;

import com.example.admanage.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class Ad {

    private Integer adId;

    private Integer planId;

    private String planTitle;

    private String adTitle;

    private String adLogo;

    private String adType;

    private Integer[] planIdStr;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer status;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    public Ad(String adTitle,Integer planId, String planTitle, String adType, Integer status) {
        this.adTitle = adTitle;
        this.planId = planId;
        this.planTitle = planTitle;
        this.adType = adType;
        this.status = status;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Ad(){

    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdLogo() {
        return adLogo;
    }

    public void setAdLogo(String adLogo) {
        this.adLogo = adLogo;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer[] getPlanIdStr() {
        return planIdStr;
    }

    public void setPlanIdStr(Integer[] planIdStr) {
        this.planIdStr = planIdStr;
    }
}
