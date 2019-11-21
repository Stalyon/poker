package com.stalyon.poker.domain;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect(
    "(SELECT " +
        "    player_data.id, " +
        "    player_data.name, " +
        "    player_data.nb_hands as nb_hands, " +
        "    100 * (SELECT COUNT(distinct pa2.hand_id) FROM player_action pa2 WHERE pa2.player_id = player_data.id AND pa2.betting_round = 'PRE_FLOP' AND (pa2.raises_pf = true OR pa2.calls_pf = true)) / NULLIF(player_data.nb_hands, 0) as v_pip, " +
        "    100 * (SELECT COUNT(distinct pa2.hand_id) FROM player_action pa2 WHERE pa2.player_id = player_data.id AND pa2.betting_round = 'PRE_FLOP' AND pa2.raises_pf = true) / NULLIF(player_data.nb_hands, 0) as pfr, " +
        "    0 as c_bet " +
        "FROM " +
        "( " +
        "    SELECT " +
        "     p.id, " +
        "     p.name, " +
        "     count(distinct pa.hand_id) as nb_hands " +
        "    FROM player p " +
        "    LEFT JOIN player_action pa ON p.id = pa.player_id " +
        "    GROUP BY p.id, p.name " +
        ") AS player_data " +
        "group by player_data.id, player_data.name, player_data.nb_hands)"
)
@Synchronize({"player", "player_action"})
@Immutable
public class PlayerData {

    @Id
    private Long id;

    private String name;

    @Column(name = "nb_hands")
    private Integer nbHands;

    @Column(name = "v_pip")
    private Double vPip;

    private Double pfr;

    @Column(name = "c_bet")
    private Double cBet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
