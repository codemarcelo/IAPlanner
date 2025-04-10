package com.planeer.iAPlanner.service;

import com.planeer.iAPlanner.model.dto.DeepSeekResponseDTO;
import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import org.springframework.stereotype.Service;

@Service
public interface DeepSeekService {

    DeepSeekResponseDTO callDeepSeekApi( ScheduleDTO scheduleDTO);
}
