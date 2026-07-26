package com.confidence.subscriptionapi.controller;

import com.confidence.subscriptionapi.model.Plan;
import com.confidence.subscriptionapi.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

    @Autowired
    private PlanService planService;

    @GetMapping("/plans")
    public List<Plan> getPlans(@RequestParam(required = false) String region) {
        return planService.getPlans(region);
    }
}