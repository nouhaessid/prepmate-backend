package com.nouha.interview_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InterviewNotFoundException extends RuntimeException {
    public InterviewNotFoundException(String message){
        super(message);
    }
}
