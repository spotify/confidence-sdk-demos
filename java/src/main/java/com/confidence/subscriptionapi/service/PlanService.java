package com.confidence.subscriptionapi.service;

import com.confidence.subscriptionapi.model.Plan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    private final List<Plan> plans = List.of(
        new Plan(1L, "Basic", "Basic plan with essential features", "$9.99", "monthly",
                List.of("na", "eu", "ap"),
                List.of("1 user", "5GB storage", "Email support")),
        new Plan(2L, "Pro", "Professional plan for growing teams", "$19.99", "monthly",
                List.of("na", "eu", "ap", "sa"),
                List.of("5 users", "50GB storage", "Priority support", "Advanced analytics")),
        new Plan(3L, "Starter", "Starter plan for individuals", "$4.99", "monthly",
                List.of("na", "sa", "af"),
                List.of("1 user", "1GB storage", "Community support")),
        new Plan(4L, "Premium", "Premium plan with all features", "$29.99", "monthly",
                List.of("ap", "eu", "na"),
                List.of("10 users", "100GB storage", "Priority support", "Advanced features", "API access")),
        new Plan(5L, "Business", "Business plan for growing companies", "$39.99", "monthly",
                List.of("eu", "ap", "sa"),
                List.of("15 users", "200GB storage", "Priority support", "Business features", "Integrations")),
        new Plan(6L, "Team", "Team plan for collaborative work", "$24.99", "monthly",
                List.of("na", "eu", "af"),
                List.of("8 users", "75GB storage", "Team support", "Collaboration tools")),
        new Plan(7L, "Regional", "Regional plan for local markets", "$14.99", "monthly",
                List.of("sa", "af", "ap"),
                List.of("3 users", "25GB storage", "Regional support", "Local features")),
        new Plan(8L, "Global", "Global plan for worldwide access", "$49.99", "monthly",
                List.of("na", "eu", "ap", "sa", "af"),
                List.of("Unlimited users", "1TB storage", "24/7 support", "Global features", "Multi-region"))
    );

    public List<Plan> getPlans(String region) {
        List<Plan> resultPlans = new java.util.ArrayList<>(plans);

        // Apply region filtering to final result
        if (region == null || region.isEmpty()) {
            return resultPlans;
        }
        return resultPlans.stream()
                .filter(plan -> plan.getRegions().contains(region))
                .collect(Collectors.toList());
    }
}