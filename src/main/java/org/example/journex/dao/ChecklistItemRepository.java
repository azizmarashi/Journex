package org.example.journex.dao;

import org.example.journex.domain.ChecklistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

    @Query("""
        SELECT i FROM ChecklistItem i
        WHERE i.checklist.id = :checklistId
        AND i.type IN ('QUESTION_BOOLEAN', 'QUESTION_TEXT')
        AND (i.deleted = false OR i.deleted IS NULL)
        """)
    Page<ChecklistItem> findAllByChecklistId(Long checklistId, Pageable pageable);

    @Query("""
        SELECT i FROM ChecklistItem i
        WHERE i.id = :id
        AND i.type IN ('QUESTION_BOOLEAN', 'QUESTION_TEXT')
        AND (i.deleted = false OR i.deleted IS NULL)
        """)
    Optional<ChecklistItem> findQuestionById(Long id);

    @Query("""
        SELECT i FROM ChecklistItem i
        WHERE i.id = :id
        AND (i.deleted = false OR i.deleted IS NULL)
        """)
    Optional<ChecklistItem> findItemId(Long id);

    @Query("""
    SELECT i FROM ChecklistItem i
    WHERE i.id = :id AND i.checklist.id = :checklistId
    AND i.type IN ('QUESTION_BOOLEAN', 'QUESTION_TEXT')
    AND i.deleted = true
    """)
    Optional<ChecklistItem> findByIdAndChecklistIdAndDeletedTrue(Long id, Long checklistId);

    @Query("""
        SELECT i FROM ChecklistItem i
        WHERE i.checklist.id = :checklistId AND i.orderIndex >= :orderIndex
        AND i.type IN ('QUESTION_BOOLEAN', 'QUESTION_TEXT')
        AND (i.deleted = false OR i.deleted IS NULL)
        ORDER BY i.orderIndex
        """)
    List<ChecklistItem> orderingFindByChecklistIdGreaterOrder(Long checklistId, Long orderIndex);

    @Query("""
        SELECT i FROM ChecklistItem i
        WHERE i.checklist.id = :checklistId
        AND i.type IN ('QUESTION_BOOLEAN', 'QUESTION_TEXT')
        AND (i.deleted = false OR i.deleted IS NULL)
        ORDER BY i.orderIndex
        """)
    List<ChecklistItem> orderingFindByChecklistIdOrder(Long checklistId);

    @Query("""
        SELECT i FROM ChecklistItem i
        WHERE i.checklist.id = :checklistId AND i.orderIndex > :orderIndex
        AND i.type IN ('QUESTION_BOOLEAN', 'QUESTION_TEXT')
        AND (i.deleted = false OR i.deleted IS NULL)
        ORDER BY i.orderIndex
        """)
    List<ChecklistItem> orderingFindByChecklistIdAndOrderGreaterOrder(Long checklistId, Long orderIndex);

    @Query("""
        SELECT COUNT(i) FROM ChecklistItem i
        WHERE i.checklist.id = :checklistId
        AND i.type IN ('QUESTION_BOOLEAN', 'QUESTION_TEXT')
        AND (i.deleted = false OR i.deleted IS NULL)
        """)
    long orderingCountByChecklistId(Long checklistId);

}