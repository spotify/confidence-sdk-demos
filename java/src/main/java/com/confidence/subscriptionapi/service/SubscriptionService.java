package com.confidence.subscriptionapi.service;

import com.confidence.subscriptionapi.model.Subscription;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final List<Subscription> subscriptions = List.of(
        new Subscription(1L, "Basic Plan", "active", "na", LocalDateTime.now().minusDays(30), 9.99),
        new Subscription(2L, "Pro Plan", "active", "na", LocalDateTime.now().minusDays(15), 19.99),
        new Subscription(3L, "Enterprise Plan", "active", "eu", LocalDateTime.now().minusDays(60), 49.99),
        new Subscription(4L, "Starter Plan", "cancelled", "na", LocalDateTime.now().minusDays(90), 4.99),
        new Subscription(5L, "Premium Plan", "active", "ap", LocalDateTime.now().minusDays(7), 29.99),
        new Subscription(6L, "Business Plan", "active", "sa", LocalDateTime.now().minusDays(20), 24.99),
        new Subscription(7L, "Regional Plan", "active", "af", LocalDateTime.now().minusDays(45), 14.99)
    );

    public List<Subscription> getSubscriptions(String region) {
        if (region == null || region.isEmpty()) {
            return subscriptions;
        }
        return subscriptions.stream()
                .filter(subscription -> subscription.getRegion().equals(region))
                .collect(Collectors.toList());
    }
}