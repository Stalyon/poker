package com.stalyon.poker.repository;

import com.stalyon.poker.domain.Hand;
import com.stalyon.poker.domain.Player;
import com.stalyon.poker.domain.PlayerHand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the PlayerHand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerHandRepository extends JpaRepository<PlayerHand, Long> {

    Optional<PlayerHand> findByHandAndPlayer(Hand hand, Player player);
}
