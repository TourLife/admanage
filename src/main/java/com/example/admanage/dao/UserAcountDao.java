package com.example.admanage.dao;

import com.example.admanage.entity.UserAcount;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserAcountDao {

    /**
     * 根据条件查询用户账户余额
     * @param startDate
     * @param endDate
     * @return num
     */
    public List<UserAcount> queryUserAcountBy2Date(@Param("startDate")String startDate, @Param("endDate")String endDate,@Param("userId")Integer userId);

    /**
     * 根据条件查询用户
     * @param startTime
     * @param endTime
     * @return num
     */
    public UserAcount queryUserAcountByCondition(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 更新用户信息
     *
     * @return num
     */
    public int updateUserAcount(@Param("userAcount") Map userAcount);

    /**
     * 插入用户账户余额信息
     *
     * @return num
     */
    public int inserteUserAcount(UserAcount userAcount);

    /**
     * 批量更新用户信息
     *
     * @return num
     */
    public int batchUpdateUserAcount(@Param("userAcountList") List<UserAcount> userAcountList);

    /**
     * 批量插入用户账户余额信息
     *
     * @return num
     */
    public int batchInserteUserAcount(@Param("userAcountList")List<UserAcount> userAcountList);

}
