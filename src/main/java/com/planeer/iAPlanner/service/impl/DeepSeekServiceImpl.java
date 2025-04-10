package com.planeer.iAPlanner.service.impl;

import com.planeer.iAPlanner.model.dto.DeepSeekResponseDTO;
import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import com.planeer.iAPlanner.service.DeepSeekService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    @Value("${deepseek.base.url}")
    private String endpointDeepSeek;

    @Value("${deepseek.endpoint}")
    private String baseUrl;


    private final RestTemplate restTemplate;

    public DeepSeekServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public DeepSeekResponseDTO callDeepSeekApi(ScheduleDTO scheduleDTO) {

        String apiUrl = baseUrl + endpointDeepSeek;

        // Faz a chamada para a API e deserializa a resposta
        DeepSeekResponseDTO response = restTemplate.postForObject(apiUrl, scheduleDTO, DeepSeekResponseDTO.class);

        // Retorna a resposta processada
        return response;
    }
}