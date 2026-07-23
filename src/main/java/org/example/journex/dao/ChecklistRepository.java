package org.example.journex.dao;

import org.example.journex.domain.Checklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    @Query("""
       select c 
       from Checklist c
       join fetch c.strategy
       where c.strategy.id = :strategyId
       """)
    Page<Checklist> findAllByStrategyId(Long strategyId, Pageable pageable);

}