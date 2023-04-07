package com.mvs.stockmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * Stock entity.\n@author Mercedes Vera Sotelo.
 */
@Entity
@Table(name = "stock")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "actual_stock")
    private Long actualStock;

    @Column(name = "reorder_point")
    private Long reorderPoint;

    @Column(name = "max_stock")
    private Long maxStock;

    @Column(name = "section")
    private String section;

    @Column(name = "level")
    private String level;

    @Column(name = "rack")
    private String rack;

    @OneToMany(mappedBy = "stock")
    @JsonIgnoreProperties(value = { "provider", "stock" }, allowSetters = true)
    private Set<Alert> alerts = new HashSet<>();

    @OneToMany(mappedBy = "stock")
    @JsonIgnoreProperties(value = { "employee", "stock", "store" }, allowSetters = true)
    private Set<Action> actions = new HashSet<>();

    @ManyToOne
    // @JsonIgnoreProperties(value = { "stocks" }, allowSetters = true)
    private Article article;

    @ManyToOne
    // @JsonIgnoreProperties(value = { "alerts", "stocks", "actions" }, allowSetters = true)
    private Store store;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Stock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActualStock() {
        return this.actualStock;
    }

    public Stock actualStock(Long actualStock) {
        this.setActualStock(actualStock);
        return this;
    }

    public void setActualStock(Long actualStock) {
        this.actualStock = actualStock;
    }

    public Long getReorderPoint() {
        return this.reorderPoint;
    }

    public Stock reorderPoint(Long reorderPoint) {
        this.setReorderPoint(reorderPoint);
        return this;
    }

    public void setReorderPoint(Long reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Long getMaxStock() {
        return this.maxStock;
    }

    public Stock maxStock(Long maxStock) {
        this.setMaxStock(maxStock);
        return this;
    }

    public void setMaxStock(Long maxStock) {
        this.maxStock = maxStock;
    }

    public String getSection() {
        return this.section;
    }

    public Stock section(String section) {
        this.setSection(section);
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLevel() {
        return this.level;
    }

    public Stock level(String level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRack() {
        return this.rack;
    }

    public Stock rack(String rack) {
        this.setRack(rack);
        return this;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public Set<Alert> getAlerts() {
        return this.alerts;
    }

    public void setAlerts(Set<Alert> alerts) {
        if (this.alerts != null) {
            this.alerts.forEach(i -> i.setStock(null));
        }
        if (alerts != null) {
            alerts.forEach(i -> i.setStock(this));
        }
        this.alerts = alerts;
    }

    public Stock alerts(Set<Alert> alerts) {
        this.setAlerts(alerts);
        return this;
    }

    public Stock addAlerts(Alert alert) {
        this.alerts.add(alert);
        alert.setStock(this);
        return this;
    }

    public Stock removeAlerts(Alert alert) {
        this.alerts.remove(alert);
        alert.setStock(null);
        return this;
    }

    public Set<Action> getActions() {
        return this.actions;
    }

    public void setActions(Set<Action> actions) {
        if (this.actions != null) {
            this.actions.forEach(i -> i.setStock(null));
        }
        if (actions != null) {
            actions.forEach(i -> i.setStock(this));
        }
        this.actions = actions;
    }

    public Stock actions(Set<Action> actions) {
        this.setActions(actions);
        return this;
    }

    public Stock addActions(Action action) {
        this.actions.add(action);
        action.setStock(this);
        return this;
    }

    public Stock removeActions(Action action) {
        this.actions.remove(action);
        action.setStock(null);
        return this;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Stock article(Article article) {
        this.setArticle(article);
        return this;
    }

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Stock store(Store store) {
        this.setStore(store);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return id != null && id.equals(((Stock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", actualStock=" + getActualStock() +
            ", reorderPoint=" + getReorderPoint() +
            ", maxStock=" + getMaxStock() +
            ", section='" + getSection() + "'" +
            ", level='" + getLevel() + "'" +
            ", rack='" + getRack() + "'" +
            "}";
    }
}
