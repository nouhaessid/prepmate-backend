package com.nouha.interview_service.ai;

import java.util.List;

public record QuestionGenerationRequest(
        String topic,
        String difficulty,
//        List<String> strengths,
//        List<String> weaknesses,
        List<String> previousQuestions
) {
}