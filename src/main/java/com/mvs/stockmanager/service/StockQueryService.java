package com.mvs.stockmanager.service;

import com.mvs.stockmanager.domain.*; // for static metamodels
import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.repository.StockRepository;
import com.mvs.stockmanager.service.criteria.StockCriteria;
import com.mvs.stockmanager.service.dto.StockDTO;
import com.mvs.stockmanager.service.mapper.StockMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service for executing complex queries for {@link Stock} entities in the database.
 * The main input is a {@link StockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockDTO} or a {@link Page} of {@link StockDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockQueryService extends QueryService<Stock> {

    private final Logger log = LoggerFactory.getLogger(StockQueryService.class);

    private final StockRepository stockRepository;

    private final StockMapper stockMapper;

    public StockQueryService(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    /**
     * Return a {@link List} of {@link StockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockDTO> findByCriteria(StockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockMapper.toDto(stockRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockDTO> findByCriteria(StockCriteria criteria, Pageable page) {

        System.out.println("CRITERIA final store:::" + criteria.getStoreId());

        System.out.println("CRITERIA final art::" + criteria.getArticleDescription());
        System.out.println("CRITERIA final art::" + criteria);

        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.findAll(specification, page).map(stockMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.count(specification);
    }

    /**
     * Function to convert {@link StockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Stock> createSpecification(StockCriteria criteria) {

        System.out.println("CRITERIA final store:::" + criteria.getStoreId());

        System.out.println("CRITERIA final art::" + criteria.getArticleDescription());
        System.out.println("CRITERIA final art::" + criteria);

        Specification<Stock> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            // if (criteria.getDistinct() != null) {
            //     specification = specification.and(distinct(criteria.getDistinct()));
            // }
            // if (criteria.getSection() != null) {
            //     specification = specification.and(buildStringSpecification(criteria.getSection(), Stock_.section));
            // }
            // if (criteria.getLevel() != null) {
            //     specification = specification.and(buildStringSpecification(criteria.getLevel(), Stock_.level));
            // }
            // if (criteria.getRack() != null) {
            //     specification = specification.and(buildStringSpecification(criteria.getRack(), Stock_.rack));
            // }
            if (criteria.getStoreId() != null) {

                specification =
                    specification.or(
                        buildSpecification(criteria.getStoreId(), root -> root.join(Stock_.store, JoinType.LEFT).get(Store_.id))
                    );
                    System.out.println("CRITERIA final store:::" + criteria.getStoreId());

            }
            if (criteria.getArticleCode() != null || criteria.getArticleDescription() != null) {
                specification = specification.and(
                    Specification.where(
                        buildSpecification(criteria.getArticleCode(), root -> root.join(Stock_.article, JoinType.LEFT).get(Article_.code))
                    ).or(
                        buildSpecification(criteria.getArticleDescription(), root -> root.join(Stock_.article, JoinType.LEFT).get(Article_.description))
                    )
                );
            }
            // if (criteria.getArticleCode() != null ) {
            //     specification =
            //         specification.and(
            //             buildSpecification(criteria.getArticleCode(), root -> root.join(Stock_.article, JoinType.LEFT).get(Article_.code))
            //         );
            // }
            // if (criteria.getArticleDescription() != null) {
            //     System.out.println("CRITERIA final art::" + criteria.getArticleDescription());

            //     specification =
            //         specification.and(
            //             buildSpecification(criteria.getArticleDescription(), root -> root.join(Stock_.article, JoinType.LEFT).get(Article_.description))
            //         );
            // }
        }
        return specification;
    }

    /**
     * Function to convert a {@link StockResource}
     * 
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the StockCriteria object.
     */
    public StockCriteria convertToCriteriaObject(Long store, String article) {
        StockCriteria criteria = new StockCriteria();
        if(article != null && !article.isEmpty()){
            StringFilter articleCodeFilter = new StringFilter();
            articleCodeFilter.setContains(article);
            criteria.setArticleCode(articleCodeFilter);
    
            StringFilter articleDescriptionFilter = new StringFilter();
            articleDescriptionFilter.setContains(article);
            criteria.setArticleDescription(articleDescriptionFilter);
        }
        if(store!=null){
            LongFilter storeFilter = new LongFilter();
            storeFilter.setEquals(store);
            criteria.setStoreId(storeFilter);    
        }
        return criteria;
    }
}
