package com.stalyon.poker.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import com.stalyon.poker.domain.enumeration.BettingRound;

import com.stalyon.poker.domain.enumeration.Action;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "betting_round")
    private BettingRound bettingRound;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Action action;

    @Column(name = "calls_pf")
    private Boolean callsPf;

    @Column(name = "raises_pf")
    private Boolean raisesPf;

    @Column(name = "three_bet_pf")
    private Boolean threeBetPf;

    @Column(name = "calls_flop")
    private Boolean callsFlop;

    @Column(name = "bets_flop")
    private Boolean betsFlop;

    @Column(name = "raises_flop")
    private Boolean raisesFlop;

    @ManyToOne
    @JsonIgnoreProperties("playerActions")
    private Player player;

    @ManyToOne
    @JsonIgnoreProperties("playerActions")
    private Game game;

    @ManyToOne
    @JsonIgnoreProperties("playerActions")
    private Hand hand;

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

    public Boolean isCallsPf() {
        return callsPf;
    }

    public PlayerAction callsPf(Boolean callsPf) {
        this.callsPf = callsPf;
        return this;
    }

    public void setCallsPf(Boolean callsPf) {
        this.callsPf = callsPf;
    }

    public Boolean isRaisesPf() {
        return raisesPf;
    }

    public PlayerAction raisesPf(Boolean raisesPf) {
        this.raisesPf = raisesPf;
        return this;
    }

    public void setRaisesPf(Boolean raisesPf) {
        this.raisesPf = raisesPf;
    }

    public Boolean isThreeBetPf() {
        return threeBetPf;
    }

    public PlayerAction threeBetPf(Boolean threeBetPf) {
        this.threeBetPf = threeBetPf;
        return this;
    }

    public void setThreeBetPf(Boolean threeBetPf) {
        this.threeBetPf = threeBetPf;
    }

    public Boolean isCallsFlop() {
        return callsFlop;
    }

    public PlayerAction callsFlop(Boolean callsFlop) {
        this.callsFlop = callsFlop;
        return this;
    }

    public void setCallsFlop(Boolean callsFlop) {
        this.callsFlop = callsFlop;
    }

    public Boolean isBetsFlop() {
        return betsFlop;
    }

    public PlayerAction betsFlop(Boolean betsFlop) {
        this.betsFlop = betsFlop;
        return this;
    }

    public void setBetsFlop(Boolean betsFlop) {
        this.betsFlop = betsFlop;
    }

    public Boolean isRaisesFlop() {
        return raisesFlop;
    }

    public PlayerAction raisesFlop(Boolean raisesFlop) {
        this.raisesFlop = raisesFlop;
        return this;
    }

    public void setRaisesFlop(Boolean raisesFlop) {
        this.raisesFlop = raisesFlop;
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
            ", bettingRound='" + getBettingRound() + "'" +
            ", action='" + getAction() + "'" +
            ", callsPf='" + isCallsPf() + "'" +
            ", raisesPf='" + isRaisesPf() + "'" +
            ", threeBetPf='" + isThreeBetPf() + "'" +
            ", callsFlop='" + isCallsFlop() + "'" +
            ", betsFlop='" + isBetsFlop() + "'" +
            ", raisesFlop='" + isRaisesFlop() + "'" +
            "}";
    }
}
