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

    public Hand startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Integer getButtonPosition() {
        return buttonPosition;
    }

    public Hand buttonPosition(Integer buttonPosition) {
        this.buttonPosition = buttonPosition;
        return this;
    }

    public void setButtonPosition(Integer buttonPosition) {
        this.buttonPosition = buttonPosition;
    }

    public String getMyCards() {
        return myCards;
    }

    public Hand myCards(String myCards) {
        this.myCards = myCards;
        return this;
    }

    public void setMyCards(String myCards) {
        this.myCards = myCards;
    }

    public Game getGame() {
        return game;
    }

    public Hand game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getWinner() {
        return winner;
    }

    public Hand winner(Player player) {
        this.winner = player;
        return this;
    }

    public void setWinner(Player player) {
        this.winner = player;
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
            "}";
    }
}
