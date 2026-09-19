package com.nouha.interview_service.interview.answer;

import com.nouha.interview_service.interview.question.Question;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    private Question question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private Double score;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(columnDefinition = "TEXT")
    private String suggestedAnswer;

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> strengths;

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> weaknesses;
}