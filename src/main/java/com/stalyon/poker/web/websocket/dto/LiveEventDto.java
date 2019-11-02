package com.stalyon.poker.web.websocket.dto;

import java.util.ArrayList;
import java.util.List;

public class LiveEventDto {

    private String gameName;
    private Long gameId;
    private List<PlayerDataDto> players;

    public LiveEventDto() {
        this.players = new ArrayList<>();
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<PlayerDataDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDataDto> players) {
        this.players = players;
    }
}
