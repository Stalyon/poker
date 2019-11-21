package com.stalyon.poker.service.mapper;

import com.stalyon.poker.domain.PlayerData;
import com.stalyon.poker.web.websocket.dto.PlayerDataDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerMapper {

    public List<PlayerDataDto> playerToPlayerDataDtos(List<PlayerData> playerDatas) {
        if (playerDatas == null) {
            return new ArrayList<>();
        }
        return playerDatas.stream().map(this::playerToPlayerDataDto).collect(Collectors.toList());
    }

    private PlayerDataDto playerToPlayerDataDto(PlayerData playerData) {
        PlayerDataDto playerDataDto = new PlayerDataDto();
        playerDataDto.setName(playerData.getName());
        playerDataDto.setNbHands(playerData.getNbHands());
        playerDataDto.setvPip(playerData.getvPip());
        playerDataDto.setPfr(playerData.getPfr());
        playerDataDto.setcBet(playerData.getcBet());
        playerDataDto.setThreeBet(playerData.getThreeBet());
        return playerDataDto;
    }
}
