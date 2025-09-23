package com.confidence.subscriptionapi;

import dev.openfeature.sdk.OpenFeatureAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.spotify.confidence.ConfidenceFeatureProvider;
import com.spotify.confidence.Confidence;

@SpringBootApplication
public class SubscriptionApiApplication {

    public static void main(String[] args) {
        // Configure OpenFeature provider with Confidence
        ConfidenceFeatureProvider provider = new ConfidenceFeatureProvider(
          Confidence.builder("BEFwpwAWFupTtxEyt7ukdIc5hwAC7Lxc")
          );
        OpenFeatureAPI.getInstance().setProviderAndWait(provider);
        SpringApplication.run(SubscriptionApiApplication.class, args);
    }
}