package com.stalyon.poker.repository;
import com.stalyon.poker.domain.ShowDown;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShowDown entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShowDownRepository extends JpaRepository<ShowDown, Long> {

}
