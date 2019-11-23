package com.stalyon.poker.repository;
import com.stalyon.poker.domain.GameHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the GameHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {

    Optional<GameHistory> findByStartDateAndName(Instant startDate, String name);
    List<GameHistory> findAllByOrderByStartDate();
}
