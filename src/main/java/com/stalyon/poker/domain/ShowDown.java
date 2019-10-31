package com.stalyon.poker.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ShowDown.
 */
@Entity
@Table(name = "show_down")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShowDown implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "cards")
    private String cards;

    @Column(name = "wins")
    private Boolean wins;

    @ManyToOne
    @JsonIgnoreProperties("showDowns")
    private Hand hand;

    @ManyToOne
    @JsonIgnoreProperties("showDowns")
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCards() {
        return cards;
    }

    public ShowDown cards(String cards) {
        this.cards = cards;
        return this;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public Boolean isWins() {
        return wins;
    }

    public ShowDown wins(Boolean wins) {
        this.wins = wins;
        return this;
    }

    public void setWins(Boolean wins) {
        this.wins = wins;
    }

    public Hand getHand() {
        return hand;
    }

    public ShowDown hand(Hand hand) {
        this.hand = hand;
        return this;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Player getPlayer() {
        return player;
    }

    public ShowDown player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShowDown)) {
            return false;
        }
        return id != null && id.equals(((ShowDown) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShowDown{" +
            "id=" + getId() +
            ", cards='" + getCards() + "'" +
            ", wins='" + isWins() + "'" +
            "}";
    }
}
