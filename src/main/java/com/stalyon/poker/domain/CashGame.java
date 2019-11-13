package com.stalyon.poker.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A CashGame.
 */
@Entity
@Table(name = "cash_game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CashGame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "profit")
    private Double profit;

    @Column(name = "jhi_table")
    private String table;

    @Column(name = "sb_bb")
    private String sbBb;

    @Column(name = "nb_hands")
    private Integer nbHands;

    @OneToOne(mappedBy = "cashGame")
    @JsonIgnore
    private GameHistory gameHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public CashGame endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Double getProfit() {
        return profit;
    }

    public CashGame profit(Double profit) {
        this.profit = profit;
        return this;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public String getTable() {
        return table;
    }

    public CashGame table(String table) {
        this.table = table;
        return this;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSbBb() {
        return sbBb;
    }

    public CashGame sbBb(String sbBb) {
        this.sbBb = sbBb;
        return this;
    }

    public void setSbBb(String sbBb) {
        this.sbBb = sbBb;
    }

    public Integer getNbHands() {
        return nbHands;
    }

    public CashGame nbHands(Integer nbHands) {
        this.nbHands = nbHands;
        return this;
    }

    public void setNbHands(Integer nbHands) {
        this.nbHands = nbHands;
    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public CashGame gameHistory(GameHistory gameHistory) {
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
        if (!(o instanceof CashGame)) {
            return false;
        }
        return id != null && id.equals(((CashGame) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CashGame{" +
            "id=" + getId() +
            ", endDate='" + getEndDate() + "'" +
            ", profit=" + getProfit() +
            ", table='" + getTable() + "'" +
            ", sbBb='" + getSbBb() + "'" +
            ", nbHands=" + getNbHands() +
            "}";
    }
}
