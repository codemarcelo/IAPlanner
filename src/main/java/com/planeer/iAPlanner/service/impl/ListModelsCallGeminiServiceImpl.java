package com.planeer.iAPlanner.service.impl;

import com.planeer.iAPlanner.service.ListModelsCallGeminiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ListModelsCallGeminiServiceImpl implements ListModelsCallGeminiService {

    @Value("${gemini.model}")
    private String model;

    @Value("${gemini.base.url}")
    private String baseUrl;

    @Value("${gemini.endpoint}")
    private String endpoint;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ListModelsCallGeminiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> verifyModelAvailability() {
        String listModelsUrl = baseUrl + "/v1beta/models";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-goog-api-key", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> modelResponse = restTemplate.exchange(
                    listModelsUrl,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            // Exibir os modelos disponíveis no log
            System.out.println("Modelos disponíveis: " + modelResponse.getBody());

            // Verificar se o modelo desejado está disponível
            if (!modelResponse.getBody().contains("\"" + model + "\"")) {
                throw new IllegalArgumentException("O modelo '" + model + "' não está disponível.");
            }

            return modelResponse; // Retorna a resposta da requisição
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar modelos disponíveis: " + e.getMessage());
        }
    }


}
