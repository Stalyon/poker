package com.stalyon.poker.repository;
import com.stalyon.poker.domain.Hand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Hand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HandRepository extends JpaRepository<Hand, Long> {

}
