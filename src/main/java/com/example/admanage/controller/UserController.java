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
import java.util.Date;
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

        String date = DateUtils.format(new Date(),"yyyy-MM-dd",Locale.CHINESE);
        Integer uId = 0;
        if(StringUtils.isBlank(userId)){
            uId = user.getUserId();
        }else{
            uId = Integer.valueOf(userId);
        }
        //获取今天设置的所有用户账号
        List<UserAcount> userAcountList = userAcountService.queryUserAcountBy2Date(date+" 00:00:00",date+" 23:59:59",uId);
        if(userAcountList != null && !userAcountList.isEmpty()){
            //如果是主账号只取第一个
            userAcount = userAcountList.get(0);
            Integer timeSpan = getSecondTimestamp(new Date()) - getSecondTimestamp(userAcount.getUpdateTime()) ;

            if( userAcount.getIsStart() == 0){
                timeSpan = 0;
            }
            Double usemoney = div((double)timeSpan,userAcount.getSpendSpeed(),1);
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
            modelMap.put("blanance",0.0);
            modelMap.put("usemoney",0.0);
            userAcount = new UserAcount(uId,0.0f,1.0f,0);
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
        User user = new User(userName,Integer.valueOf(userSex),Integer.valueOf(userAge),userPassword,new Date());
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
    public JSONObject updateUser(String userName,String userId){
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.OPERATE_ERROR,"修改失败","");
        try {
            User user = new User();
            user.setUserId(Integer.valueOf(userId));
            user.setUserName(userName);
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
}
