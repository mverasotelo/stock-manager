package com.mvs.stockmanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mvs.stockmanager.domain.Stock} entity. This class is used
 * in {@link com.mvs.stockmanager.web.rest.StockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter section;

    private StringFilter level;

    private StringFilter rack;

    private LongFilter storeId;

    private LongFilter articleId;

    private StringFilter articleDescription;

    private StringFilter articleCode;

    private Boolean distinct;

    public StockCriteria() {}

    public StockCriteria(StockCriteria other) {
        this.section = other.section == null ? null : other.section.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.rack = other.rack == null ? null : other.rack.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
        this.articleDescription = other.articleDescription == null ? null : other.articleDescription.copy();
        this.articleCode = other.articleCode == null ? null : other.articleCode.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StockCriteria copy() {
        return new StockCriteria(this);
    }

    public StringFilter getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(StringFilter articleDescription) {
        this.articleDescription = articleDescription;
    }

    public StringFilter getArticleCode() {
        return articleCode;
    }

    public StringFilter articleCode() {
        if (articleCode == null) {
            articleCode = new StringFilter();
        }
        return articleCode;
    }

    public StringFilter articleDescription() {
        if (articleDescription == null) {
            articleDescription = new StringFilter();
        }
        return articleDescription;
    }


    public void setArticleCode(StringFilter articleCode) {
        this.articleCode = articleCode;
    }
    
    public StringFilter getSection() {
        return section;
    }

    public StringFilter section() {
        if (section == null) {
            section = new StringFilter();
        }
        return section;
    }

    public void setSection(StringFilter section) {
        this.section = section;
    }

    public StringFilter getLevel() {
        return level;
    }

    public StringFilter level() {
        if (level == null) {
            level = new StringFilter();
        }
        return level;
    }

    public void setLevel(StringFilter level) {
        this.level = level;
    }

    public StringFilter getRack() {
        return rack;
    }

    public StringFilter rack() {
        if (rack == null) {
            rack = new StringFilter();
        }
        return rack;
    }

    public void setRack(StringFilter rack) {
        this.rack = rack;
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

    public void setArticlelId(LongFilter articleId) {
        this.articleId = articleId;
    }

    public LongFilter getArticleId() {
        return articleId;
    }

    public LongFilter articleId() {
        if (articleId == null) {
            articleId = new LongFilter();
        }
        return articleId;
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
        final StockCriteria that = (StockCriteria) o;
        return (
            Objects.equals(section, that.section) &&
            Objects.equals(level, that.level) &&
            Objects.equals(rack, that.rack) &&
            Objects.equals(storeId, that.storeId) &&
            Objects.equals(articleDescription, that.articleDescription) &&
            Objects.equals(articleCode, that.articleCode) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            section,
            level,
            rack,
            storeId,
            distinct,
            articleDescription, 
            articleCode 
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockCriteria{" +
            (section != null ? "section=" + section + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (rack != null ? "rack=" + rack + ", " : "") +
            (storeId != null ? "storeId=" + storeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
