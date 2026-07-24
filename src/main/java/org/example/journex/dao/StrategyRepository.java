package org.example.journex.dao;

import org.example.journex.domain.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    @Query("""
       SELECT s
       FROM Strategy s
       WHERE s.id = :strategyId
       AND s.user.id = :userId
       """)
    Optional<Strategy> findByIdAndUserId(Long strategyId, Long userId );

    @Query("""
       SELECT s
       FROM Strategy s
       WHERE s.user.id = :userId
       """)
    Page<Strategy> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
       SELECT s
       FROM Strategy s
       WHERE s.user.id = :userId
       AND s.address = :address
       """)
    Optional<Strategy> findByUserIdAndAddress(Long userId, String address);

    @Query("""
       SELECT s
       FROM Strategy s
       WHERE s.publicStrategy = true
       AND s.address = :address
       """)
    Optional<Strategy> findByPublicStrategyAndAddress( String address);
}