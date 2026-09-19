package com.nouha.interview_service.handler;

import com.nouha.interview_service.exception.InterviewNotFoundException;
import com.nouha.interview_service.exception.InterviewNotInProgressException;
import com.nouha.interview_service.exception.QuestionAlreadyAnsweredException;
import com.nouha.interview_service.exception.QuestionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InterviewNotFoundException.class)
    public ResponseEntity<String> handle(InterviewNotFoundException exp){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exp.getMessage());
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<String> handle(QuestionNotFoundException exp){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exp.getMessage());
    }

    @ExceptionHandler(InterviewNotInProgressException.class)
    public ResponseEntity<String> handle(InterviewNotInProgressException exp){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMessage());
    }

    @ExceptionHandler(QuestionAlreadyAnsweredException.class)
    public ResponseEntity<String> handle(QuestionAlreadyAnsweredException exp){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exp){
        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError)error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}
