package com.mvs.stockmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mvs.stockmanager.domain.enumeration.AlertType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * Alert entity.\n@author Mercedes Vera Sotelo.
 */
@Entity
@Table(name = "alert")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "datetime")
    private Instant datetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AlertType type;

    @Column(name = "rectification_datetime")
    private Instant rectificationDatetime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "alerts", "stocks", "actions" }, allowSetters = true)
    private Store provider;

    @ManyToOne
    @JsonIgnoreProperties(value = { "alerts", "actions", "article", "store" }, allowSetters = true)
    private Stock stock;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alert id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatetime() {
        return this.datetime;
    }

    public Alert datetime(Instant datetime) {
        this.setDatetime(datetime);
        return this;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public AlertType getType() {
        return this.type;
    }

    public Alert type(AlertType type) {
        this.setType(type);
        return this;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public Instant getRectificationDatetime() {
        return this.rectificationDatetime;
    }

    public Alert rectificationDatetime(Instant rectificationDatetime) {
        this.setRectificationDatetime(rectificationDatetime);
        return this;
    }

    public void setRectificationDatetime(Instant rectificationDatetime) {
        this.rectificationDatetime = rectificationDatetime;
    }

    public Store getProvider() {
        return this.provider;
    }

    public void setProvider(Store store) {
        this.provider = store;
    }

    public Alert provider(Store store) {
        this.setProvider(store);
        return this;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Alert stock(Stock stock) {
        this.setStock(stock);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alert)) {
            return false;
        }
        return id != null && id.equals(((Alert) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alert{" +
            "id=" + getId() +
            ", datetime='" + getDatetime() + "'" +
            ", type='" + getType() + "'" +
            ", rectificationDatetime='" + getRectificationDatetime() + "'" +
            "}";
    }
}
