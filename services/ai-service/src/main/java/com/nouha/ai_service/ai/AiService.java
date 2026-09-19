package com.nouha.ai_service.ai;

import com.nouha.ai_service.config.GroqEvaluationOutputConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;

    public EvaluationResponse evaluateAnswer(EvaluationRequest request) {

        return chatClient
                .prompt()
                .system("""
                        You are an expert technical interviewer.

                        Evaluate the candidate's answer objectively.

                        Give a score from 0 to 10.

                        Evaluate:
                        - technical correctness
                        - completeness
                        - understanding of the concept
                        - clarity

                        Do not give credit for incorrect technical statements.

                        Feedback should explain what the candidate did well
                        and what they should improve.
                        
                        Identify weaknesses based only on evidence from the candidate's answer.
                        If the question has multiple parts and the candidate does not answer
                        a part, identify that as an unanswered or insufficiently addressed area,
                        rather than assuming the candidate completely lacks the underlying knowledge.

                        The suggested answer should be a strong example of
                        how an experienced candidate could answer the question.

                        Return the evaluation as structured data matching the requested schema.

                        The fields feedback and suggestedAnswer are strings.
                        Markdown is allowed inside these strings.

                        If feedback or suggestedAnswer contains multiple lines,
                        represent line breaks using escaped JSON newline characters (\\n).
                        Do not include literal unescaped line breaks inside JSON string values.

                        If you include source code inside feedback or suggestedAnswer:
                        - Use Markdown fenced code blocks.
                        - Always specify the programming language after the opening backticks.
                        - For example, Java code must use ```java.
                        - Keep the entire code block valid JSON string content.

                        When referring to a short piece of code inside a sentence,
                        use Markdown inline code formatting with single backticks.
                        """)
                .user("""
                        Interview question:
                        %s

                        Candidate answer:
                        %s
                        """.formatted(
                        request.question(),
                        request.answer()
                ))
                .call()
                .entity(
                        new GroqEvaluationOutputConverter(),
                        spec -> spec.useProviderStructuredOutput()
                );

    }

    public QuestionGenerationResponse generateQuestion(
            QuestionGenerationRequest request
    ) {

        return chatClient
                .prompt()
                .system("""
                    You are an expert technical interviewer.

                    Generate exactly one interview question.

                    The question must:
                    - match the requested interview topic
                    - match the requested difficulty
                    - test understanding rather than simple memorization
                    - be appropriate for a software engineering interview
                    - not repeat or closely duplicate any previous question
                    
                    Modern technology practices:
                    - Prefer current, modern, and recommended practices for the
                      requested technology.
                    - Avoid deprecated or legacy APIs, patterns, and architectures
                      when a modern recommended alternative exists.
                    - Do not use legacy approaches simply because they are historically
                      common.
                    - Only ask about legacy approaches when the question explicitly
                      targets legacy technology or backward compatibility
                    
                    Interview question selection rules:

                    1. Topic and difficulty
                        - Stay strictly within the requested topic.
                        - Respect the requested difficulty level.
                        - Do not make the question significantly easier or harder
                          than the requested difficulty.

                    2. Broad topic coverage
                        - Broad coverage of the requested topic is the primary goal.
                        - Explore different important concepts and subtopics
                          within the topic throughout the interview.
                        - Do not focus repeatedly on one narrow concept.
                        - Prefer a relevant concept that has not yet been tested.

                    3. Previous questions
                        - Never generate a question that is the same as,
                          or essentially equivalent to, a previous question.
                        - Do not simply rephrase a previous question.
                        - Avoid testing the same underlying concept repeatedly.
                        - Use the previous questions to determine which concepts
                          have already been covered.

                    4. Interview progression
                        - Each question should assess a different aspect of
                          the requested topic when possible.
                        - Avoid several consecutive questions about the same
                          concept or closely related subtopic.
                        - When multiple suitable questions are possible,
                          prefer the one that increases coverage of the topic.

                    5. Question quality
                        - Prefer practical, conceptual, or scenario-based questions
                          when appropriate.
                        - Test whether the candidate understands how and why
                          something works, not only whether they can recall a
                          definition.
                        - The question should be answerable by a candidate at
                          the requested difficulty level.

                    Use Markdown formatting in the question.

                    If the question includes source code:
                    - Use Markdown fenced code blocks.
                    - Always specify the programming language after the
                      opening backticks.
                    - For example, Java code must use ```java.
                    - Do not present multi-line source code as ordinary text.

                    When referring to a short piece of code inside a sentence,
                    use Markdown inline code formatting with single backticks.

                    Return only the question content.
                    """)
                .user("""
                    Interview topic: %s
                    Interview difficulty: %s
                    Previous questions: %s
                    """.formatted(
                        request.topic(),
                        request.difficulty(),
                        request.previousQuestions()
                ))
                .call()
                .entity(
                        QuestionGenerationResponse.class,
                        spec -> spec.useProviderStructuredOutput()
                );
    }
}