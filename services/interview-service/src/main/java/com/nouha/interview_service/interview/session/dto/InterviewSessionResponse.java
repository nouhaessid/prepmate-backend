package com.nouha.interview_service.interview.session.dto;

import com.nouha.interview_service.interview.question.QuestionResponse;
import com.nouha.interview_service.interview.session.Difficulty;
import com.nouha.interview_service.interview.session.InterviewTopic;
import com.nouha.interview_service.interview.session.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

public record InterviewSessionResponse(
        Integer id,
        InterviewTopic topic,
        Difficulty difficulty,
        SessionStatus status,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        Double finalScore,
        List<QuestionResponse> questions
) {}