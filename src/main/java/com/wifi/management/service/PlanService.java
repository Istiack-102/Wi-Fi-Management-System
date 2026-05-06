package com.wifi.management.service;

import com.wifi.management.database_operation.PlanDAO;
import com.wifi.management.model.Plan;
import java.util.List;
import java.util.stream.Collectors;

public class PlanService {

    private PlanDAO planDAO;

    public PlanService() {
        this.planDAO = new PlanDAO();
    }

    // সংশোধিত মেথড
    public boolean updatePlan(int planId, String name, int speed, double price) {
        Plan plan = new Plan();
        plan.setPlanId(planId);
        plan.setPlanName(name);
        plan.setSpeedLimitMbps(speed);
        plan.setMonthlyPrice(price);

        // DAO মেথডটি এখন সঠিক আর্গুমেন্ট পাবে
        return planDAO.updatePlan(plan);
    }

    public List<Plan> getAvailablePlans() {
        return planDAO.getAllPlans();
    }

    public Plan getPlanDetails(int planId) {
        return planDAO.getPlanById(planId);
    }

    public List<Plan> getPlansByMinSpeed(int minMbps) {
        List<Plan> allPlans = planDAO.getAllPlans();
        return allPlans.stream()
                .filter(plan -> plan.getSpeedLimitMbps() >= minMbps)
                .collect(Collectors.toList());
    }

    public String getFormattedPlanInfo(Plan plan) {
        if (plan == null) return "Plan not found";
        return String.format("%s - %d Mbps at ৳%.2f/month",
                plan.getPlanName(), plan.getSpeedLimitMbps(), plan.getMonthlyPrice());
    }
}