package com.stalyon.poker.service;

import com.stalyon.poker.domain.Player;
import com.stalyon.poker.repository.PlayerRepository;
import com.stalyon.poker.service.mapper.PlayerMapper;
import com.stalyon.poker.web.websocket.dto.PlayerDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerDataService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerMapper playerMapper;

    public List<PlayerDataDto> getPlayerDatas(String searchText) {
        List<Player> players = this.playerRepository.findByNameIgnoreCaseContaining(searchText);
        return this.playerMapper.playerToPlayerDataDtos(players);
    }
}
