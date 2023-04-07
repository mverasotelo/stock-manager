package com.mvs.stockmanager.service.dto;

import com.mvs.stockmanager.domain.enumeration.AlertType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mvs.stockmanager.domain.Alert} entity.
 */
@Schema(description = "Alert entity.\n@author Mercedes Vera Sotelo.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlertDTO implements Serializable {

    private Long id;

    private Instant datetime;

    private AlertType type;

    private Instant rectificationDatetime;

    private StoreDTO provider;

    private StockDTO stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public Instant getRectificationDatetime() {
        return rectificationDatetime;
    }

    public void setRectificationDatetime(Instant rectificationDatetime) {
        this.rectificationDatetime = rectificationDatetime;
    }

    public StoreDTO getProvider() {
        return provider;
    }

    public void setProvider(StoreDTO provider) {
        this.provider = provider;
    }

    public StockDTO getStock() {
        return stock;
    }

    public void setStock(StockDTO stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertDTO)) {
            return false;
        }

        AlertDTO alertDTO = (AlertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertDTO{" +
            "id=" + getId() +
            ", datetime='" + getDatetime() + "'" +
            ", type='" + getType() + "'" +
            ", rectificationDatetime='" + getRectificationDatetime() + "'" +
            ", provider=" + getProvider() +
            ", stock=" + getStock() +
            "}";
    }
}
