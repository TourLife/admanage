package com.example.admanage.service.Impl;

import com.example.admanage.dao.PlanDao;
import com.example.admanage.dao.PlanTypeDao;
import com.example.admanage.entity.Plan;
import com.example.admanage.entity.PlanType;
import com.example.admanage.service.PlanService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanTypeDao planTypeDao;

    @Autowired
    private PlanDao planDao;

    @Override
    public List<PlanType> queryType() {
        return planTypeDao.queryType();
    }

    @Override
    public List<Plan> queryPlanList(Plan plan) {
        List<Plan> planList = planDao.queryPlanList(plan);
        return planList;
    }

    @Override
    public Plan queryPlan(Integer planId) {
        return planDao.queryPlan(planId);
    }

    @Override
    public Plan addPlan(Plan plan) {
        return planDao.addPlan(plan);
    }

    @Override
    public Plan updatePlan(Plan plan) {
        return planDao.updatePlan(plan);
    }
}
