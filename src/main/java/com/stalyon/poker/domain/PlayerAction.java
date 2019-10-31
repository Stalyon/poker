package com.stalyon.poker.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A PlayerAction.
 */
@Entity
@Table(name = "player_action")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlayerAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JsonIgnoreProperties("playerActions")
    private Player player;

    @ManyToOne
    @JsonIgnoreProperties("playerActions")
    private Game game;

    @ManyToOne
    @JsonIgnoreProperties("playerActions")
    private Hand hand;

    @ManyToOne
    @JsonIgnoreProperties("playerActions")
    private Action action;

    @ManyToOne
    @JsonIgnoreProperties("playerActions")
    private BettingRound bettingRound;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public PlayerAction amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerAction player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public PlayerAction game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Hand getHand() {
        return hand;
    }

    public PlayerAction hand(Hand hand) {
        this.hand = hand;
        return this;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Action getAction() {
        return action;
    }

    public PlayerAction action(Action action) {
        this.action = action;
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public BettingRound getBettingRound() {
        return bettingRound;
    }

    public PlayerAction bettingRound(BettingRound bettingRound) {
        this.bettingRound = bettingRound;
        return this;
    }

    public void setBettingRound(BettingRound bettingRound) {
        this.bettingRound = bettingRound;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerAction)) {
            return false;
        }
        return id != null && id.equals(((PlayerAction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
