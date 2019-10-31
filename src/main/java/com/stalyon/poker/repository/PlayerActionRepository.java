package com.stalyon.poker.repository;
import com.stalyon.poker.domain.PlayerAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlayerAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerActionRepository extends JpaRepository<PlayerAction, Long> {

}
