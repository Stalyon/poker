package com.stalyon.poker.repository;

import com.stalyon.poker.domain.Tournoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Tournoi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TournoiRepository extends JpaRepository<Tournoi, Long> {

}
