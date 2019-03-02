package com.example.admanage.dao;

import com.example.admanage.entity.Plan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanDao {

    public List<Plan> queryPlanList(Plan plan);

    public Plan queryPlan(@Param("planId")Integer planId);

    public Plan addPlan(Plan plan);

    public Plan updatePlan(Plan plan);
}
