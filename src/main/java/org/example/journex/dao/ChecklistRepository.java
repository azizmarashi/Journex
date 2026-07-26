package org.example.journex.dao;

import org.example.journex.domain.Checklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    @Query("SELECT c FROM Checklist c WHERE c.id IN :ids AND (c.deleted = false OR c.deleted IS NULL)")
    List<Checklist> findAllByIds(List<Long> ids);

    @Query("SELECT c FROM Checklist c WHERE c.user.id = :userId AND (c.deleted = false OR c.deleted IS NULL)")
    Page<Checklist> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
            SELECT c FROM Checklist c WHERE c.user.id = :userId
            AND (c.deleted = false OR c.deleted IS NULL)
            AND c.active = true
            """)
    Page<Checklist> findAllActives(Long userId, Pageable pageable);

    @Query("""
        SELECT c FROM Checklist c
        WHERE c.id = :id AND c.user.id = :userId
        AND (c.deleted = false OR c.deleted IS NULL)
        """)
    Optional<Checklist> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT c FROM Checklist c WHERE c.user.id = :userId AND c.deleted = true")
    Page<Checklist> findAllDeleted(Long userId, Pageable pageable);

    @Query("SELECT c FROM Checklist c WHERE c.id = :id AND c.user.id = :userId AND c.deleted = true")
    Optional<Checklist> findDeleted(Long id, Long userId);

    @Query("""
    SELECT c FROM Checklist c
    JOIN c.strategies s
    WHERE s.id = :strategyId
    AND c.user.id = :userId
    AND (c.deleted = false OR c.deleted IS NULL)
    AND c.active = true
    """)
    Page<Checklist> findActivesByStrategyId(Long userId, Long strategyId, Pageable pageable);

}