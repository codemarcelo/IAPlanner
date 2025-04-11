package com.planeer.iAPlanner.model.dto;

import lombok.Data;

@Data
public class GeminiResponseDTO2 {
    private Candidate[] candidates;

    @Data
    public static class Candidate {
        private Content content;
        private String finishReason;

        @Data
        public static class Content {
            private Part[] parts;

            @Data
            public static class Part {
                private String text;
            }
        }
    }
}