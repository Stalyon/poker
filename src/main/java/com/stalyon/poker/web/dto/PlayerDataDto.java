package com.stalyon.poker.web.dto;

public class PlayerDataDto {

    private String name;
    private Integer nbHands;
    private Double vPip;
    private Double pfr;
    private Double cBet;
    private Double threeBet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNbHands() {
        return nbHands;
    }

    public void setNbHands(Integer nbHands) {
        this.nbHands = nbHands;
    }

    public Double getvPip() {
        return vPip;
    }

    public void setvPip(Double vPip) {
        this.vPip = vPip;
    }

    public Double getPfr() {
        return pfr;
    }

    public void setPfr(Double pfr) {
        this.pfr = pfr;
    }

    public Double getcBet() {
        return cBet;
    }

    public void setcBet(Double cBet) {
        this.cBet = cBet;
    }

    public Double getThreeBet() {
        return threeBet;
    }

    public void setThreeBet(Double threeBet) {
        this.threeBet = threeBet;
    }
}
