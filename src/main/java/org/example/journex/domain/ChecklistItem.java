package org.example.journex.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.ChecklistItemType;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "checklist_item_tb" , schema = "journex_db")
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChecklistItemType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;

    @NotNull
    @Column(name = "required")
    private Boolean required;

    @NotNull
    @Column(name = "order_index")
    private Long orderIndex;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_question_id")
    private ChecklistItem sourceQuestion;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
}