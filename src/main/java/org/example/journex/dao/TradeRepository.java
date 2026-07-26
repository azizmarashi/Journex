package org.example.journex.dao;
import org.example.journex.domain.Trade;
import org.example.journex.enums.TradeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query("SELECT t FROM Trade t WHERE t.user.id = :userId AND (t.deleted = false OR t.deleted IS NULL)")
    Page<Trade> findAllActiveByUserId(Long userId, Pageable pageable);

    @Query("""
        SELECT t FROM Trade t
        WHERE t.id = :id AND t.user.id = :userId
        AND (t.deleted = false OR t.deleted IS NULL)
        """)
    Optional<Trade> findByIdAndUserIdAndDeletedFalseOrDeletedIsNull(Long id, Long userId);

    @Query("""
        SELECT t FROM Trade t
        WHERE t.user.id = :userId AND t.status = :status
        AND (t.deleted = false OR t.deleted IS NULL)
        """)
    Page<Trade> findAllByUserIdAndStatus(Long userId, TradeStatus status, Pageable pageable);

    @Query("""
        SELECT t FROM Trade t
        WHERE t.strategy.id = :strategyId AND t.user.id = :userId
        AND (t.deleted = false OR t.deleted IS NULL)
        """)
    Page<Trade> findAllByStrategyIdAndUserId(Long strategyId, Long userId, Pageable pageable);

    @Query("SELECT t FROM Trade t WHERE t.user.id = :userId AND t.deleted = true")
    Page<Trade> findAllDeletedByUserId(Long userId, Pageable pageable);

    @Query("SELECT t FROM Trade t WHERE t.id = :id AND t.user.id = :userId AND t.deleted = true")
    Optional<Trade> findByIdAndUserIdAndDeletedTrue(Long id, Long userId);
}