package com.stalyon.poker.repository;

import com.stalyon.poker.domain.Hand;
import com.stalyon.poker.domain.Player;
import com.stalyon.poker.domain.ShowDown;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the ShowDown entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShowDownRepository extends JpaRepository<ShowDown, Long> {

    Optional<ShowDown> findByHandAndPlayer(Hand hand, Player player);
}
