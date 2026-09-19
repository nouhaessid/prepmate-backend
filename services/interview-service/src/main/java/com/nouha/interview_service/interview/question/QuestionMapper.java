package com.nouha.interview_service.interview.question;

import com.nouha.interview_service.interview.answer.Answer;
import com.nouha.interview_service.interview.answer.AnswerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionMapper {

    private final AnswerMapper answerMapper;

    public QuestionResponse toQuestionResponse(Question question) {
        Answer answer = question.getAnswer();

        return new QuestionResponse(
                question.getId(),
                question.getSession().getId(),
                question.getContent(),
                question.getOrderNumber(),
                answer != null ? answerMapper.toAnswerResponse(answer) : null
        );
    }
}
