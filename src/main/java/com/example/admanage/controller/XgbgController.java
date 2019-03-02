package com.example.admanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.admanage.entity.DataStatistics;
import com.example.admanage.entity.User;
import com.example.admanage.entity.UserAcount;
import com.example.admanage.service.DataStatisticsService;
import com.example.admanage.service.UserAcountService;
import com.example.admanage.service.UserService;
import com.example.admanage.utils.CoreUtils;
import com.example.admanage.utils.DataStatisticsQuartz;
import com.example.admanage.utils.ResultType;
import freemarker.template.utility.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequestMapping("/xgbg")
@Controller
public class XgbgController extends BaseController {

    @Autowired
    private DataStatisticsService dataStatisticsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAcountService userAcountService;

    @Autowired
    private DataStatisticsQuartz dataStatisticsQuartz;

    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap){
        User user = getLoginUserInfo();
        if(user == null){
            return "thymeleaf/login";
        }
        modelMap.put("currentUser",user);
        return "thymeleaf/xgbg";
    }

    @PostMapping(value = "/dataStatistics")
    @ResponseBody
    public JSONObject userAcountStatistics(String startTime, String endTime){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"失败","");
        String date = DateUtils.format(new Date(),"yyyy-MM-dd",Locale.CHINESE);
        List<DataStatistics> dataStatisticsList = dataStatisticsService.queryDataBy2Date(startTime,endTime);
        result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"成功",dataStatisticsList);
        return result;
    }

    @RequestMapping(value = "/setUserMoney")
    public String setUserMoney(ModelMap modelMap){
        List<User> userList = userService.queryUser(true);
        modelMap.put("userList",userList);
        return "thymeleaf/setusermoney";
    }

    @PostMapping(value = "/addUserMoney")
    @ResponseBody
    public JSONObject addUserMoney(ModelMap modelMap, UserAcount userAcount, HttpServletRequest request){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"失败","");
        try {
            String date = request.getParameter("date");
            String[] dates = date.replaceAll(" ","").split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,Integer.valueOf(dates[0]));
            calendar.set(Calendar.WEEK_OF_MONTH,Integer.valueOf(dates[1]));
            calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(dates[2]));

            userAcount.setCreateTime(calendar.getTime());
            userAcount.setUpdateTime(calendar.getTime());
            userAcount.setSpendSpeed(1);
            int data = userAcountService.inserteUserAcount(userAcount);
            if(data == 1) {
                //立马执行定时任务
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(Calendar.YEAR,Integer.valueOf(dates[0]));
                endCalendar.set(Calendar.WEEK_OF_MONTH,Integer.valueOf(dates[1]));
                endCalendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(dates[2])+1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dataStatisticsQuartz.setStartDate(sdf.format(calendar.getTime()));
                dataStatisticsQuartz.setEndDate(sdf.format(endCalendar.getTime()));
                dataStatisticsQuartz.play();
                result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS, "成功");
            }
        }catch (Exception e){
            logger.info("插入消耗金额数据失败！"+e.getMessage());
        }
        return result;
    }

}
