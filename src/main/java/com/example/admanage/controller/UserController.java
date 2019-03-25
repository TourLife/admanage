package com.example.admanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.admanage.constants.Constants;
import com.example.admanage.entity.User;
import com.example.admanage.entity.UserAcount;
import com.example.admanage.service.UserAcountService;
import com.example.admanage.service.UserService;
import com.example.admanage.utils.CoreUtils;
import com.example.admanage.utils.ResultType;
import com.example.admanage.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    @Autowired
    private UserAcountService userAcountService;

    @PostMapping(value = "/login")
    @ResponseBody
    public JSONObject login(String loginName,String userpassword){
        User user = userService.queryLogin(loginName,userpassword);
        if(user != null){
            HttpSession session = getSession();
            session.setAttribute(Constants.SESSION_USER_KEY,user);
        }
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"",user);
        return result;
    }

    @GetMapping(value = "/loginout")
    public String loginout( ){
        HttpSession session = getSession();
        session.removeAttribute(Constants.SESSION_USER_KEY);
        return "thymeleaf/login";
    }
    @GetMapping(value = "/index")
    public String index(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response){
        User user = getLoginUserInfo();
        if(user == null){
            Cookie cookie = new Cookie("jsessionid", "2jcligmgi6fh");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
            return "thymeleaf/login";
        }
        String userId = request.getParameter("userId");
        UserAcount userAcount = new UserAcount();

        Date now = new Date();
        String date = DateUtils.format(now,"yyyy-MM-dd",Locale.CHINESE);
        Integer uId = 0;
        if(StringUtils.isBlank(userId)){
            uId = user.getUserId();
        }else{
            uId = Integer.valueOf(userId);
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(calendar.DATE,1);
        String tomorrow = DateUtils.format(calendar.getTime(),"yyyy-MM-dd",Locale.CHINESE);
        //获取今天设置的所有用户账号
        List<UserAcount> userAcountList = userAcountService.queryUserAcountBy2Date(date,tomorrow,uId);
        if(userAcountList != null && !userAcountList.isEmpty()){
            //如果是主账号只取第一个
            userAcount = userAcountList.get(0);
            Integer timeSpan = getSecondTimestamp(new Date()) - getSecondTimestamp(userAcount.getUpdateTime()) ;

            if( userAcount.getIsStart() == 0){
                timeSpan = 0;
            }
            //获取当前跑动账号的余额与时间之间的兑换
            Double usemoney = multiply(timeSpan,userAcount.getSpendSpeed(),1);
            //获取当日消耗
            List<UserAcount> todayUse = userAcountService.queryUserSumAcountBy2Date(date+" 00:00:00", tomorrow+" 00:00:00", userAcount.getUserId());
            if(todayUse != null && !todayUse.isEmpty()){
                Double todayUseMoney = add(todayUse.get(0).getUseMoney(), usemoney);
                modelMap.put("todayuse",changeDecimal(todayUseMoney,1));
            }else {
                modelMap.put("todayuse",0.0);
            }
            //随着时间变化余额变化情况
            Double blanance = sub(sub(userAcount.getBlanance(),usemoney),userAcount.getUseMoney());
            usemoney = add(userAcount.getUseMoney(), usemoney);
            if(blanance <= Math.abs(0)){
                blanance = 0.0;
                usemoney = (double)userAcount.getBlanance();
            }
            modelMap.put("blanance",changeDecimal(blanance,1));
            modelMap.put("usemoney",changeDecimal(usemoney,1));
        }else{
            //获取今天消耗
            List<UserAcount> todayUse = userAcountService.queryUserSumAcountBy2Date(date+" 00:00:00", tomorrow+" 00:00:00", userAcount.getUserId());
            if(todayUse != null && !todayUse.isEmpty()){
                modelMap.put("todayuse",changeDecimal(todayUse.get(0).getUseMoney(),1));
            }else {
                modelMap.put("todayuse",0.0);
            }
            modelMap.put("blanance",0.0);
            modelMap.put("usemoney",0.0);
            userAcount = new UserAcount(uId,0.0f,1.0f,0);
        }

        //获取昨天消耗
        calendar.add(calendar.DATE,-2);
        String yesterday = DateUtils.format(calendar.getTime(),"yyyy-MM-dd",Locale.CHINESE);
        List<UserAcount> yesterdayUse = userAcountService.queryUserSumAcountBy2Date(yesterday+" 00:00:00", date+" 00:00:00", userAcount.getUserId());
        //获取当月消耗
        //获取前月的第一天
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String firstDay = DateUtils.format(calendar.getTime(),"yyyy-MM-dd",Locale.CHINESE);
        //获取前月的最后一天
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));//设置为1号,当前日期既为本月第一天
        String lastDay = DateUtils.format(calendar.getTime(),"yyyy-MM-dd",Locale.CHINESE);
        List<UserAcount> currentMonthUse = userAcountService.queryUserSumAcountBy2Date(firstDay+" 00:00:00", lastDay+" 00:00:00", userAcount.getUserId());

        if(yesterdayUse != null && !yesterdayUse.isEmpty()){
            modelMap.put("yesterday",yesterdayUse.get(0).getUseMoney());
        }else {
            modelMap.put("yesterday",0.0);
        }

        if(currentMonthUse != null && !currentMonthUse.isEmpty()){
            modelMap.put("currentMonth",currentMonthUse.get(0).getUseMoney());
        }else {
            modelMap.put("currentMonth",0.0);
        }

        //获取所有的用户(去除超管)
        List<User> userList = userService.queryUser(false);
        modelMap.put("userList",userList);
        modelMap.put("userAcount",userAcount);
        modelMap.put("currentUser",user);
        return "thymeleaf/adAdmin";
    }

    @PostMapping(value = "/edit")
    public String edit(User user){
        userService.updateUser(user);
        return "redirect:user/index";
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public JSONObject list(){
        List<User> userList = userService.queryUser(true);
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"5555",userList);
        return result;
    }

    @GetMapping(value = "/queryUser")
    @ResponseBody
    public JSONObject queryUser(){
        List<User> userList = userService.queryUser(true);
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"5555",userList);
        return result;
    }

    @PostMapping(value = "/addUser")
    public String addUser(HttpServletRequest request){
        String userName = request.getParameter("userName");
        String userSex = request.getParameter("userSex");
        String userAge = request.getParameter("userAge");
        String userPassword = request.getParameter("userPassword");
        User user = new User(userName,Integer.valueOf(userSex),userPassword,new Date());
        JSONObject result = addUserDeal(user);
        return "redirect:acountSetting";
    }

    @GetMapping(value = "/acountSetting")
    public String acountSetting(ModelMap modelMap, HttpServletRequest request){
        Integer currentPage = 1;
        User user = getLoginUserInfo();
        if(user == null){
            return "thymeleaf/login";
        }
        if(StringUtils.isNotBlank(request.getParameter("currentpage"))){
            currentPage = Integer.valueOf(request.getParameter("currentpage"));
        }
        Page page = PageHelper.startPage(currentPage, 5,true);
        List<User> userList = new ArrayList();
        if(user.getIsSuperadmin() == 0){
            user = userService.queryUserByCondition(user);
            if(user != null) {
                userList.add(user);
            }
        }else{
            userList = userService.queryUser(true);
        }
        modelMap.put("page",page.getTotal());
        modelMap.put("currentpage",currentPage);
        modelMap.put("currentUser",user);
        modelMap.put("userlist",userList);
        return "thymeleaf/acountSetting";
    }

    public JSONObject addUserDeal(User user){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"","");
        try {
            int resultUser = userService.insertUser(user);
            if (resultUser == 1) {
                result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS, "添加成功", "");
            }
        }catch(Exception e){
            logger.info(e.getStackTrace().toString());
        }

        return result;
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public JSONObject updatePassword(String userName,String userPassword,String newPassword){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"修改失败","");
        try {
            if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(userPassword)  ) {
                User user = userService.queryLogin(userName, userPassword);
                if (user != null) {
                    user.setUserPassword(newPassword);
                    int resultUser = userService.updateUser(user);
                    if (resultUser == 1) {
                        result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS, "修改成功", "");
                    }
                    logger.info("更新用户密码成功！");
                } else {
                    logger.info("更新用户密码失败！");
                }
            }
        }catch(Exception e){
            logger.info("更新用户密码失败！");
        }
        return result;
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public JSONObject updateUser(User user){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"修改失败","");
        try {
            int resultUser = userService.updateUser(user);
            if (resultUser == 1) {
                result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS, "修改成功", "");
            }
            logger.info("更新用户信息成功！");
        }catch(Exception e){
            logger.info("更新用户信息失败！"+e.getStackTrace());
        }
        return result;
    }
    @RequestMapping(value = "nameIsExit")
    @ResponseBody
    public JSONObject nameIsExit(String userName){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"该登录名已经被占用！","");
        User user = new User();
        user.setUserName(userName);
        user = userService.queryUserByCondition(user);
        if(user == null){
            result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,"","");
        }
        return result;
    }
}
