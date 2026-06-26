package com.confidence.subscriptionapi.config;

import com.spotify.confidence.Confidence;
import com.spotify.confidence.ConfidenceFeatureProvider;
import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class OpenFeatureConfig {

    @PostConstruct
    public void initializeOpenFeature() {
        // Configure OpenFeature provider with Confidence on application startup
        ConfidenceFeatureProvider provider = new ConfidenceFeatureProvider(
                Confidence.builder("ofQ5I97NrLyORWHRACpDYPRphlLviCyU")
        );
        OpenFeatureAPI.getInstance().setProviderAndWait(provider);
    }

    @Bean
    public Client openFeatureClient() {
        // @Bean is singleton by default in Spring
        return OpenFeatureAPI.getInstance().getClient();
    }
}