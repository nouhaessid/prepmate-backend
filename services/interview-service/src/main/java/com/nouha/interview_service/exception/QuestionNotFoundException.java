package com.nouha.interview_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException(String message){
        super(message);
    }
}