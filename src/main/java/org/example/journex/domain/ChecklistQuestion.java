package org.example.journex.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.ChecklistCategory;
import org.example.journex.enums.ChecklistQuestionType;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "checklist_question_tb" , schema = "journex_db")
public class ChecklistQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "checklist_question")
    private String checklistQuestion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChecklistQuestionType questionType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChecklistCategory checklistCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;

    @NotNull
    @Column(name = "required")
    private Boolean required;

    @NotNull
    @Column(name = "order_index")
    private Long orderIndex;
}