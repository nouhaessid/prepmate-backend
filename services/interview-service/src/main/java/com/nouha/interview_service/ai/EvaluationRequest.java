package com.nouha.interview_service.ai;

import com.nouha.interview_service.interview.session.Difficulty;

public record EvaluationRequest(
        String question,
        String answer,
        Difficulty difficulty
) {
}