package com.mvs.stockmanager.service;

import com.mvs.stockmanager.domain.*; // for static metamodels
import com.mvs.stockmanager.domain.Alert;
import com.mvs.stockmanager.repository.AlertRepository;
import com.mvs.stockmanager.service.criteria.AlertCriteria;
import com.mvs.stockmanager.service.dto.AlertDTO;
import com.mvs.stockmanager.service.mapper.AlertMapper;
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

/**
 * Service for executing complex queries for {@link Alert} entities in the database.
 * The main input is a {@link AlertCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AlertDTO} or a {@link Page} of {@link AlertDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlertQueryService extends QueryService<Alert> {

    private final Logger log = LoggerFactory.getLogger(AlertQueryService.class);

    private final AlertRepository alertRepository;

    private final AlertMapper alertMapper;

    public AlertQueryService(AlertRepository alertRepository, AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    /**
     * Return a {@link List} of {@link AlertDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AlertDTO> findByCriteria(AlertCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Alert> specification = createSpecification(criteria);
        return alertMapper.toDto(alertRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AlertDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlertDTO> findByCriteria(AlertCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Alert> specification = createSpecification(criteria);
        return alertRepository.findAll(specification, page).map(alertMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlertCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Alert> specification = createSpecification(criteria);
        return alertRepository.count(specification);
    }

    /**
     * Function to convert {@link AlertCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Alert> createSpecification(AlertCriteria criteria) {
        Specification<Alert> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Alert_.id));
            }
            if (criteria.getDatetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatetime(), Alert_.datetime));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Alert_.type));
            }
            if (criteria.getProviderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProviderId(), root -> root.join(Alert_.provider, JoinType.LEFT).get(Store_.id))
                    );
            }
            if (criteria.getStockId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStockId(), root -> root.join(Alert_.stock, JoinType.LEFT).get(Stock_.id))
                    );
            }
            if (criteria.getStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStoreId(), root -> root.join(Alert_.stock, JoinType.LEFT).join(Stock_.store, JoinType.LEFT).get(Store_.id))
                    );
            }
            if (criteria.getArticleCode() != null || criteria.getArticleDescription() != null) {
                specification = specification.and(
                    Specification.where(
                        buildSpecification(criteria.getArticleCode(), root -> root.join(Alert_.stock, JoinType.LEFT).join(Stock_.article, JoinType.LEFT).get(Article_.code))
                    ).or(
                        buildSpecification(criteria.getArticleDescription(), root -> root.join(Alert_.stock, JoinType.LEFT).join(Stock_.article, JoinType.LEFT).get(Article_.description))
                    )
                );
            }
            if (criteria.getIsActive() != null) {
                specification =
                    specification.and(
                        (root, query, criteriaBuilder) -> {
                            if (criteria.getIsActive()) {
                                return criteriaBuilder.isNull(root.get(Alert_.rectificationDatetime));
                            } else {
                                return criteriaBuilder.isNotNull(root.get(Alert_.rectificationDatetime));
                            }
                        }
                        // buildSpecification(criteria.getStoreId(), root -> root.join(Alert_.stock, JoinType.LEFT).join(Stock_.store, JoinType.LEFT).get(Store_.id))
                    );
            }
        }
        return specification;
    }       
}
