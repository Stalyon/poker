package com.stalyon.poker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ParseHistory.
 */
@Entity
@Table(name = "parse_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParseHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "parsed_date")
    private Instant parsedDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ParseHistory fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public ParseHistory fileSize(Integer fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public Instant getParsedDate() {
        return parsedDate;
    }

    public void setParsedDate(Instant parsedDate) {
        this.parsedDate = parsedDate;
    }

    public ParseHistory parsedDate(Instant parsedDate) {
        this.parsedDate = parsedDate;
        return this;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ParseHistory game(Game game) {
        this.game = game;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParseHistory)) {
            return false;
        }
        return id != null && id.equals(((ParseHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ParseHistory{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileSize=" + getFileSize() +
            ", parsedDate='" + getParsedDate() + "'" +
            "}";
    }
}
