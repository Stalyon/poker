package com.stalyon.poker.repository;

import com.stalyon.poker.domain.PlayerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface PlayerDataRepository extends JpaRepository<PlayerData, Long> {

    List<PlayerData> findByNameIgnoreCaseContaining(String name);
}
