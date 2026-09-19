package com.nouha.interview_service.interview.answer;

import org.springframework.stereotype.Service;

@Service
public class AnswerMapper {

    public AnswerResponse toAnswerResponse(Answer answer) {
        return new AnswerResponse(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getContent(),
                answer.getScore(),
                answer.getFeedback(),
                answer.getSuggestedAnswer(),
                answer.getStrengths(),
                answer.getWeaknesses()
        );
    }
}
