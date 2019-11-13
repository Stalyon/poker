package com.stalyon.poker.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Tournoi.
 */
@Entity
@Table(name = "tournoi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tournoi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "buy_in")
    private Double buyIn;

    @Column(name = "rebuy")
    private Double rebuy;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "profit")
    private Double profit;

    @Column(name = "bounty")
    private Double bounty;

    @OneToOne(mappedBy = "tournoi")
    @JsonIgnore
    private GameHistory gameHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBuyIn() {
        return buyIn;
    }

    public Tournoi buyIn(Double buyIn) {
        this.buyIn = buyIn;
        return this;
    }

    public void setBuyIn(Double buyIn) {
        this.buyIn = buyIn;
    }

    public Double getRebuy() {
        return rebuy;
    }

    public Tournoi rebuy(Double rebuy) {
        this.rebuy = rebuy;
        return this;
    }

    public void setRebuy(Double rebuy) {
        this.rebuy = rebuy;
    }

    public Integer getRanking() {
        return ranking;
    }

    public Tournoi ranking(Integer ranking) {
        this.ranking = ranking;
        return this;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Double getProfit() {
        return profit;
    }

    public Tournoi profit(Double profit) {
        this.profit = profit;
        return this;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getBounty() {
        return bounty;
    }

    public Tournoi bounty(Double bounty) {
        this.bounty = bounty;
        return this;
    }

    public void setBounty(Double bounty) {
        this.bounty = bounty;
    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public Tournoi gameHistory(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
        return this;
    }

    public void setGameHistory(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tournoi)) {
            return false;
        }
        return id != null && id.equals(((Tournoi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tournoi{" +
            "id=" + getId() +
            ", buyIn=" + getBuyIn() +
            ", rebuy=" + getRebuy() +
            ", ranking=" + getRanking() +
            ", profit=" + getProfit() +
            ", bounty=" + getBounty() +
            "}";
    }
}
