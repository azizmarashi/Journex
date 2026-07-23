package org.example.journex.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.ChecklistQuestionType;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "checklist_execution_item_tb" , schema = "journex_db")
public class ChecklistExecutionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_index")
    private Long orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_id")
    private ChecklistExecution execution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private ChecklistQuestion question;

    @Column(name = "question_text")
    private String questionSnapshot;

    @Enumerated(EnumType.STRING)
    private ChecklistQuestionType questionTypeSnapshot;

    @Column(name = "answer_value")
    private String answerValue;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    @Column(name = "required")
    private Boolean required;

    @Column(name = "tag")
    private String tag;
}
