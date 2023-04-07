package com.mvs.stockmanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mvs.stockmanager.domain.Article} entity. This class is used
 * in {@link com.mvs.stockmanager.web.rest.ArticleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /articles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleCriteria implements Serializable, Criteria {


    private static final long serialVersionUID = 1L;

    private String code;

    private String description;

    public ArticleCriteria() {}

    public ArticleCriteria(ArticleCriteria other) {
        this.code = other.code == null ? null : other.code;
        this.description = other.description == null ? null : other.description;
    }

    @Override
    public ArticleCriteria copy() {
        return new ArticleCriteria(this);
    }

    public String getCode() {
        return code;
    }

    public String code() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArticleCriteria that = (ArticleCriteria) o;
        return (
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) 
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleCriteria{" +
            (code != null ? "code=" + code + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            "}";
    }
}
