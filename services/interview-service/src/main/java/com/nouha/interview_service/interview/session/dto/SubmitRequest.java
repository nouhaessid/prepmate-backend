package com.nouha.interview_service.interview.session.dto;

import jakarta.validation.constraints.NotBlank;

public record SubmitRequest(
        @NotBlank String content
) {}