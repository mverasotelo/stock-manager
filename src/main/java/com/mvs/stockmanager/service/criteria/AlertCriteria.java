package com.mvs.stockmanager.service.criteria;

import com.mvs.stockmanager.domain.enumeration.AlertType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mvs.stockmanager.domain.Alert} entity. This class is used
 * in {@link com.mvs.stockmanager.web.rest.AlertResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alerts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlertCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AlertType
     */
    public static class AlertTypeFilter extends Filter<AlertType> {

        public AlertTypeFilter() {}

        public AlertTypeFilter(AlertTypeFilter filter) {
            super(filter);
        }

        @Override
        public AlertTypeFilter copy() {
            return new AlertTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter datetime;

    private AlertTypeFilter type;

    private InstantFilter rectificationDatetime;

    private Boolean isActive;

    private LongFilter providerId;

    private LongFilter stockId;

    private LongFilter storeId;

    private StringFilter articleCode;

    private StringFilter articleDescription;

    private Boolean distinct;

    public AlertCriteria() {}

    public AlertCriteria(AlertCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.datetime = other.datetime == null ? null : other.datetime.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.rectificationDatetime = other.rectificationDatetime == null ? null : other.rectificationDatetime.copy();
        this.providerId = other.providerId == null ? null : other.providerId.copy();
        this.stockId = other.stockId == null ? null : other.stockId.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
        this.articleDescription = other.articleDescription == null ? null : other.articleDescription.copy();
        this.articleCode = other.articleCode == null ? null : other.articleCode.copy();
        this.distinct = other.distinct;
        this.isActive = other.isActive;
    }

    @Override
    public AlertCriteria copy() {
        return new AlertCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDatetime() {
        return datetime;
    }

    public InstantFilter datetime() {
        if (datetime == null) {
            datetime = new InstantFilter();
        }
        return datetime;
    }

    public void setDatetime(InstantFilter datetime) {
        this.datetime = datetime;
    }

    public AlertTypeFilter getType() {
        return type;
    }

    public AlertTypeFilter type() {
        if (type == null) {
            type = new AlertTypeFilter();
        }
        return type;
    }

    public void setType(AlertTypeFilter type) {
        this.type = type;
    }

    public InstantFilter getRectificationDatetime() {
        return rectificationDatetime;
    }

    public InstantFilter rectificationDatetime() {
        if (rectificationDatetime == null) {
            rectificationDatetime = new InstantFilter();
        }
        return rectificationDatetime;
    }

    public void setRectificationDatetime(InstantFilter rectificationDatetime) {
        this.rectificationDatetime = rectificationDatetime;
    }

    public LongFilter getProviderId() {
        return providerId;
    }

    public LongFilter providerId() {
        if (providerId == null) {
            providerId = new LongFilter();
        }
        return providerId;
    }

    public void setProviderId(LongFilter providerId) {
        this.providerId = providerId;
    }

    public LongFilter getStockId() {
        return stockId;
    }

    public LongFilter stockId() {
        if (stockId == null) {
            stockId = new LongFilter();
        }
        return stockId;
    }

    public void setStockId(LongFilter stockId) {
        this.stockId = stockId;
    }

    public LongFilter getStoreId() {
        return storeId;
    }

    public void setStoreId(LongFilter storeId) {
        this.storeId = storeId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public StringFilter getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(StringFilter articleCode) {
        this.articleCode = articleCode;
    }

    public StringFilter getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(StringFilter articleDescription) {
        this.articleDescription = articleDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlertCriteria that = (AlertCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(datetime, that.datetime) &&
            Objects.equals(type, that.type) &&
            Objects.equals(rectificationDatetime, that.rectificationDatetime) &&
            Objects.equals(providerId, that.providerId) &&
            Objects.equals(stockId, that.stockId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datetime, type, rectificationDatetime, providerId, stockId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (datetime != null ? "datetime=" + datetime + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (rectificationDatetime != null ? "rectificationDatetime=" + rectificationDatetime + ", " : "") +
            (providerId != null ? "providerId=" + providerId + ", " : "") +
            (stockId != null ? "stockId=" + stockId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
