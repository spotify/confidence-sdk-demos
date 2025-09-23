package com.confidence.subscriptionapi.model;

import java.util.List;

public class Plan {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String billingCycle;
    private String region;
    private List<String> features;

    public Plan() {}

    public Plan(Long id, String name, String description, Double price, String billingCycle, String region, List<String> features) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.billingCycle = billingCycle;
        this.region = region;
        this.features = features;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}