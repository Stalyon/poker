package com.stalyon.poker.repository;

import com.stalyon.poker.domain.CashGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashGame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashGameRepository extends JpaRepository<CashGame, Long> {

}
