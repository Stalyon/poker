package com.stalyon.poker.repository;
import com.stalyon.poker.domain.BettingRound;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BettingRound entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BettingRoundRepository extends JpaRepository<BettingRound, Long> {

}