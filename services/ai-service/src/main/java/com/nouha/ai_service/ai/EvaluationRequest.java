package com.nouha.ai_service.ai;

import jakarta.validation.constraints.NotBlank;

public record EvaluationRequest(
        @NotBlank String question,
        @NotBlank String answer
) {
}