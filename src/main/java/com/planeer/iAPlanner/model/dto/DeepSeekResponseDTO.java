package com.planeer.iAPlanner.model.dto;

import lombok.Data;

@Data
public class DeepSeekResponseDTO {
    private String reminder;
    private String address;
    private String bestRoute;
}