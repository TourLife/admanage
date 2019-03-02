package com.example.admanage.service.Impl;

import com.example.admanage.dao.UserAcountDao;
import com.example.admanage.entity.UserAcount;
import com.example.admanage.service.UserAcountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.DateUtils;

import java.util.*;

@Service
public class UserAcountServiceImpl implements UserAcountService {

    @Autowired
    private UserAcountDao userAcountDao;

    @Override
    public List<UserAcount> queryUserAcountBy2Date(String startDate, String endDate,Integer userId){
        List<UserAcount> result = userAcountDao.queryUserAcountBy2Date(startDate+" 00:00:00",endDate+" 00:00:00",userId);
        return result;
    }

    @Override
    public UserAcount queryUserAcountByCondition(String date) {
        UserAcount result = userAcountDao.queryUserAcountByCondition(date+" 00:00:00",date+" 23:59:59");
        return result;
    }

    @Transactional
    @Override
    public int updateUserAcount(UserAcount userAcount) {
        int flagNum = 0;
        if(userAcount.getUserId() != 0 || userAcount.getId() != 0){
            try {
                String date = DateUtils.format(new Date(),"yyyy-MM-dd",Locale.CHINESE);
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("startTime",date+" 00:00:00");
                map.put("endTime",date+" 23:59:59");
                map.put("userAcount",userAcount);
                int result = userAcountDao.updateUserAcount(map);
                if (result == 1){
                    flagNum = result;
                }
            }catch (Exception e){
                throw new RuntimeException("更新用户账户余额信息失败！"+e.getMessage());
            }
        }
        return flagNum;
    }

    /**
     * 插入用户账户余额信息
     *
     * @return num
     */
    public int inserteUserAcount(UserAcount userAcount){
        int result = userAcountDao.inserteUserAcount(userAcount);
        return result;
    }

    @Override
    public int batchUpdateUserAcount(List<UserAcount> userAcountlist) {
        int flagNum = 0;
        if(userAcountlist != null && !userAcountlist.isEmpty()){
            try {
                int result = userAcountDao.batchUpdateUserAcount(userAcountlist);
                if (result == 1){
                    flagNum = result;
                }
            }catch (Exception e){
                throw new RuntimeException("更新用户账户余额信息失败！"+e.getMessage());
            }
        }
        return flagNum;
    }

    @Override
    public int batchInserteUserAcount(List<UserAcount> userAcountlist) {
        int result = userAcountDao.batchInserteUserAcount(userAcountlist);
        return result;
    }
}
