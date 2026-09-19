package com.nouha.interview_service.interview.session.dto;

import com.nouha.interview_service.interview.answer.AnswerResponse;
import com.nouha.interview_service.interview.question.QuestionResponse;

public record SubmitResponse(
        AnswerResponse evaluation,
        QuestionResponse nextQuestion,
        boolean interviewCompleted
) {}