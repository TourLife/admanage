package com.example.admanage.service;

import com.example.admanage.entity.Plan;
import com.example.admanage.entity.PlanType;
import com.github.pagehelper.Page;

import java.util.List;

public interface PlanService {

    public List<PlanType> queryType();

    public List<Plan> queryPlanList(Plan plan);

    public Plan queryPlan(Integer planId);

    public Plan addPlan(Plan plan);

    public Plan updatePlan(Plan plan);
}
