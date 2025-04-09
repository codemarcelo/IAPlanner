package com.planeer.iAPlanner.service;

import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import com.planeer.iAPlanner.model.persistence.domains.ScheduleEntity;

import java.util.List;

public interface ScheduleService {

    Void saveSchedule(ScheduleDTO scheduleDTO);

    ScheduleDTO getSchedule(Integer scheduleID);

    List<ScheduleDTO> getAllSchedules();
}
