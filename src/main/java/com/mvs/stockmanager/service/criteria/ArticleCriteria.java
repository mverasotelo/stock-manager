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

    private StringFilter articleCode;

    private StringFilter articleDescription;

    public ArticleCriteria() {}

    public ArticleCriteria(ArticleCriteria other) {
        this.articleCode = other.articleCode == null ? null : other.articleCode;
        this.articleDescription = other.articleDescription == null ? null : other.articleDescription;
    }

    @Override
    public ArticleCriteria copy() {
        return new ArticleCriteria(this);
    }

    public StringFilter getArticleCode() {
        return articleCode;
    }

    public StringFilter articleCode() {
        return articleCode;
    }

    public void setArticleCode(StringFilter articleCode) {
        this.articleCode = articleCode;
    }

    public StringFilter getArticleDescription() {
        return articleDescription;
    }

    public StringFilter articleDescription() {
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
        final ArticleCriteria that = (ArticleCriteria) o;
        return (
            Objects.equals(articleCode, that.articleCode) &&
            Objects.equals(articleDescription, that.articleDescription) 
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleCode, articleDescription);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleCriteria{" +
            (articleCode != null ? "code=" + articleCode + ", " : "") +
            (articleDescription != null ? "description=" + articleDescription + ", " : "") +
            "}";
    }
}
