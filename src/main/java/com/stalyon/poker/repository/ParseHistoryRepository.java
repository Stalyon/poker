package com.stalyon.poker.repository;
import com.stalyon.poker.domain.ParseHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ParseHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParseHistoryRepository extends JpaRepository<ParseHistory, Long> {

    Optional<ParseHistory> findByFileName(String fileName);
}
