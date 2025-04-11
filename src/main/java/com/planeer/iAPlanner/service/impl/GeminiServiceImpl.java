package com.planeer.iAPlanner.service.impl;

import com.planeer.iAPlanner.model.dto.GeminiResponseDTO;
import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import com.planeer.iAPlanner.service.GeminikService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class GeminiServiceImpl implements GeminikService {

    private static final String PROMPT_TEMPLATE =
            "Analise estas informações de agendamento e forneça:\n" +
                    "1. Lembretes importantes para os participantes\n" +
                    "2. Orientação sobre como chegar ao local\n" +
                    "3. Melhor rota considerando pontos de referência\n" +
                    "4. Informações adicionais relevantes\n\n" +
                    "Dados do agendamento:\n" +
                    "- Local: %s\n" +
                    "- Ponto de referência: %s\n" +
                    "- Detalhes: %s";

    @Value("${gemini.base.url}")
    private String baseUrl;

    @Value("${gemini.endpoint}")
    private String endpoint;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final RestTemplate restTemplate;

    public GeminiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GeminiResponseDTO callGeminiApi(ScheduleDTO scheduleDTO) {
        // Verificar modelos disponíveis
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
        } catch (Exception e) {
            return createErrorResponse("Erro ao verificar modelos disponíveis: " + e.getMessage());
        }

        // Construir o prompt com os campos disponíveis no ScheduleDTO
        String formattedPrompt = String.format(PROMPT_TEMPLATE,
                scheduleDTO.getLocalAddress(),
                scheduleDTO.getReferencePoint(),
                scheduleDTO.getDescription());

        // Configurar headers para Gemini API
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Corpo da requisição para Gemini
        String requestBody = buildGeminiRequestBody(formattedPrompt);

        // URL completa
        String apiUrl = baseUrl + (baseUrl.endsWith("/") ? "" : "/") + endpoint;

        HttpEntity<String> geminiRequestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    geminiRequestEntity,
                    String.class);

            return mapGeminiResponseToDto(response.getBody());
        } catch (Exception e) {
            // Fallback em caso de erro
            return createErrorResponse(e.getMessage());
        }
    }

    private String buildGeminiRequestBody(String prompt) {
        return String.format("""
    {
        "model": "%s",
        "contents": [{
            "parts": [{
                "text": "%s"
            }]
        }],
        "generationConfig": {
            "temperature": 0.7,
            "maxOutputTokens": 1000
        }
    }
    """, model, prompt.replace("\"", "\\\""));
    }

    private GeminiResponseDTO mapGeminiResponseToDto(String apiResponse) {
        GeminiResponseDTO geminiResponseDTO = new GeminiResponseDTO();

        try {
            // Extração básica - adapte conforme sua necessidade
            String content = apiResponse.contains("\"text\":")
                    ? apiResponse.split("\"text\":")[1].split("\"")[1]
                    : "Resposta não estruturada: " + apiResponse;

            // Distribui o conteúdo nos campos (você pode aprimorar esta lógica)
            geminiResponseDTO.setReminder("Lembrete: " + content);
            geminiResponseDTO.setAddress("Local: " + content);
            geminiResponseDTO.setBestRoute("Rota sugerida: " + content);
            geminiResponseDTO.setAdditionalInfo(content);

        } catch (Exception e) {
            geminiResponseDTO = createErrorResponse("Erro ao processar: " + e.getMessage());
        }

        return geminiResponseDTO;
    }

    private GeminiResponseDTO createErrorResponse(String error) {
        GeminiResponseDTO response = new GeminiResponseDTO();
        response.setReminder("Erro na API");
        response.setAdditionalInfo(error);
        return response;
    }
}