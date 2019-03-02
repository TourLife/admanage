package com.example.admanage.service;

import com.example.admanage.entity.UserAcount;

import java.util.List;

public interface UserAcountService {

    /**
     * 根据条件查询用户账户余额
     * @param startDate
     * @param endDate
     * @return num
     */
    public List<UserAcount> queryUserAcountBy2Date(String startDate, String endDate,Integer userId);

    /**
     * 根据条件查询用户账户余额
     * @param date
     * @return num
     */
    public UserAcount queryUserAcountByCondition(String date);

    /**
     * 更新用户账户余额信息
     *
     * @return num
     */
    public int updateUserAcount(UserAcount userAcount);

    /**
     * 插入用户账户余额信息
     *
     * @return num
     */
    public int inserteUserAcount(UserAcount userAcount);

    /**
     * 更新用户账户余额信息
     *
     * @return num
     */
    public int batchUpdateUserAcount(List<UserAcount> userAcountlist);

    /**
     * 插入用户账户余额信息
     *
     * @return num
     */
    public int batchInserteUserAcount(List<UserAcount> userAcountlist);
}
