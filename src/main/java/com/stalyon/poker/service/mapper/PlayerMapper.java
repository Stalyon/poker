package com.stalyon.poker.service.mapper;

import com.stalyon.poker.domain.Player;
import com.stalyon.poker.web.websocket.dto.PlayerDataDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerMapper {

    public PlayerDataDto playerToPlayerDataDto(Player player) {
        PlayerDataDto playerDataDto = new PlayerDataDto();
        playerDataDto.setName(player.getName());
        playerDataDto.setNbHands(player.getNbHands());
        playerDataDto.setvPip(player.getvPip());
        playerDataDto.setPfr(player.getPfr());
        playerDataDto.setMe(player.isIsMe());
        return playerDataDto;
    }

    public List<PlayerDataDto> playerToPlayerDataDtos(List<Player> players) {
        if (players == null) {
            return new ArrayList<>();
        }
        return players.stream().map(this::playerToPlayerDataDto).collect(Collectors.toList());
    }
}
