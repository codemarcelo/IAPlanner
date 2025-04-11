package com.planeer.iAPlanner.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequestDTO {
    private Content[] contents;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private Part[] parts;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Part {
            private String text;
        }
    }
}
