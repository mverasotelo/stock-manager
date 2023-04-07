package com.mvs.stockmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mvs.stockmanager.domain.enumeration.StoreType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Store entity.\n@author Mercedes Vera Sotelo.
 */
@Entity
@Table(name = "store")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private StoreType type;

    @OneToMany(mappedBy = "provider")
    @JsonIgnoreProperties(value = { "provider", "stock" }, allowSetters = true)
    private Set<Alert> alerts = new HashSet<>();

    @OneToMany(mappedBy = "store")
    @JsonIgnoreProperties(value = { "alerts", "actions", "article", "store" }, allowSetters = true)
    private Set<Stock> stocks = new HashSet<>();

    @OneToMany(mappedBy = "store")
    @JsonIgnoreProperties(value = { "employee", "stock", "store" }, allowSetters = true)
    private Set<Action> actions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Store id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Store code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public StoreType getType() {
        return this.type;
    }

    public Store type(StoreType type) {
        this.setType(type);
        return this;
    }

    public void setType(StoreType type) {
        this.type = type;
    }

    public Set<Alert> getAlerts() {
        return this.alerts;
    }

    public void setAlerts(Set<Alert> alerts) {
        if (this.alerts != null) {
            this.alerts.forEach(i -> i.setProvider(null));
        }
        if (alerts != null) {
            alerts.forEach(i -> i.setProvider(this));
        }
        this.alerts = alerts;
    }

    public Store alerts(Set<Alert> alerts) {
        this.setAlerts(alerts);
        return this;
    }

    public Store addAlerts(Alert alert) {
        this.alerts.add(alert);
        alert.setProvider(this);
        return this;
    }

    public Store removeAlerts(Alert alert) {
        this.alerts.remove(alert);
        alert.setProvider(null);
        return this;
    }

    public Set<Stock> getStocks() {
        return this.stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        if (this.stocks != null) {
            this.stocks.forEach(i -> i.setStore(null));
        }
        if (stocks != null) {
            stocks.forEach(i -> i.setStore(this));
        }
        this.stocks = stocks;
    }

    public Store stocks(Set<Stock> stocks) {
        this.setStocks(stocks);
        return this;
    }

    public Store addStocks(Stock stock) {
        this.stocks.add(stock);
        stock.setStore(this);
        return this;
    }

    public Store removeStocks(Stock stock) {
        this.stocks.remove(stock);
        stock.setStore(null);
        return this;
    }

    public Set<Action> getActions() {
        return this.actions;
    }

    public void setActions(Set<Action> actions) {
        if (this.actions != null) {
            this.actions.forEach(i -> i.setStore(null));
        }
        if (actions != null) {
            actions.forEach(i -> i.setStore(this));
        }
        this.actions = actions;
    }

    public Store actions(Set<Action> actions) {
        this.setActions(actions);
        return this;
    }

    public Store addActions(Action action) {
        this.actions.add(action);
        action.setStore(this);
        return this;
    }

    public Store removeActions(Action action) {
        this.actions.remove(action);
        action.setStore(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Store)) {
            return false;
        }
        return id != null && id.equals(((Store) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Store{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
