package org.example.journex.dao;

import org.example.journex.domain.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    @Query("SELECT s FROM Strategy s WHERE s.user.id = :userId AND (s.deleted = false OR s.deleted IS NULL)")
    Page<Strategy> findAll(Long userId, Pageable pageable);

    @Query("""
        SELECT s FROM Strategy s
        WHERE s.id = :id AND s.user.id = :userId
        AND (s.deleted = false OR s.deleted IS NULL)
        """)
    Optional<Strategy> findById(Long id, Long userId);

    @Query("SELECT s FROM Strategy s WHERE s.user.id = :userId AND s.deleted = true")
    Page<Strategy> findAllDeleted(Long userId, Pageable pageable);

    @Query("SELECT s FROM Strategy s WHERE s.id = :id AND s.user.id = :userId AND s.deleted = true")
    Optional<Strategy> findByIdAndUserIdAndDeletedTrue(Long id, Long userId);

    @EntityGraph(attributePaths = {"checklists"})
    @Query("""
        SELECT s FROM Strategy s
        WHERE s.address = :address AND s.publicStrategy = true
        AND (s.deleted = false OR s.deleted IS NULL)
        """)
    Optional<Strategy> findPublicByAddress(String address);

}