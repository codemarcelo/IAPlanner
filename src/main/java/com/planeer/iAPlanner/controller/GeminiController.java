package com.planeer.iAPlanner.controller;

import com.planeer.iAPlanner.service.impl.GeminiServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeminiController {

    private final GeminiServiceImpl geminiServiceImpl;

    public GeminiController(GeminiServiceImpl geminiServiceImpl) {
        this.geminiServiceImpl = geminiServiceImpl;
    }

    @PostMapping("/api/gemini/generate")
    public String generateContent(@RequestBody String prompt) {
        try {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao comunicar com a API Gemini", e);
        }
    }
}
