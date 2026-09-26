package com.nouha.interview_service.interview;

import com.nouha.interview_service.ai.*;
import com.nouha.interview_service.exception.InterviewNotFoundException;
import com.nouha.interview_service.exception.InterviewNotInProgressException;
import com.nouha.interview_service.exception.QuestionAlreadyAnsweredException;
import com.nouha.interview_service.exception.QuestionNotFoundException;
import com.nouha.interview_service.interview.answer.Answer;
import com.nouha.interview_service.interview.answer.AnswerMapper;
import com.nouha.interview_service.interview.answer.AnswerRepository;
import com.nouha.interview_service.interview.question.Question;
import com.nouha.interview_service.interview.question.QuestionRepository;
import com.nouha.interview_service.interview.question.QuestionResponse;
import com.nouha.interview_service.interview.session.*;
import com.nouha.interview_service.interview.session.dto.InterviewSessionRequest;
import com.nouha.interview_service.interview.session.dto.InterviewSessionResponse;
import com.nouha.interview_service.interview.session.dto.SubmitRequest;
import com.nouha.interview_service.interview.session.dto.SubmitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewSessionRepository interviewSessionRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final InterviewSessionMapper interviewSessionMapper;
    private final AnswerMapper answerMapper;

    private final AiClient aiClient;


    @Transactional
    public InterviewSessionResponse createInterview(
            InterviewSessionRequest request,
            Authentication authentication
    ) {

        // 1. Get authenticated user's Keycloak ID
        String keycloakUserId = authentication.getName();

        // 2. Create the interview session
        InterviewSession session = interviewSessionMapper.toInterviewSession(request);

        session.setKeycloakUserId(keycloakUserId);
        session.setStatus(SessionStatus.IN_PROGRESS);
        session.setStartedAt(LocalDateTime.now());

        // 3. Save the session first
        session = interviewSessionRepository.save(session);

        // 4. Generate the first question
        QuestionGenerationRequest aiRequest = new QuestionGenerationRequest(
                        session.getTopic().name(),
                        session.getDifficulty().name(),
                        new ArrayList<>()
                );

        QuestionGenerationResponse aiResponse = aiClient.generateQuestion(aiRequest);

        // 5. Create Question 1
        Question question = Question.builder()
                .session(session)
                .content(aiResponse.question())
                .orderNumber(1)
                .build();

        // 6. Save the question
        questionRepository.save(question);

        // 7. Add question to session's collection
        session.getQuestions().add(question);

        // 8. Return the interview
        return interviewSessionMapper.toInterviewSessionResponse(session);
    }


    @Transactional
    @PreAuthorize("@interviewSecurity.isOwner(#interviewId)")
    public SubmitResponse submitAnswer(
            Integer interviewId,
            Integer questionId,
            SubmitRequest request
    ) {

        // 1. Find the interview
        InterviewSession session = interviewSessionRepository
                .findById(interviewId)
                .orElseThrow(() ->
                        new InterviewNotFoundException( String.format("Cannot submit answer:: No interview found with the provided ID:: %d", interviewId))
                );

        // 2. Make sure the interview is still in progress
        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new InterviewNotInProgressException("Interview is not in progress");
        }

        // 3. Find the question belonging to this interview
        Question question = questionRepository
                .findByIdAndSessionId(questionId, interviewId)
                .orElseThrow(() ->
                        new QuestionNotFoundException(String.format("Cannot submit answer:: No question found with the provided ID:: %d", questionId))
                );

        // 4. Prevent answering the same question twice
        if (question.getAnswer() != null) {
            throw new QuestionAlreadyAnsweredException(
                    "Question has already been answered"
            );
        }

        // 5. Create the answer
        Answer answer = Answer.builder()
                .question(question)
                .content(request.content())
                .build();

        // 6. Ask AI to evaluate the answer
        EvaluationRequest evaluationRequest = new EvaluationRequest(
                        question.getContent(),
                        request.content(),
                        session.getDifficulty()
                );

        EvaluationResponse evaluation = aiClient.evaluate(evaluationRequest);


        // 7. Store AI evaluation inside the Answer
        answer.setScore(evaluation.score());
        answer.setFeedback(evaluation.feedback());
        answer.setSuggestedAnswer(evaluation.suggestedAnswer());
        answer.setStrengths(evaluation.strengths());
        answer.setWeaknesses(evaluation.weaknesses());

        // 8. Connect answer to question
        question.setAnswer(answer);

        // 9. Save the answer
        answerRepository.save(answer);

        // 10. Save the question
        questionRepository.save(question);


        // 11. Check whether this was the last question
        boolean interviewCompleted = question.getOrderNumber() >= session.getQuestionCount();

        QuestionResponse nextQuestion = null;

        // 12. If this was last question , finish the interview
        if (interviewCompleted) {

            Integer finalScore = calculateFinalScore(session);

            session.setFinalScore(finalScore);
            session.setStatus(SessionStatus.COMPLETED);
            session.setCompletedAt(LocalDateTime.now());

        } else {

            // 13. Collect all previous questions
            List<String> previousQuestions = session.getQuestions()
                            .stream()
                            .map(Question::getContent)
                            .toList();

/*
            // Collect all strengths from previous answers
            List<String> strengths = session.getQuestions()
                            .stream()
                            .filter(q -> q.getAnswer() != null)
                            .flatMap(q -> q.getAnswer().getStrengths().stream())
                            .toList();


            // Collect all weaknesses from previous answers
            List<String> weaknesses = session.getQuestions()
                            .stream()
                            .filter(q -> q.getAnswer() != null)
                            .flatMap(q -> q.getAnswer().getWeaknesses().stream())
                            .toList();
*/

            // 14. Ask AI to generate the next question
            QuestionGenerationRequest aiRequest =  new QuestionGenerationRequest(
                            session.getTopic().name(),
                            session.getDifficulty().name(),
            //                strengths,
            //                weaknesses,
                            previousQuestions
                    );

            QuestionGenerationResponse aiResponse = aiClient.generateQuestion(aiRequest);

            // 15. Create the next question
            Question next = Question.builder()
                    .session(session)
                    .content(aiResponse.question())
                    .orderNumber(question.getOrderNumber() + 1)
                    .build();

            // 16. Save the next question
            questionRepository.save(next);

            // 17. Add it to the session
            session.getQuestions().add(next);

            // 18. Convert it to QuestionResponse
            nextQuestion = new QuestionResponse(
                    next.getId(),
                    session.getId(),
                    next.getContent(),
                    next.getOrderNumber(),
                    null
            );
        }


        // 19. Return the submitted answer + next question + completion status
        return new SubmitResponse(
                answerMapper.toAnswerResponse(answer),
                nextQuestion,
                interviewCompleted
        );
    }


    private Integer calculateFinalScore(InterviewSession session) {

        return (int) session.getQuestions()
                .stream()
                .filter(question -> question.getAnswer() != null)
                .mapToInt(question -> question.getAnswer().getScore())
                .average()
                .orElse(0);
    }

    public List<InterviewSessionResponse> getMyInterviews(Authentication authentication) {
        String keycloakUserId = authentication.getName();
        return interviewSessionRepository.findAllByKeycloakUserId(keycloakUserId)
                .stream()
                .map(interviewSessionMapper::toInterviewSessionResponse)
                .toList();
    }

    @PreAuthorize("@interviewSecurity.isOwner(#interviewId)")
    public InterviewSessionResponse getMyInterviewById(Integer interviewId) {

        InterviewSession session = interviewSessionRepository
                .findById(interviewId)
                .orElseThrow(() ->
                        new InterviewNotFoundException(String.format("No interview was found with ID:: %d", interviewId))
                );

        return interviewSessionMapper.toInterviewSessionResponse(session);
    }

    @PreAuthorize("@interviewSecurity.isOwner(#interviewId)")
    public void deleteMyInterviewById(Integer interviewId) {
        var session = interviewSessionRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(String.format("No Interview was found with ID:: %d",interviewId)));

        interviewSessionRepository.delete(session);
    }
}