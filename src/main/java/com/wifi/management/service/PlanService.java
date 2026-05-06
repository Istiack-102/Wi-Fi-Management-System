package com.wifi.management.service;

import com.wifi.management.database_operation.PlanDAO;
import com.wifi.management.model.Plan;
import java.util.List;
import java.util.stream.Collectors;

public class PlanService {

    private PlanDAO planDAO;

    public boolean updatePlan(int planId, int speed, double price) {
        return planDAO.updatePlan(planId, speed, price);
    }

    public PlanService() {
        this.planDAO = new PlanDAO();
    }

    // Fetches all plans from the DAO for display
    public List<Plan> getAvailablePlans() {
        return planDAO.getAllPlans();
    }

    // Returns a specific plan's details by its ID
    public Plan getPlanDetails(int planId) {
        return planDAO.getPlanById(planId);
    }

    // Filters plans to show only those within a specific speed range
    public List<Plan> getPlansByMinSpeed(int minMbps) {
        List<Plan> allPlans = planDAO.getAllPlans();
        return allPlans.stream()
                .filter(plan -> plan.getSpeedLimitMbps() >= minMbps)
                .collect(Collectors.toList());
    }

    // Business Logic: Formats plan details for a clean GUI display
    public String getFormattedPlanInfo(Plan plan) {
        if (plan == null) return "Plan not found";
        return String.format("%s - %d Mbps at ৳%.2f/month",
                plan.getPlanName(), plan.getSpeedLimitMbps(), plan.getMonthlyPrice());
    }
}