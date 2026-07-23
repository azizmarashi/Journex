package org.example.journex.dao;

import org.example.journex.domain.Checklist;
import org.example.journex.domain.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

}
