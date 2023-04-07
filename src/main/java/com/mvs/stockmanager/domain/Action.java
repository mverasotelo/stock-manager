package com.mvs.stockmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mvs.stockmanager.domain.enumeration.ActionType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Action entity.\n@author Mercedes Vera Sotelo.
 */
@Entity
@Table(name = "action")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "datetime")
    private Instant datetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ActionType type;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne
    @JsonIgnoreProperties(value = { "actions" }, allowSetters = true)
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "alerts", "actions", "article", "store" }, allowSetters = true)
    private Stock stock;

    @ManyToOne
    @JsonIgnoreProperties(value = { "alerts", "stocks", "actions" }, allowSetters = true)
    private Store store;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Action id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Action code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDatetime() {
        return this.datetime;
    }

    public Action datetime(Instant datetime) {
        this.setDatetime(datetime);
        return this;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public ActionType getType() {
        return this.type;
    }

    public Action type(ActionType type) {
        this.setType(type);
        return this;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public Action quantity(Long quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Action employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Action stock(Stock stock) {
        this.setStock(stock);
        return this;
    }

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Action store(Store store) {
        this.setStore(store);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Action)) {
            return false;
        }
        return id != null && id.equals(((Action) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Action{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", datetime='" + getDatetime() + "'" +
            ", type='" + getType() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
