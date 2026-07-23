package org.example.journex.dao;

import org.example.journex.domain.ChecklistQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChecklistQuestionRepository extends JpaRepository<ChecklistQuestion, Long> {

    @Query("""
        SELECT q
        FROM ChecklistQuestion q
        WHERE q.checklist.id = :checklistId
        ORDER BY q.orderIndex
        """)
    List<ChecklistQuestion> findByChecklistIdOrderByOrderIndex(Long checklistId);

    @Query("""
        SELECT q
        FROM ChecklistQuestion q
        WHERE q.checklist.id = :checklistId
        AND q.orderIndex >= :orderIndex
        ORDER BY q.orderIndex
        """)
    List<ChecklistQuestion> findByChecklistIdAndOrderIndexGreaterThanEqualOrderByOrderIndex(
            Long checklistId,
            Long orderIndex
    );

    @Query("""
        SELECT q
        FROM ChecklistQuestion q
        WHERE q.checklist.id = :checklistId
        AND q.orderIndex > :orderIndex
        ORDER BY q.orderIndex
        """)
    List<ChecklistQuestion> findByChecklistIdAndOrderIndexGreaterThanOrderByOrderIndex(
            Long checklistId,
            Long orderIndex
    );

    @Query("""
        SELECT COUNT(q)
        FROM ChecklistQuestion q
        WHERE q.checklist.id = :checklistId
        """)
    long countByChecklistId(Long checklistId);

    @Query("""
        SELECT q
        FROM ChecklistQuestion q
        WHERE q.checklist.id = :checklistId
        """)
    Page<ChecklistQuestion> findAllByChecklistId(Long checklistId, Pageable pageable);

}