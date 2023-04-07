package com.mvs.stockmanager.service.dto;

import com.mvs.stockmanager.domain.enumeration.ActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mvs.stockmanager.domain.Action} entity.
 */
@Schema(description = "Action entity.\n@author Mercedes Vera Sotelo.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActionDTO implements Serializable {

    private Long id;

    private String code;

    private Instant datetime;

    private ActionType type;

    private Long quantity;

    private EmployeeDTO employee;

    private StockDTO stock;

    private StoreDTO store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public StockDTO getStock() {
        return stock;
    }

    public void setStock(StockDTO stock) {
        this.stock = stock;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionDTO)) {
            return false;
        }

        ActionDTO actionDTO = (ActionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, actionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActionDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", datetime='" + getDatetime() + "'" +
            ", type='" + getType() + "'" +
            ", quantity=" + getQuantity() +
            ", employee=" + getEmployee() +
            ", stock=" + getStock() +
            ", store=" + getStore() +
            "}";
    }
}
