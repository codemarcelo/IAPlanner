package com.planeer.iAPlanner.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.base.url}")
    private String apiUrl;

    @Value("${gemini.model}")
    private String modelName;

    @Value("${gemini.endpoint}")
    private String endpoint;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getModelName() {
        return modelName;
    }

    public String getEndpoint() {
        return endpoint;
    }
}