package com.nouha.interview_service.interview.session;

import com.nouha.interview_service.interview.question.QuestionMapper;
import com.nouha.interview_service.interview.session.dto.InterviewSessionRequest;
import com.nouha.interview_service.interview.session.dto.InterviewSessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewSessionMapper {
    private final QuestionMapper questionMapper;

    public InterviewSession toInterviewSession(InterviewSessionRequest request) {
        return InterviewSession.builder()
                .topic(request.topic())
                .difficulty(request.difficulty())
                .questionCount(request.questionCount())
                .build();
    }

    public InterviewSessionResponse toInterviewSessionResponse(InterviewSession session) {
        return new InterviewSessionResponse(
                session.getId(),
                session.getTopic(),
                session.getDifficulty(),
                session.getQuestionCount(),
                session.getStatus(),
                session.getStartedAt(),
                session.getCompletedAt(),
                session.getFinalScore(),
                session.getQuestions()
                        .stream()
                        .map(questionMapper::toQuestionResponse)
                        .toList()
        );
    }
}
