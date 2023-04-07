package com.mvs.stockmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mvs.stockmanager.domain.enumeration.ArticleType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Article entity.\n@author Mercedes Vera Sotelo.
 */
@Entity
@Table(name = "article")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ArticleType type;

    @OneToMany(mappedBy = "article")
    @JsonIgnoreProperties(value = { "alerts", "actions", "article", "store" }, allowSetters = true)
    private Set<Stock> stocks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Article id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Article code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public Article description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArticleType getType() {
        return this.type;
    }

    public Article type(ArticleType type) {
        this.setType(type);
        return this;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }

    public Set<Stock> getStocks() {
        return this.stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        if (this.stocks != null) {
            this.stocks.forEach(i -> i.setArticle(null));
        }
        if (stocks != null) {
            stocks.forEach(i -> i.setArticle(this));
        }
        this.stocks = stocks;
    }

    public Article stocks(Set<Stock> stocks) {
        this.setStocks(stocks);
        return this;
    }

    public Article addStocks(Stock stock) {
        this.stocks.add(stock);
        stock.setArticle(this);
        return this;
    }

    public Article removeStocks(Stock stock) {
        this.stocks.remove(stock);
        stock.setArticle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
