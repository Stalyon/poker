package com.stalyon.poker.repository;
import com.stalyon.poker.domain.SitAndGo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SitAndGo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SitAndGoRepository extends JpaRepository<SitAndGo, Long> {

}
