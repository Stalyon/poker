package com.stalyon.poker.repository;
import com.stalyon.poker.domain.GameHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GameHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {

}
