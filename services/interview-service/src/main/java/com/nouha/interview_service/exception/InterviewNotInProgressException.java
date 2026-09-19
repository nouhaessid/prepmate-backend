package com.nouha.interview_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InterviewNotInProgressException extends RuntimeException{
    public InterviewNotInProgressException(String message){
        super(message);
    }
}
