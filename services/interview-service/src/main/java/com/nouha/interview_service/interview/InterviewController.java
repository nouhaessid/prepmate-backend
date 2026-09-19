package com.nouha.interview_service.interview;

import com.nouha.interview_service.interview.session.dto.InterviewSessionRequest;
import com.nouha.interview_service.interview.session.dto.InterviewSessionResponse;
import com.nouha.interview_service.interview.session.dto.SubmitRequest;
import com.nouha.interview_service.interview.session.dto.SubmitResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interviews")
public class InterviewController {

    private final InterviewService service;

    @PostMapping
    public ResponseEntity<InterviewSessionResponse> createInterview(
            @RequestBody @Valid InterviewSessionRequest request,
            Authentication authentication
            ) {
        return ResponseEntity.ok(service.createInterview(request, authentication));
    }

    @PostMapping("/{interviewId}/questions/{questionId}/answer")
    public ResponseEntity<SubmitResponse> submit(
            @PathVariable Integer interviewId,
            @PathVariable Integer questionId,
            @Valid @RequestBody SubmitRequest request
    ) {
        return ResponseEntity.ok(service.submitAnswer(interviewId, questionId, request));
    }

    @GetMapping
    public ResponseEntity<List<InterviewSessionResponse>> getMyInterviews(
            Authentication authentication
    ){
        return ResponseEntity.ok(service.getMyInterviews(authentication));
    }

    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewSessionResponse> getMyInterviewById(
            @PathVariable Integer interviewId
    ){
        return ResponseEntity.ok(service.getMyInterviewById(interviewId));
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<Void> deleteMyInterviewById(
            @PathVariable Integer interviewId
    ){
        service.deleteMyInterviewById(interviewId);
        return ResponseEntity.noContent().build();
    }
}


