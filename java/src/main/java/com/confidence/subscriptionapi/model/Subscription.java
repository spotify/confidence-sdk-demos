package com.confidence.subscriptionapi.model;

import java.time.LocalDateTime;

public class Subscription {
    private Long id;
    private String name;
    private String status;
    private String region;
    private LocalDateTime createdAt;
    private Double price;

    public Subscription() {}

    public Subscription(Long id, String name, String status, String region, LocalDateTime createdAt, Double price) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.region = region;
        this.createdAt = createdAt;
        this.price = price;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}