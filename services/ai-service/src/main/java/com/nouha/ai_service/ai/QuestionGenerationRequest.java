package com.nouha.ai_service.ai;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record QuestionGenerationRequest(
        @NotBlank String topic,
        @NotBlank String difficulty,
        //List<String> strengths,
        //List<String> weaknesses,
        List<String> previousQuestions
) {}