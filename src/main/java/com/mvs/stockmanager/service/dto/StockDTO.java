package com.mvs.stockmanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/**
 * A DTO for the {@link com.mvs.stockmanager.domain.Stock} entity.
 */
@Schema(description = "Stock entity.\n@author Mercedes Vera Sotelo.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockDTO implements Serializable {

    private Long id;

    private Long actualStock;

    private Long reorderPoint;

    private Long maxStock;

    private String section;

    private String level;

    private String rack;

    private ArticleDTO article;

    private StoreDTO store;

    private Boolean isUnderReorderPoint;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActualStock() {
        return actualStock;
    }

    public void setActualStock(Long actualStock) {
        this.actualStock = actualStock;
    }

    public Long getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Long reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Long getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Long maxStock) {
        this.maxStock = maxStock;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    public static ArticleDTO getArticle(StockDTO stockDTO) {
        return stockDTO.getArticle();
    }

    public static Long getActualStock(StockDTO stockDTO) {
        return stockDTO.getActualStock();
    }

    public Boolean getIsUnderReorderPoint() {
        return actualStock < reorderPoint;
    }

    public void setIsUnderReorderPoint(Boolean isUnderReorderPoint) {
        this.isUnderReorderPoint = isUnderReorderPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockDTO)) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockDTO{" +
            "id=" + getId() +
            ", actualStock=" + getActualStock() +
            ", reorderPoint=" + getReorderPoint() +
            ", maxStock=" + getMaxStock() +
            ", section='" + getSection() + "'" +
            ", level='" + getLevel() + "'" +
            ", rack='" + getRack() + "'" +
            ", article=" + getArticle() +
            ", store=" + getStore() +
            "}";
    }
}
