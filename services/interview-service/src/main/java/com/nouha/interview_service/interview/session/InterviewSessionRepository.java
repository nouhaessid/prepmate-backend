package com.nouha.interview_service.interview.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Integer> {
    List<InterviewSession> findAllByKeycloakUserId(String keycloakUserId);
}
