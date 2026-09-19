package com.nouha.interview_service.ai;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ai-service",
        url = "${application.config.ai-url}"
)
public interface AiClient {

    @PostMapping("/evaluate")
    public EvaluationResponse evaluate(
            @RequestBody EvaluationRequest request
    );
    @PostMapping("/questions")
    public QuestionGenerationResponse generateQuestion(
            @Valid @RequestBody QuestionGenerationRequest request
    );
}
