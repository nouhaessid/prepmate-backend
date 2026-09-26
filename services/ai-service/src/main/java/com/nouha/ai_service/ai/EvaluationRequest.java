package com.nouha.ai_service.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EvaluationRequest(
        @NotBlank String question,
        @NotBlank String answer,
        @NotNull Difficulty difficulty
) {
}