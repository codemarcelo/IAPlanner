package com.planeer.iAPlanner.service.impl;

import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import com.planeer.iAPlanner.model.persistence.domains.ScheduleEntity;
import com.planeer.iAPlanner.model.persistence.repositories.ScheduleRepository;
import com.planeer.iAPlanner.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Void saveSchedule(ScheduleDTO scheduleDTO) {
        // Converte o DTO para a entidade
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setTitle(scheduleDTO.getTitle());
        scheduleEntity.setDescription(scheduleDTO.getDescription());
        scheduleEntity.setDateTime(LocalDateTime.parse(scheduleDTO.getDateTime())); // Converte para LocalDateTime

        // Salva no banco de dados
        scheduleRepository.save(scheduleEntity);
        return null;
    }

    @Override
    public ScheduleDTO getSchedule(Integer scheduleId) {

        Optional<ScheduleEntity> scheduleEntityOpt = scheduleRepository.findById(Long.valueOf(scheduleId));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        if(scheduleEntityOpt.isPresent()) {
            scheduleEntity = scheduleEntityOpt.get();
        }

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDescription(scheduleEntity.getDescription());
        scheduleDTO.setDateTime(String.valueOf(scheduleEntity.getDateTime()));
        scheduleDTO.setTitle(scheduleEntity.getTitle());
        return scheduleDTO;
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findAll();
        return scheduleEntities.stream().map(entity -> {
            ScheduleDTO dto = new ScheduleDTO();
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setDateTime(String.valueOf(entity.getDateTime()));
            return dto;
        }).toList();
    }
}