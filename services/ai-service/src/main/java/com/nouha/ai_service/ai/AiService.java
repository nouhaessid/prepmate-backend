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

                        Evaluate the candidate's answer objectively, calibrated to
                        their difficulty level (BEGINNER, INTERMEDIATE, or ADVANCED,
                        given below). Judge a beginner's answer against what a
                        competent junior candidate should know, not against
                        expert-level depth — do not deduct points for missing depth
                        that wasn't asked for at that level.

                        Give a score from 0 to 100. A correct, complete answer for the
                        candidate's level should score in the 90-100 range.

                        Evaluate:
                        - technical correctness
                        - completeness relative to the question and the difficulty level
                        - understanding of the concept
                        - clarity

                        Do not give credit for incorrect technical statements.

                        Keep feedback and suggestedAnswer concise — 2 to 4 sentences
                        each. Do not write long-form essays.

                        Feedback should explain what the candidate did well
                        and what they should improve.
                        
                        Identify weaknesses based only on evidence from the candidate's answer.
                        If the question has multiple parts and the candidate does not answer
                        a part, identify that as an unanswered or insufficiently addressed area,
                        rather than assuming the candidate completely lacks the underlying knowledge.

                        The suggested answer should be a concise example of how a
                        candidate at the requested difficulty level could answer.

                        Return the evaluation as structured data matching the requested schema.
                        Always include all five fields: score, feedback, suggestedAnswer,
                        strengths, weaknesses. strengths and weaknesses must always be
                        present as arrays, even if empty.

                        The fields feedback and suggestedAnswer are Markdown text. Write
                        normal, well-formatted Markdown, exactly as you would in a chat
                        message — separate paragraphs with a blank line between them.

                        If you include source code, it MUST be formatted as its own
                        separate block, not inline in a sentence:
                        - Put a blank line before the code, then the opening fence
````java on its own line by itself, then each line of code
                          on its own line, then the closing ``` on its own line, then
                          a blank line after it.
                        - Never place the opening ```java fence in the middle of a
                          sentence, and never put multiple statements on one line
                          separated by spaces instead of line breaks.
                        - Use single backticks only for a short inline reference like
                          `String`, never for multi-line or multi-statement code.

                        Example of correctly formatted output containing code:

                        Reference equality checks whether two variables point to the
                        same object, while `.equals()` checks the actual content.

```java
                        String a = new String("hello");
                        String b = new String("hello");
                        System.out.println(a == b);      // false
                        System.out.println(a.equals(b)); // true
```

                        This example shows two separately created objects with equal
                        content but different identities.
                        """)
                .user("""
                        Interview question:
                        %s

                        Interview difficulty: %s

                        Candidate answer:
                        %s
                        """.formatted(
                        request.question(),
                        request.difficulty(),
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

                    Generate exactly one interview question. Keep it short and
                    focused — 1 to 3 sentences. Do not write long, multi-paragraph
                    or heavily-scenario-laden questions.

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
                          when appropriate, but keep them brief.
                        - Test whether the candidate understands how and why
                          something works, not only whether they can recall a
                          definition.
                        - The question should be answerable by a candidate at
                          the requested difficulty level.

                    Use Markdown formatting in the question.

                    If the question includes source code, it MUST be formatted as
                    its own separate block, not inline in a sentence:
                    - Put a blank line before the code, then the opening fence
```java on its own line by itself, then each line of code
                      on its own line, then the closing ``` on its own line, then
                      a blank line after it.
                    - Never place the opening ```java fence in the middle of a
                      sentence, and never put multiple statements on one line
                      separated by spaces instead of line breaks.
                    - Use single backticks only for a short inline reference like
                      `String`, never for multi-line or multi-statement code.

                    When referring to a short piece of code inside a sentence,
                    use Markdown inline code formatting with single backticks.

                    Return the question as structured data in the `question` field.
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