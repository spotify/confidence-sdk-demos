package com.confidence.subscriptionapi.model;

import java.util.List;

public class Plan {
    private Long id;
    private String name;
    private String description;
    private String price;
    private String billingCycle;
    private List<String> regions;
    private List<String> features;

    public Plan() {}

    public Plan(Long id, String name, String description, String price, String billingCycle, List<String> regions, List<String> features) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.billingCycle = billingCycle;
        this.regions = regions;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}