package com.example.admanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.admanage.constants.Constants;
import com.example.admanage.entity.Plan;
import com.example.admanage.entity.PlanType;
import com.example.admanage.entity.User;
import com.example.admanage.service.PlanService;
import com.example.admanage.service.UserService;
import com.example.admanage.utils.CoreUtils;
import com.example.admanage.utils.ResultType;
import com.example.admanage.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/pm")
public class PlanManageController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private PlanService planService;

    @GetMapping("/index")
    public String index(ModelMap modelMap,HttpServletRequest request){
        Integer currentPage = 1;
        String planStype = request.getParameter("planstype");
        User user = getLoginUserInfo();
        if(user == null){
            return "thymeleaf/login";
        }
        String userId = request.getParameter("userId");
        List<PlanType> planTypeList = planService.queryType();
        Plan plan = new Plan();
        if(StringUtils.isNotBlank(request.getParameter("currentpage"))){
            currentPage = Integer.valueOf(request.getParameter("currentpage"));
        }
        if(StringUtils.isNotBlank(planStype) && !"00".equalsIgnoreCase(planStype)){
            plan.setPlanStype(planStype);
        }
        if(StringUtils.isBlank(userId)){
            if(user.getIsSuperadmin() == 0) {
                plan.setPlanCreateId(user.getUserId());
            }
        }else{
            if(!"0".equals(userId)) {
                plan.setPlanCreateId(Integer.valueOf(userId));
            }
        }

        Page page = PageHelper.startPage(currentPage, 5,true);
        List<Plan> planList = planService.queryPlanList(plan);
        //获取所有的用户(去除超管)
        List<User> userList = userService.queryUser(false);
        modelMap.put("selectUser",userId);
        modelMap.put("userList",userList);
        modelMap.put("page",page.getTotal());
        modelMap.put("currentpage",currentPage);
        modelMap.put("planTypeList",planTypeList);
        modelMap.put("planStype",planStype);
        modelMap.put("planList",planList);
        modelMap.put("currentUser",user);
        return "thymeleaf/planManage";
    }

    @PostMapping("/update")
    public String update(ModelMap modelMap, HttpServletRequest request,@RequestParam("planlogo") MultipartFile planlogos){
        String planId = request.getParameter("planid");
        String planCreateId = request.getParameter("plancreateid");
        String planCreateName = request.getParameter("plancreatename");
        String planTitle = request.getParameter("plantitle");
        String planBtype = request.getParameter("planbtype");
        String planStyle = request.getParameter("planstyle");
        String planPrice = request.getParameter("planprice");
        String planMaxprice = request.getParameter("planmaxprice");
        String planStatus = request.getParameter("status");
        String result = handleFileUpload(planlogos);
        Plan plan = new Plan(Integer.valueOf(planCreateId),planCreateName,planTitle,planBtype,planStyle,Float.valueOf(planPrice),Float.valueOf(planMaxprice),Integer.valueOf(planStatus));
        if(!"fail".equalsIgnoreCase(result)){
            plan.setPlanLogo(result);
        }
        if(StringUtils.isNotBlank(planId)){
            plan.setPlanId(Integer.valueOf(planId));
            plan.setUpdateTime(new Date());
            planService.updatePlan(plan);
        }else{
            plan.setCreateTime(new Date());
            plan.setUpdateTime(new Date());
            planService.addPlan(plan);
        }
        return "redirect:index";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request){
        String planId = request.getParameter("planid");
        Plan plan = new Plan();
        if(StringUtils.isNotBlank(planId)){
            plan.setPlanId(Integer.valueOf(planId));
            plan.setPlanStatus(2);
            planService.updatePlan(plan);
        }
        return "redirect:index";
    }

    @PostMapping("/getPlan")
    @ResponseBody
    public JSONObject getPlan(String planId){
        Plan plan = new Plan();
        if(StringUtils.isNotBlank(planId)){
            plan = planService.queryPlan(Integer.valueOf(planId));
        }
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,plan);
        return result;
    }

    public static void main(String[] args){
        URL url =  PlanManageController.class.getClassLoader().getResource("");
        System.out.println(File.pathSeparator);
    }
}
