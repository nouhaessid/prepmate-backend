package com.nouha.interview_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QuestionAlreadyAnsweredException extends RuntimeException{
    public QuestionAlreadyAnsweredException(String message){
        super(message);
    }
}
