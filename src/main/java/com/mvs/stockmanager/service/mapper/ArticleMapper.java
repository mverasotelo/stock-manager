package com.mvs.stockmanager.service.mapper;

import com.mvs.stockmanager.domain.Article;
import com.mvs.stockmanager.service.dto.ArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {}
