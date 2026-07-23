package org.example.journex.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.ChecklistStatus;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "checklist_execution_tb" , schema = "journex_db")
public class ChecklistExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trade_id")
    private Long tradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChecklistStatus status;

    @OneToMany(
            mappedBy="execution",
            cascade=CascadeType.ALL,
            orphanRemoval=true
    )
    private List<ChecklistExecutionItem> items;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "completion_rate")
    private Double completionRate;

    @Column(name = "score")
    private Long score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="checklist_id")
    private Checklist checklist;

}