package com.planeer.iAPlanner.service.impl;

import com.planeer.iAPlanner.service.DeepSeekService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    private final RestTemplate restTemplate;

    public DeepSeekServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callDeepSeekApi(String endpoint, Object requestPayload) {
        String apiUrl = "https://api.deepseek.com/" + endpoint;
        return restTemplate.postForObject(apiUrl, requestPayload, String.class);
    }
}