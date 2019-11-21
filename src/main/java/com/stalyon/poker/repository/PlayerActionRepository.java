package com.stalyon.poker.repository;

import com.stalyon.poker.domain.Hand;
import com.stalyon.poker.domain.PlayerAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PlayerAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerActionRepository extends JpaRepository<PlayerAction, Long> {

    List<PlayerAction> findAllByHand(Hand hand);
}
