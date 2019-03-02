package com.example.admanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.admanage.entity.User;
import com.example.admanage.entity.UserAcount;
import com.example.admanage.service.UserAcountService;
import com.example.admanage.utils.CoreUtils;
import com.example.admanage.utils.ResultType;
import com.example.admanage.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/userAcount")
public class UserAcountController extends BaseController{
    @Autowired
    private UserAcountService userAcountService;

    @GetMapping(value = "/queryUserAcount")
    @ResponseBody
    public JSONObject queryUserAcount(){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"");
        User user = getLoginUserInfo();
        if(user == null){
            result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"获取用户信息失败！");
        }
        UserAcount userAcount = new UserAcount();
        userAcount.setUserId(user.getUserId());
        String date = DateUtils.format(new Date(),"yyyy-MM-dd",Locale.CHINESE);
        userAcount = userAcountService.queryUserAcountByCondition(date);
        result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"5555",userAcount);
        return result;
    }

    @PostMapping(value = "/updateUserAcount")
    @ResponseBody
    public JSONObject updateUserAcount(String blanance, String usemoney,String spendspeed,String isStart,String isUpdate,String userId,String userAcountId){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"");
        Float spendSpeed = 1.0f;
        Float blananceMoney = 0.0f;
        Float useMoney = 0.0f;
        try {
            if (StringUtils.isNotBlank(spendspeed)) {
                spendSpeed = Float.valueOf(spendspeed);
            }
            if (StringUtils.isNotBlank(blanance)) {
                blananceMoney = Float.valueOf(blanance);
            }
            if (StringUtils.isNotBlank(usemoney)) {
                useMoney = Float.valueOf(usemoney);
            }
            User user = getLoginUserInfo();
            UserAcount userAcount = new UserAcount();
            if(StringUtils.isNotBlank(userId)){
                userAcount.setUserId(Integer.valueOf(userId));
            }
            userAcount.setBlanance(blananceMoney);
            userAcount.setSpendSpeed(spendSpeed);
            //userAcount.setUseMoney(useMoney);
            if ("false".equals(isUpdate)) {
                userAcount.setCreateTime(new Date());
                userAcount.setUpdateTime(new Date());
                //更新该用户的所有账号全部停止运行
                UserAcount userAcountNew = new UserAcount();
                userAcountNew.setSpendSpeed(spendSpeed);
                userAcountNew.setUseMoney(useMoney);
                userAcountNew.setIsStart(0);
                userAcountNew.setId(Integer.valueOf(userAcountId));
                userAcountNew.setUpdateTime(new Date());
                userAcountService.updateUserAcount(userAcountNew);
                //该用户新增一条账户余额信息
                userAcountService.inserteUserAcount(userAcount);
                result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"添加成功",userAcount);
            } else {
                if(StringUtils.isNotBlank(isStart)) {
                    userAcount.setIsStart(Integer.valueOf(isStart));
                }
                userAcount.setId(Integer.valueOf(userAcountId));
                userAcount.setUpdateTime(new Date());
                userAcountService.updateUserAcount(userAcount);
                result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"更新成功");
            }
        }catch(Exception e){
            logger.info("错误"+e);
        }

        return result;
    }
}
