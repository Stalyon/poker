package com.stalyon.poker.repository;
import com.stalyon.poker.domain.PlayerHand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlayerHand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerHandRepository extends JpaRepository<PlayerHand, Long> {

}
