package com.stalyon.poker.repository;

import com.stalyon.poker.domain.GameHistory;
import com.stalyon.poker.domain.enumeration.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<GameHistory> findAllByStartDateBeforeAndStartDateAfterAndTypeInOrderByStartDate(Instant beforeDate, Instant afterDate,
                                                                                       List<GameType> gameTypes);
}
