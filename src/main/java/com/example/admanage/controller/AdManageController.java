package com.example.admanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.admanage.entity.Ad;
import com.example.admanage.entity.Plan;
import com.example.admanage.entity.PlanType;
import com.example.admanage.entity.User;
import com.example.admanage.service.AdManageService;
import com.example.admanage.service.PlanService;
import com.example.admanage.utils.CoreUtils;
import com.example.admanage.utils.ResultType;
import com.example.admanage.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping(value = "/am")
@Controller
public class AdManageController extends BaseController {

    @Autowired
    private PlanService planService;

    @Autowired
    private AdManageService adManageService;

    @GetMapping("/index")
    public String index(ModelMap modelMap, HttpServletRequest request){
        Integer currentPage = 1;
        String adType = request.getParameter("adtype");
        String planId = request.getParameter("planId");
        String userId = request.getParameter("userId");
        User user = getLoginUserInfo();
        if(user == null){
            return "thymeleaf/login";
        }

        Ad ad = new Ad();
        ad.setStatus(0);
        if(StringUtils.isNotBlank(request.getParameter("currentpage"))){
            currentPage = Integer.valueOf(request.getParameter("currentpage"));
        }
        if(StringUtils.isNotBlank(adType) && !"00".equalsIgnoreCase(adType)){
            ad.setAdType(adType);
            modelMap.put("adtype",adType);
        }

        Plan plan = new Plan();
        if(user.getIsSuperadmin() == 0) {
            plan.setPlanCreateId(user.getUserId());
        }
        //获取当前登录用户的所有计划
        List<Plan> planList = planService.queryPlanList(plan);

        List<Plan> planLists  = new ArrayList();
        if(StringUtils.isNotBlank(planId) && !"0".equals(planId)){
            ad.setPlanId(Integer.valueOf(planId));
            plan.setPlanId(Integer.valueOf(planId));
        }
        //获取当前所选择计划
        planLists = planService.queryPlanList(plan);
        //获取当前所有计划的PlanId
        if(planLists != null && !planLists.isEmpty()){
            Integer[] planIdStr = new Integer[planLists.size()];
            for(int i=0;i<planLists.size();i++){
                planIdStr[i] = planLists.get(i).getPlanId();
            }
            ad.setPlanIdStr(planIdStr);
        }
        Page page = PageHelper.startPage(currentPage, 5,true);
        List<Ad> adList = adManageService.queryAdList(ad);
        modelMap.put("planlist",planList);
        modelMap.put("adlist",adList);
        modelMap.put("selectPlan",planId);
        modelMap.put("page",page.getTotal());
        modelMap.put("currentpage",currentPage);
        modelMap.put("currentUser",user);
        return "thymeleaf/adManage";
    }

    @PostMapping("/update")
    public String update(ModelMap modelMap, HttpServletRequest request,@RequestParam("adlogo") MultipartFile adlogo){
        String adId = request.getParameter("adid");
        Integer planId = 1;
        String adTitle = request.getParameter("adtitle");
        String planTitle = request.getParameter("plantitle");
        String adType = request.getParameter("adtype");
        String status = request.getParameter("status");
        String result = handleFileUpload(adlogo);

        if(StringUtils.isNotBlank(request.getParameter("planid"))) {
            planId = Integer.valueOf(request.getParameter("planid"));
        }
        Ad ad = new Ad(adTitle,planId,planTitle,adType,Integer.valueOf(status));
        if(!"fail".equalsIgnoreCase(result)){
            ad.setAdLogo(result);
        }
        if(StringUtils.isNotBlank(adId)){
            ad.setAdId(Integer.valueOf(adId));
            ad.setUpdateTime(new Date());
            adManageService.updateAd(ad);
        }else{
            ad.setCreateTime(new Date());
            adManageService.addAd(ad);
        }
        return "redirect:index";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request){
        String adId = request.getParameter("adId");
        Ad ad = new Ad();
        if(StringUtils.isNotBlank(adId)){
            ad.setAdId(Integer.valueOf(adId));
            ad.setStatus(2);
            ad.setPlanId(0);
            adManageService.updateAd(ad);
        }
        return "redirect:index";
    }

    @PostMapping("/getAd")
    @ResponseBody
    public JSONObject getPlan(String adId){
        Ad ad = new Ad();
        if(StringUtils.isNotBlank(adId)){
            ad = adManageService.queryAd(Integer.valueOf(adId));
        }
        JSONObject result = CoreUtils.createResultJson(ResultType.SimpleResultType.SUCCESS,ad);
        return result;
    }
}
