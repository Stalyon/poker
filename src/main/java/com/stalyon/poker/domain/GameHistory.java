package com.stalyon.poker.domain;

import com.stalyon.poker.domain.enumeration.GameType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A GameHistory.
 */
@Entity
@Table(name = "game_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GameHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private GameType type;

    @Column(name = "net_result")
    private Double netResult;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private SitAndGo sitAndGo;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private Tournoi tournoi;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private CashGame cashGame;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public GameHistory startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameHistory name(String name) {
        this.name = name;
        return this;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public GameHistory type(GameType type) {
        this.type = type;
        return this;
    }

    public Double getNetResult() {
        return netResult;
    }

    public void setNetResult(Double netResult) {
        this.netResult = netResult;
    }

    public GameHistory netResult(Double netResult) {
        this.netResult = netResult;
        return this;
    }

    public SitAndGo getSitAndGo() {
        return sitAndGo;
    }

    public void setSitAndGo(SitAndGo sitAndGo) {
        this.sitAndGo = sitAndGo;
    }

    public GameHistory sitAndGo(SitAndGo sitAndGo) {
        this.sitAndGo = sitAndGo;
        return this;
    }

    public Tournoi getTournoi() {
        return tournoi;
    }

    public void setTournoi(Tournoi tournoi) {
        this.tournoi = tournoi;
    }

    public GameHistory tournoi(Tournoi tournoi) {
        this.tournoi = tournoi;
        return this;
    }

    public CashGame getCashGame() {
        return cashGame;
    }

    public void setCashGame(CashGame cashGame) {
        this.cashGame = cashGame;
    }

    public GameHistory cashGame(CashGame cashGame) {
        this.cashGame = cashGame;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameHistory)) {
            return false;
        }
        return id != null && id.equals(((GameHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", netResult=" + getNetResult() +
            "}";
    }
}
