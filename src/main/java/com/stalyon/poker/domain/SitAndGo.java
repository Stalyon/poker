package com.stalyon.poker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stalyon.poker.domain.enumeration.SitAndGoFormat;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A SitAndGo.
 */
@Entity
@Table(name = "sit_and_go")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SitAndGo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private SitAndGoFormat format;

    @Column(name = "buy_in")
    private Double buyIn;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "profit")
    private Double profit;

    @Column(name = "bounty")
    private Double bounty;

    @OneToOne(mappedBy = "sitAndGo")
    @JsonIgnore
    private GameHistory gameHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SitAndGoFormat getFormat() {
        return format;
    }

    public void setFormat(SitAndGoFormat format) {
        this.format = format;
    }

    public SitAndGo format(SitAndGoFormat format) {
        this.format = format;
        return this;
    }

    public Double getBuyIn() {
        return buyIn;
    }

    public void setBuyIn(Double buyIn) {
        this.buyIn = buyIn;
    }

    public SitAndGo buyIn(Double buyIn) {
        this.buyIn = buyIn;
        return this;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public SitAndGo ranking(Integer ranking) {
        this.ranking = ranking;
        return this;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public SitAndGo profit(Double profit) {
        this.profit = profit;
        return this;
    }

    public Double getBounty() {
        return bounty;
    }

    public void setBounty(Double bounty) {
        this.bounty = bounty;
    }

    public SitAndGo bounty(Double bounty) {
        this.bounty = bounty;
        return this;
    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
    }

    public SitAndGo gameHistory(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SitAndGo)) {
            return false;
        }
        return id != null && id.equals(((SitAndGo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SitAndGo{" +
            "id=" + getId() +
            ", format='" + getFormat() + "'" +
            ", buyIn=" + getBuyIn() +
            ", ranking=" + getRanking() +
            ", profit=" + getProfit() +
            ", bounty=" + getBounty() +
            "}";
    }
}
