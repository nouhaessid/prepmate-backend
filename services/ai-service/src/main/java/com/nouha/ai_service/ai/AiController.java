package com.nouha.ai_service.ai;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResponse> evaluate(
            @RequestBody @Valid EvaluationRequest request
    ) {
        return ResponseEntity.ok(
                aiService.evaluateAnswer(request)
        );
    }

    @PostMapping("/questions")
    public ResponseEntity<QuestionGenerationResponse> generateQuestion(
            @RequestBody @Valid QuestionGenerationRequest request
    ) {
        return ResponseEntity.ok(
                aiService.generateQuestion(request)
        );
    }
}