package com.stalyon.poker.web.dto;

import com.stalyon.poker.domain.enumeration.GameType;
import com.stalyon.poker.domain.enumeration.SitAndGoFormat;

import java.time.Instant;
import java.util.List;

public class RequestStatsDto {

    private List<GameType> gameTypes;
    private List<SitAndGoFormat> sitAndGoFormats;
    private Instant beforeDate;
    private Instant afterDate;

    public RequestStatsDto() {
        // Do nothing
    }

    public List<GameType> getGameTypes() {
        return gameTypes;
    }

    public void setGameTypes(List<GameType> gameTypes) {
        this.gameTypes = gameTypes;
    }

    public List<SitAndGoFormat> getSitAndGoFormats() {
        return sitAndGoFormats;
    }

    public void setSitAndGoFormats(List<SitAndGoFormat> sitAndGoFormats) {
        this.sitAndGoFormats = sitAndGoFormats;
    }

    public Instant getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(Instant beforeDate) {
        this.beforeDate = beforeDate;
    }

    public Instant getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(Instant afterDate) {
        this.afterDate = afterDate;
    }
}
