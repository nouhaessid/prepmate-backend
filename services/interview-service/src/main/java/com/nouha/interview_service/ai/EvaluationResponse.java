package com.nouha.interview_service.ai;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public record EvaluationResponse(
        Integer score,
        String feedback,
        String suggestedAnswer,
        List<String> strengths,
        List<String>weaknesses
) {
}