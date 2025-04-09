package com.planeer.iAPlanner.service;

import org.springframework.stereotype.Service;

@Service
public interface DeepSeekService {

    String callDeepSeekApi(String endpoint, Object requestPayload);
}
