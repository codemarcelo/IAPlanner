package com.planeer.iAPlanner.service;

import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import org.springframework.stereotype.Service;

@Service
public interface GeminiService {

    public String generateContent(ScheduleDTO scheduleDTO);

}
