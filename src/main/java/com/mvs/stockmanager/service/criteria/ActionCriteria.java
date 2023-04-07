package com.mvs.stockmanager.service.criteria;

import com.mvs.stockmanager.domain.enumeration.ActionType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mvs.stockmanager.domain.Action} entity. This class is used
 * in {@link com.mvs.stockmanager.web.rest.ActionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /actions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ActionType
     */
    public static class ActionTypeFilter extends Filter<ActionType> {

        public ActionTypeFilter() {}

        public ActionTypeFilter(ActionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ActionTypeFilter copy() {
            return new ActionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private InstantFilter datetime;

    private ActionTypeFilter type;

    private LongFilter quantity;

    private LongFilter employeeId;

    private LongFilter stockId;

    private LongFilter storeId;

    private StringFilter articleCode;

    private StringFilter articleDescription;

    private Boolean distinct;

    public ActionCriteria() {}

    public ActionCriteria(ActionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.datetime = other.datetime == null ? null : other.datetime.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.stockId = other.stockId == null ? null : other.stockId.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
        this.articleDescription = other.articleDescription == null ? null : other.articleDescription.copy();
        this.articleCode = other.articleCode == null ? null : other.articleCode.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ActionCriteria copy() {
        return new ActionCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public ActionTypeFilter getType() {
        return type;
    }

    public ActionTypeFilter type() {
        if (type == null) {
            type = new ActionTypeFilter();
        }
        return type;
    }

    public void setType(ActionTypeFilter type) {
        this.type = type;
    }

    public LongFilter getQuantity() {
        return quantity;
    }

    public LongFilter quantity() {
        if (quantity == null) {
            quantity = new LongFilter();
        }
        return quantity;
    }

    public void setQuantity(LongFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            employeeId = new LongFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
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

    public LongFilter storeId() {
        if (storeId == null) {
            storeId = new LongFilter();
        }
        return storeId;
    }

    public void setStoreId(LongFilter storeId) {
        this.storeId = storeId;
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

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ActionCriteria that = (ActionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(datetime, that.datetime) &&
            Objects.equals(type, that.type) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(stockId, that.stockId) &&
            Objects.equals(storeId, that.storeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, datetime, type, quantity, employeeId, stockId, storeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (datetime != null ? "datetime=" + datetime + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (stockId != null ? "stockId=" + stockId + ", " : "") +
            (storeId != null ? "storeId=" + storeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
