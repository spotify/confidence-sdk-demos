package com.confidence.subscriptionapi.service;

import com.confidence.subscriptionapi.model.Plan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    private final List<Plan> plans = List.of(
        new Plan(1L, "Basic", "Basic plan with essential features", 9.99, "monthly", "na",
                List.of("1 user", "5GB storage", "Email support")),
        new Plan(2L, "Pro", "Professional plan for growing teams", 19.99, "monthly", "na",
                List.of("5 users", "50GB storage", "Priority support", "Advanced analytics")),
        new Plan(3L, "Enterprise", "Enterprise plan for large organizations", 49.99, "monthly", "eu",
                List.of("Unlimited users", "500GB storage", "24/7 phone support", "Custom integrations", "SLA")),
        new Plan(4L, "Starter", "Starter plan for individuals", 4.99, "monthly", "na",
                List.of("1 user", "1GB storage", "Community support")),
        new Plan(5L, "Premium", "Premium plan with all features", 29.99, "monthly", "ap",
                List.of("10 users", "100GB storage", "Priority support", "Advanced features", "API access")),
        new Plan(6L, "Regional", "Regional plan for local markets", 14.99, "monthly", "sa",
                List.of("3 users", "25GB storage", "Regional support", "Local features")),
        new Plan(7L, "Continental", "Continental plan for African markets", 12.99, "monthly", "af",
                List.of("2 users", "15GB storage", "Local support", "Regional features"))
    );

    public List<Plan> getPlans(String region) {
        if (region == null || region.isEmpty()) {
            return plans;
        }
        return plans.stream()
                .filter(plan -> plan.getRegion().equals(region))
                .collect(Collectors.toList());
    }
}