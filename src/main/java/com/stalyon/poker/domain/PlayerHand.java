package com.stalyon.poker.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A PlayerHand.
 */
@Entity
@Table(name = "player_hand")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlayerHand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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
    @JsonIgnoreProperties("playerHands")
    private Player player;

    @ManyToOne
    @JsonIgnoreProperties("playerHands")
    private Hand hand;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isCallsPf() {
        return callsPf;
    }

    public PlayerHand callsPf(Boolean callsPf) {
        this.callsPf = callsPf;
        return this;
    }

    public void setCallsPf(Boolean callsPf) {
        this.callsPf = callsPf;
    }

    public Boolean isRaisesPf() {
        return raisesPf;
    }

    public PlayerHand raisesPf(Boolean raisesPf) {
        this.raisesPf = raisesPf;
        return this;
    }

    public void setRaisesPf(Boolean raisesPf) {
        this.raisesPf = raisesPf;
    }

    public Boolean isThreeBetPf() {
        return threeBetPf;
    }

    public PlayerHand threeBetPf(Boolean threeBetPf) {
        this.threeBetPf = threeBetPf;
        return this;
    }

    public void setThreeBetPf(Boolean threeBetPf) {
        this.threeBetPf = threeBetPf;
    }

    public Boolean isCallsFlop() {
        return callsFlop;
    }

    public PlayerHand callsFlop(Boolean callsFlop) {
        this.callsFlop = callsFlop;
        return this;
    }

    public void setCallsFlop(Boolean callsFlop) {
        this.callsFlop = callsFlop;
    }

    public Boolean isBetsFlop() {
        return betsFlop;
    }

    public PlayerHand betsFlop(Boolean betsFlop) {
        this.betsFlop = betsFlop;
        return this;
    }

    public void setBetsFlop(Boolean betsFlop) {
        this.betsFlop = betsFlop;
    }

    public Boolean isRaisesFlop() {
        return raisesFlop;
    }

    public PlayerHand raisesFlop(Boolean raisesFlop) {
        this.raisesFlop = raisesFlop;
        return this;
    }

    public void setRaisesFlop(Boolean raisesFlop) {
        this.raisesFlop = raisesFlop;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerHand player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Hand getHand() {
        return hand;
    }

    public PlayerHand hand(Hand hand) {
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
        if (!(o instanceof PlayerHand)) {
            return false;
        }
        return id != null && id.equals(((PlayerHand) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PlayerHand{" +
            "id=" + getId() +
            ", callsPf='" + isCallsPf() + "'" +
            ", raisesPf='" + isRaisesPf() + "'" +
            ", threeBetPf='" + isThreeBetPf() + "'" +
            ", callsFlop='" + isCallsFlop() + "'" +
            ", betsFlop='" + isBetsFlop() + "'" +
            ", raisesFlop='" + isRaisesFlop() + "'" +
            "}";
    }
}
