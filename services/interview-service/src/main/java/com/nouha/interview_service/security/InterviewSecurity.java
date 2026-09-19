package com.nouha.interview_service.security;


import com.nouha.interview_service.interview.session.InterviewSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("interviewSecurity")
@RequiredArgsConstructor
public class InterviewSecurity {

    private final InterviewSessionRepository repository;

    public boolean isOwner(Integer interviewId) {

        var authentication =  SecurityContextHolder.getContext().getAuthentication();

        String currentUserId = authentication.getName();

        return repository.findById(interviewId)
                .map(interview ->
                        interview.getKeycloakUserId().equals(currentUserId)
                )
                .orElse(false);
    }
}
