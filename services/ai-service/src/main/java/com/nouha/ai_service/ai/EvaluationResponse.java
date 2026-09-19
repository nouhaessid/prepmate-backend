package com.nouha.ai_service.ai;

import java.util.List;

public record EvaluationResponse(
        Double score,
        String feedback,
        String suggestedAnswer,
        List<String> strengths,
        List<String> weaknesses
) {
}