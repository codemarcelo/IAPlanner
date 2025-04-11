package com.planeer.iAPlanner.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleDTO {

    private String title;
    private String description;
    private String dateTime;
    private String localAddress;
    private String referencePoint;
    private String addInfoSchedule;
    private List<ParticipantsDTO> participants;

}