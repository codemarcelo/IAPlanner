package com.planeer.iAPlanner.service.impl;

import com.planeer.iAPlanner.infra.config.GeminiConfig;
import com.planeer.iAPlanner.model.dto.GeminiRequestDTO;
import com.planeer.iAPlanner.model.dto.GeminiResponseDTO2;
import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import com.planeer.iAPlanner.service.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class GeminiServiceImpl implements GeminiService {

    private static final String PROMPT_TEMPLATE =
            "Analise estas informações de agendamento e forneça:\n" +
                    "1. Lembretes importantes para os participantes\n" +
                    "2. Orientação sobre como chegar ao local\n" +
                    "3. Melhor rota considerando pontos de referência\n" +
                    "4. Informações adicionais relevantes\n\n" +
                    "Dados do agendamento:\n" +
                    "- Local: %s\n" +
                    "- Ponto de referência: %s\n" +
                    "- Horario e Data: %s\n" +
                    "- Detalhes: %s";

    private final GeminiConfig config;
    private final ObjectMapper objectMapper;

    public GeminiServiceImpl(GeminiConfig config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateContent(ScheduleDTO scheduleDTO) {
        final Logger logger = LoggerFactory.getLogger(GeminiServiceImpl.class);
        String apiUrl = config.getApiUrl() + config.getModelName() + config.getEndpoint() + config.getApiKey();

        String formattedPrompt = buildPrompt(scheduleDTO);

        logger.info("Iniciando chamada para Gemini API. URL: {}", apiUrl);
        logger.debug("Prompt recebido: {}", formattedPrompt);

        GeminiRequestDTO request = new GeminiRequestDTO(
                new GeminiRequestDTO.Content[]{
                        new GeminiRequestDTO.Content(
                                new GeminiRequestDTO.Content.Part[]{
                                        new GeminiRequestDTO.Content.Part(formattedPrompt)
                                }
                        )
                }
        );

        try {
            String requestBody = objectMapper.writeValueAsString(request);
            logger.debug("Request body: {}", requestBody);

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(apiUrl);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setEntity(new StringEntity(requestBody));

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    String responseBody = EntityUtils.toString(response.getEntity());

                    logger.info("Resposta recebida - Status: {}, Body: {}", statusCode, responseBody);

                    if (statusCode != HttpStatus.SC_OK) {
                        logger.error("Erro na chamada à API Gemini. Status: {}, Response: {}", statusCode, responseBody);
                        throw new RuntimeException("Erro na API Gemini. Status: " + statusCode + ", Response: " + responseBody);
                    }

                    GeminiResponseDTO2 geminiResponse = objectMapper.readValue(responseBody, GeminiResponseDTO2.class);

                    if (geminiResponse.getCandidates() != null &&
                            geminiResponse.getCandidates().length > 0 &&
                            geminiResponse.getCandidates()[0].getContent() != null &&
                            geminiResponse.getCandidates()[0].getContent().getParts() != null &&
                            geminiResponse.getCandidates()[0].getContent().getParts().length > 0) {

                        String result = geminiResponse.getCandidates()[0].getContent().getParts()[0].getText();
                        logger.debug("Resposta processada com sucesso: {}", result);
                        return result;
                    }

                    logger.error("Resposta inesperada da API Gemini. Estrutura de dados inválida. Response: {}", responseBody);
                    throw new RuntimeException("Resposta inesperada da API Gemini. Estrutura de dados inválida.");
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao chamar API Gemini", e);
            throw new RuntimeException("Falha ao comunicar com a API Gemini: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(ScheduleDTO scheduleDTO) {
        return String.format(PROMPT_TEMPLATE,
                scheduleDTO.getLocalAddress(),
                scheduleDTO.getReferencePoint(),
                scheduleDTO.getDateTime(),
                scheduleDTO.getDescription());
    }
}
