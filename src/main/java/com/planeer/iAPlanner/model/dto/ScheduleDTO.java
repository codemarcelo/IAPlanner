package com.planeer.iAPlanner.model.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    private String title;
    private String description;
    private String dateTime; // Formato ISO 8601 (exemplo: 2023-10-01T10:00:00)
}