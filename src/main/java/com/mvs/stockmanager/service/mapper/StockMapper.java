package com.mvs.stockmanager.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.service.dto.StockDTO;

/**
 * Mapper for the entity {@link Stock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockMapper extends EntityMapper<StockDTO, Stock> {
    @Mapping(target = "article", source = "article")
    @Mapping(target = "store", source = "store")
    StockDTO toDto(Stock s);

    @Mapping(target = "article", source = "article")
    @Mapping(target = "store", source = "store")
    Stock toEntity(StockDTO s);

    // @Named("articleId")
    // @BeanMapping(ignoreByDefault = true)
    // @Mapping(target = "id", source = "id")
    // ArticleDTO toDtoArticleId(Article article);

    // @Named("storeId")
    // @BeanMapping(ignoreByDefault = true)
    // @Mapping(target = "id", source = "id")
    // StoreDTO toDtoStoreId(Store store);
}
