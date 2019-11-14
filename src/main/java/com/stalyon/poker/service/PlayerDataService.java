package com.stalyon.poker.service;

import com.stalyon.poker.domain.PlayerData;
import com.stalyon.poker.repository.PlayerDataRepository;
import com.stalyon.poker.service.mapper.PlayerMapper;
import com.stalyon.poker.web.websocket.dto.PlayerDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerDataService {

    @Autowired
    private PlayerDataRepository playerDataRepository;

    @Autowired
    private PlayerMapper playerMapper;

    public List<PlayerDataDto> getPlayerDatas(String searchText) {
        List<PlayerData> playerDatas = this.playerDataRepository.findByNameIgnoreCaseContaining(searchText);
        return this.playerMapper.playerToPlayerDataDtos(playerDatas);
    }
}
