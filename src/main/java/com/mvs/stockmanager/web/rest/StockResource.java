package com.mvs.stockmanager.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.repository.StockRepository;
import com.mvs.stockmanager.service.StockQueryService;
import com.mvs.stockmanager.service.StockService;
import com.mvs.stockmanager.service.criteria.StockCriteria;
import com.mvs.stockmanager.service.dto.StockDTO;
import com.mvs.stockmanager.web.rest.errors.BadRequestAlertException;
import com.mvs.stockmanager.web.rest.errors.InvalidArgumentException;

import io.prometheus.client.Predicate;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mvs.stockmanager.domain.Stock}.
 */
@RestController
@RequestMapping("/api")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "stock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockService stockService;

    private final StockRepository stockRepository;

    private final StockQueryService stockQueryService;

    public StockResource(StockService stockService, StockRepository stockRepository, StockQueryService stockQueryService) {
        this.stockService = stockService;
        this.stockRepository = stockRepository;
        this.stockQueryService = stockQueryService;
    }

    /**
     * {@code POST  /stocks} : Create a new stock.
     *
     * @param stockDTO the stockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new stockDTO, or with status {@code 400 (Bad Request)} if
     *         the stock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stocks")
    public ResponseEntity<StockDTO> createStock(@RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stockDTO);
        if (stockDTO.getId() != null) {
            throw new BadRequestAlertException("A new stock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (stockRepository.existsByArticleIdAndStoreId(stockDTO.getArticle().getId(), stockDTO.getStore().getId())) {
            throw new BadRequestAlertException("Ya existe el stock del articulo para el almacén ", ENTITY_NAME, "stockexists", stockDTO.getArticle().getCode(), stockDTO.getStore().getCode());
        }
        // Validations
        if (stockDTO.getMaxStock() <= 0) {
            throw new BadRequestAlertException("Max stock must be greater than 0", ENTITY_NAME, "maxStockValidation");
        }

        if (stockDTO.getReorderPoint() <= 0 || stockDTO.getReorderPoint() >= stockDTO.getMaxStock()) {
            throw new BadRequestAlertException("Reorder point must be greater than 0 and less than max stock",
                    ENTITY_NAME, "reorderPointValidation");
        }

        if (stockDTO.getActualStock() < 0 || stockDTO.getActualStock() > stockDTO.getMaxStock()) {
            throw new BadRequestAlertException("Current stock must be between 0 and max capacity", ENTITY_NAME,
                    "actualStockValidation");
        }

        StockDTO result = stockService.save(stockDTO);
        return ResponseEntity
                .created(new URI("/api/stocks/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /stocks/:id} : Updates an existing stock.
     *
     * @param id       the id of the stockDTO to save.
     * @param stockDTO the stockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated stockDTO,
     *         or with status {@code 400 (Bad Request)} if the stockDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the stockDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stocks/{id}")
    public ResponseEntity<StockDTO> updateStock(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to update Stock : {}, {}", id, stockDTO);
        if (stockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // Validations
        if (stockDTO.getMaxStock() <= 0) {
            throw new BadRequestAlertException("Max stock must be greater than 0",ENTITY_NAME, "maxStockValidation");
        }

        if (stockDTO.getReorderPoint() <= 0 || stockDTO.getReorderPoint() >= stockDTO.getMaxStock()) {
            throw new BadRequestAlertException("Reorder point must be greater than 0 and less than max stock",ENTITY_NAME, "reorderPointValidation");
        }

        if (stockDTO.getActualStock() < 0 || stockDTO.getActualStock() > stockDTO.getMaxStock()) {
            throw new BadRequestAlertException("Current stock must be between 0 and max capacity",ENTITY_NAME, "actualStockValidation");
        }

        StockDTO result = stockService.update(stockDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        stockDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /stocks/:id} : Partial updates given fields of an existing
     * stock, field will ignore if it is null
     *
     * @param id       the id of the stockDTO to save.
     * @param stockDTO the stockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated stockDTO,
     *         or with status {@code 400 (Bad Request)} if the stockDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the stockDTO is not found,
     *         or with status {@code 500 (Internal Server Error)} if the stockDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stocks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockDTO> partialUpdateStock(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to partial update Stock partially : {}, {}", id, stockDTO);
        if (stockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockDTO> result = stockService.partialUpdate(stockDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockDTO.getId().toString()));
    }

    /**
     * {@code GET  /stocks} : get all the stocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of stocks in body.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    @GetMapping("/stocks")
    public ResponseEntity<List<StockDTO>> getAllStocks(
            String criteria,
            @org.springdoc.api.annotations.ParameterObject Pageable pageable)
            throws JsonMappingException, JsonProcessingException {

        // StockCriteria criteria = stockQueryService.convertToCriteriaObject(store,
        // article);
        ObjectMapper objectMapper = new ObjectMapper();
        StockCriteria criteriaObject = objectMapper.readValue(criteria, StockCriteria.class);

        log.debug("REST request to get Stocks by criteria: {} - {}", criteriaObject);

        Page<StockDTO> page = stockQueryService.findByCriteria(criteriaObject, pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stocks/count} : count all the stocks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/stocks/count")
    public ResponseEntity<Long> countStocks(StockCriteria criteria) {
        log.debug("REST request to count Stocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /stocks/:id} : get the "id" stock.
     *
     * @param id the id of the stockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the stockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stocks/{id}")
    public ResponseEntity<StockDTO> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Optional<StockDTO> stockDTO = stockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockDTO);
    }

    /**
     * {@code DELETE  /stocks/:id} : delete the "id" stock.
     *
     * @param id the id of the stockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

}
