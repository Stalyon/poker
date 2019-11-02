package com.stalyon.poker.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "added_date")
    private Instant addedDate;

    @Column(name = "is_me")
    private Boolean isMe;

    @Formula("(select count(distinct pa.hand_id) from player_action pa where pa.player_id = id)")
    private Integer nbHands;

    @Formula("(select CASE WHEN count(distinct pa.hand_id) != 0 THEN (" +
        "100.0 " +
        "* count(select distinct pa2.hand_id from player_action pa2 where pa2.id = pa.id " +
        "       and (pa2.action = 'CALLS' or pa2.action = 'RAISES')) " +
        "/ count(select distinct pa3.hand_id from player_action pa3 where pa3.id = pa.id)) ELSE 0. END " +
        "from player_action pa where pa.player_id = id and pa.betting_round = 'PRE_FLOP')")
    private Double vPip;

    @Formula("(select CASE WHEN count(distinct pa.hand_id) != 0 THEN (" +
        "100.0 " +
        "* count(select distinct pa2.hand_id from player_action pa2 where pa2.id = pa.id " +
        "       and pa2.action = 'RAISES') " +
        "/ count(select distinct pa3.hand_id from player_action pa3 where pa3.id = pa.id)) ELSE 0. END " +
        "from player_action pa where pa.player_id = id and pa.betting_round = 'PRE_FLOP')")
    private Double pfr;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getAddedDate() {
        return addedDate;
    }

    public Player addedDate(Instant addedDate) {
        this.addedDate = addedDate;
        return this;
    }

    public void setAddedDate(Instant addedDate) {
        this.addedDate = addedDate;
    }

    public Boolean isIsMe() {
        return isMe;
    }

    public Player isMe(Boolean isMe) {
        this.isMe = isMe;
        return this;
    }

    public void setIsMe(Boolean isMe) {
        this.isMe = isMe;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Integer getNbHands() {
        return nbHands;
    }

    public void setNbHands(Integer nbHands) {
        this.nbHands = nbHands;
    }

    public Double getvPip() {
        return vPip;
    }

    public void setvPip(Double vPip) {
        this.vPip = vPip;
    }

    public Double getPfr() {
        return pfr;
    }

    public void setPfr(Double pfr) {
        this.pfr = pfr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", addedDate='" + getAddedDate() + "'" +
            ", isMe='" + isIsMe() + "'" +
            "}";
    }
}
