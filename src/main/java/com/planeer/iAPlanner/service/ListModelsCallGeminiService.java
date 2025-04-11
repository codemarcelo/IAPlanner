package com.planeer.iAPlanner.service;

import com.planeer.iAPlanner.model.dto.GeminiResponseDTO;
import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ListModelsCallGeminiService {

    ResponseEntity<String> verifyModelAvailability();
}
