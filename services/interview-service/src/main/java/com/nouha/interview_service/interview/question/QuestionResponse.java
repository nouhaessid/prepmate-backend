package com.nouha.interview_service.interview.question;

import com.nouha.interview_service.interview.answer.Answer;
import com.nouha.interview_service.interview.answer.AnswerResponse;

public record QuestionResponse(
        Integer id,
        Integer sessionId,
        String content,
        Integer orderNumber,
        AnswerResponse answer
) {
}
