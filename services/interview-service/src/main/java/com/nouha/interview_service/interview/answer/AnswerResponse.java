package com.nouha.interview_service.interview.answer;

import java.util.List;

public record AnswerResponse(
        Integer id,
        Integer questionId,
        String content,
        Integer score,
        String feedback,
        String suggestedAnswer,
        List<String> strengths,
        List<String> weaknesses
) {
}