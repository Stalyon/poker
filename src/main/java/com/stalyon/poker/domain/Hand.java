package com.stalyon.poker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Hand.
 */
@Entity
@Table(name = "hand")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "button_position")
    private Integer buttonPosition;

    @Column(name = "my_cards")
    private String myCards;

    @Column(name = "flop_cards")
    private String flopCards;

    @Column(name = "river_cards")
    private String riverCards;

    @Column(name = "turn_cards")
    private String turnCards;

    @ManyToOne
    @JsonIgnoreProperties("hands")
    private Game game;

    @ManyToOne
    @JsonIgnoreProperties("hands")
    private Player winner;

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

    public Hand startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public Integer getButtonPosition() {
        return buttonPosition;
    }

    public void setButtonPosition(Integer buttonPosition) {
        this.buttonPosition = buttonPosition;
    }

    public Hand buttonPosition(Integer buttonPosition) {
        this.buttonPosition = buttonPosition;
        return this;
    }

    public String getMyCards() {
        return myCards;
    }

    public void setMyCards(String myCards) {
        this.myCards = myCards;
    }

    public Hand myCards(String myCards) {
        this.myCards = myCards;
        return this;
    }

    public String getFlopCards() {
        return flopCards;
    }

    public void setFlopCards(String flopCards) {
        this.flopCards = flopCards;
    }

    public Hand flopCards(String flopCards) {
        this.flopCards = flopCards;
        return this;
    }

    public String getRiverCards() {
        return riverCards;
    }

    public void setRiverCards(String riverCards) {
        this.riverCards = riverCards;
    }

    public Hand riverCards(String riverCards) {
        this.riverCards = riverCards;
        return this;
    }

    public String getTurnCards() {
        return turnCards;
    }

    public void setTurnCards(String turnCards) {
        this.turnCards = turnCards;
    }

    public Hand turnCards(String turnCards) {
        this.turnCards = turnCards;
        return this;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Hand game(Game game) {
        this.game = game;
        return this;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player player) {
        this.winner = player;
    }

    public Hand winner(Player player) {
        this.winner = player;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hand)) {
            return false;
        }
        return id != null && id.equals(((Hand) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Hand{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", buttonPosition=" + getButtonPosition() +
            ", myCards='" + getMyCards() + "'" +
            ", flopCards='" + getFlopCards() + "'" +
            ", riverCards='" + getRiverCards() + "'" +
            ", turnCards='" + getTurnCards() + "'" +
            "}";
    }
}
