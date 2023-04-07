package com.mvs.stockmanager.repository;

import com.mvs.stockmanager.domain.Article;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    Boolean existsByCodeIgnoreCase(String code);

    Boolean existsByCodeAndIdNot(String code, Long id);
}
