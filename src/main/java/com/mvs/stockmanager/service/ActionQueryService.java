package com.mvs.stockmanager.service;

import com.mvs.stockmanager.domain.*; // for static metamodels
import com.mvs.stockmanager.domain.Action;
import com.mvs.stockmanager.repository.ActionRepository;
import com.mvs.stockmanager.service.criteria.ActionCriteria;
import com.mvs.stockmanager.service.dto.ActionDTO;
import com.mvs.stockmanager.service.mapper.ActionMapper;
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
 * Service for executing complex queries for {@link Action} entities in the database.
 * The main input is a {@link ActionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActionDTO} or a {@link Page} of {@link ActionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActionQueryService extends QueryService<Action> {

    private final Logger log = LoggerFactory.getLogger(ActionQueryService.class);

    private final ActionRepository actionRepository;

    private final ActionMapper actionMapper;

    public ActionQueryService(ActionRepository actionRepository, ActionMapper actionMapper) {
        this.actionRepository = actionRepository;
        this.actionMapper = actionMapper;
    }

    /**
     * Return a {@link List} of {@link ActionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActionDTO> findByCriteria(ActionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Action> specification = createSpecification(criteria);
        return actionMapper.toDto(actionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActionDTO> findByCriteria(ActionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Action> specification = createSpecification(criteria);
        return actionRepository.findAll(specification, page).map(actionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Action> specification = createSpecification(criteria);
        return actionRepository.count(specification);
    }

    /**
     * Function to convert {@link ActionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Action> createSpecification(ActionCriteria criteria) {
        Specification<Action> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Action_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Action_.code));
            }
            if (criteria.getDatetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatetime(), Action_.datetime));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Action_.type));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Action_.quantity));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmployeeId(), root -> root.join(Action_.employee, JoinType.LEFT).get(Employee_.id))
                    );
            }
            if (criteria.getStockId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStockId(), root -> root.join(Action_.stock, JoinType.LEFT).get(Stock_.id))
                    );
            }
            if (criteria.getStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStoreId(), root -> root.join(Action_.store, JoinType.LEFT).get(Store_.id))
                    );
            }
            if (criteria.getArticleCode() != null || criteria.getArticleDescription() != null) {
                specification = specification.and(
                    Specification.where(
                        buildSpecification(criteria.getArticleCode(), root -> root.join(Action_.stock, JoinType.LEFT).join(Stock_.article, JoinType.LEFT).get(Article_.code))
                    ).or(
                        buildSpecification(criteria.getArticleDescription(), root -> root.join(Action_.stock, JoinType.LEFT).join(Stock_.article, JoinType.LEFT).get(Article_.description))
                    )
                );
            }        
        }
        return specification;
    }
}
