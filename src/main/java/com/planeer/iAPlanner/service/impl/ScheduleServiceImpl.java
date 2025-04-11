package com.planeer.iAPlanner.service.impl;

import com.planeer.iAPlanner.model.dto.GeminiResponseDTO;
import com.planeer.iAPlanner.model.dto.ParticipantsDTO;
import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import com.planeer.iAPlanner.model.persistence.domains.ParticipantsEntity;
import com.planeer.iAPlanner.model.persistence.domains.ScheduleEntity;
import com.planeer.iAPlanner.model.persistence.repositories.ParticipantsRepository;
import com.planeer.iAPlanner.model.persistence.repositories.ScheduleRepository;
import com.planeer.iAPlanner.service.CallGeminiService;
import com.planeer.iAPlanner.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

    @Autowired
    CallGeminiService callGeminiService;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Void saveSchedule(ScheduleDTO scheduleDTO) {

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setTitle(scheduleDTO.getTitle());
        scheduleEntity.setDescription(scheduleDTO.getDescription());
        scheduleEntity.setDateTime(LocalDateTime.parse(scheduleDTO.getDateTime()));
        scheduleEntity.setLocalAddress(scheduleDTO.getLocalAddress());
        scheduleEntity.setReferencePoint(scheduleDTO.getReferencePoint());

        scheduleEntity = scheduleRepository.save(scheduleEntity);

        final ScheduleEntity finalScheduleEntity = scheduleEntity;

        List<ParticipantsEntity> participantsEntities = scheduleDTO.getParticipants().stream().map(participantDTO -> {
            ParticipantsEntity participantsEntity = new ParticipantsEntity();
            participantsEntity.setName(participantDTO.getName());
            participantsEntity.setPhone(participantDTO.getPhone());
            participantsEntity.setEmail(participantDTO.getEmail());
            participantsEntity.setSchedule(finalScheduleEntity); // Associa o agendamento ao participante
            return participantsEntity;
        }).collect(Collectors.toList());


        GeminiResponseDTO geminiResponseDTO = callGeminiService.callGeminiApi(scheduleDTO);

        scheduleEntity.setAddInfoSchedule(geminiResponseDTO.getAdditionalInfo());

        participantsRepository.saveAll(participantsEntities);

        scheduleEntity.setParticipants(participantsEntities);

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
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setTitle(entity.getTitle());
            scheduleDTO.setDescription(entity.getDescription());
            scheduleDTO.setDateTime(String.valueOf(entity.getDateTime()));
            scheduleDTO.setLocalAddress(entity.getLocalAddress());
            scheduleDTO.setReferencePoint(entity.getReferencePoint());
            scheduleDTO.setAddInfoSchedule(entity.getAddInfoSchedule());

            if (entity.getParticipants() != null) {
                scheduleDTO.setParticipants(entity.getParticipants().stream().map(participant -> {
                    ParticipantsDTO participantDTO = new ParticipantsDTO();
                    participantDTO.setName(participant.getName());
                    participantDTO.setPhone(participant.getPhone());
                    participantDTO.setEmail(participant.getEmail());
                    return participantDTO;
                }).collect(Collectors.toList()));
            }

            return scheduleDTO;
        }).collect(Collectors.toList());
    }
}