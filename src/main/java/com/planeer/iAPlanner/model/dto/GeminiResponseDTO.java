package com.planeer.iAPlanner.model.dto;

import lombok.Data;

@Data
public class GeminiResponseDTO {
    private String reminder;
    private String address;
    private String bestRoute;
    private String additionalInfo;
}