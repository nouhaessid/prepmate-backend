package com.nouha.interview_service.interview.session.dto;

import com.nouha.interview_service.interview.session.Difficulty;
import com.nouha.interview_service.interview.session.InterviewTopic;
import jakarta.validation.constraints.NotNull;

public record InterviewSessionRequest(
        @NotNull InterviewTopic topic,
        @NotNull Difficulty difficulty
) {
}