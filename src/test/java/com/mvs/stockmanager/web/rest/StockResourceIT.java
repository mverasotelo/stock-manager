package com.mvs.stockmanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mvs.stockmanager.IntegrationTest;
import com.mvs.stockmanager.domain.Action;
import com.mvs.stockmanager.domain.Alert;
import com.mvs.stockmanager.domain.Article;
import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.domain.Store;
import com.mvs.stockmanager.repository.StockRepository;
import com.mvs.stockmanager.service.criteria.StockCriteria;
import com.mvs.stockmanager.service.dto.StockDTO;
import com.mvs.stockmanager.service.mapper.StockMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockResourceIT {

    private static final Long DEFAULT_ACTUAL_STOCK = 1L;
    private static final Long UPDATED_ACTUAL_STOCK = 2L;
    private static final Long SMALLER_ACTUAL_STOCK = 1L - 1L;

    private static final Long DEFAULT_REORDER_POINT = 1L;
    private static final Long UPDATED_REORDER_POINT = 2L;
    private static final Long SMALLER_REORDER_POINT = 1L - 1L;

    private static final Long DEFAULT_MAX_STOCK = 1L;
    private static final Long UPDATED_MAX_STOCK = 2L;
    private static final Long SMALLER_MAX_STOCK = 1L - 1L;

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_RACK = "AAAAAAAAAA";
    private static final String UPDATED_RACK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockMockMvc;

    private Stock stock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .actualStock(DEFAULT_ACTUAL_STOCK)
            .reorderPoint(DEFAULT_REORDER_POINT)
            .maxStock(DEFAULT_MAX_STOCK)
            .section(DEFAULT_SECTION)
            .level(DEFAULT_LEVEL)
            .rack(DEFAULT_RACK);
        return stock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createUpdatedEntity(EntityManager em) {
        Stock stock = new Stock()
            .actualStock(UPDATED_ACTUAL_STOCK)
            .reorderPoint(UPDATED_REORDER_POINT)
            .maxStock(UPDATED_MAX_STOCK)
            .section(UPDATED_SECTION)
            .level(UPDATED_LEVEL)
            .rack(UPDATED_RACK);
        return stock;
    }

    @BeforeEach
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();
        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);
        restStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getActualStock()).isEqualTo(DEFAULT_ACTUAL_STOCK);
        assertThat(testStock.getReorderPoint()).isEqualTo(DEFAULT_REORDER_POINT);
        assertThat(testStock.getMaxStock()).isEqualTo(DEFAULT_MAX_STOCK);
        assertThat(testStock.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testStock.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testStock.getRack()).isEqualTo(DEFAULT_RACK);
    }

    @Test
    @Transactional
    void createStockWithExistingId() throws Exception {
        // Create the Stock with an existing ID
        stock.setId(1L);
        StockDTO stockDTO = stockMapper.toDto(stock);

        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualStock").value(hasItem(DEFAULT_ACTUAL_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].reorderPoint").value(hasItem(DEFAULT_REORDER_POINT.intValue())))
            .andExpect(jsonPath("$.[*].maxStock").value(hasItem(DEFAULT_MAX_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].rack").value(hasItem(DEFAULT_RACK)));
    }

    @Test
    @Transactional
    void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc
            .perform(get(ENTITY_API_URL_ID, stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.actualStock").value(DEFAULT_ACTUAL_STOCK.intValue()))
            .andExpect(jsonPath("$.reorderPoint").value(DEFAULT_REORDER_POINT.intValue()))
            .andExpect(jsonPath("$.maxStock").value(DEFAULT_MAX_STOCK.intValue()))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.rack").value(DEFAULT_RACK));
    }

    @Test
    @Transactional
    void getStocksByIdFiltering() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        Long id = stock.getId();

        defaultStockShouldBeFound("id.equals=" + id);
        defaultStockShouldNotBeFound("id.notEquals=" + id);

        defaultStockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStockShouldNotBeFound("id.greaterThan=" + id);

        defaultStockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStockShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStocksByActualStockIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where actualStock equals to DEFAULT_ACTUAL_STOCK
        defaultStockShouldBeFound("actualStock.equals=" + DEFAULT_ACTUAL_STOCK);

        // Get all the stockList where actualStock equals to UPDATED_ACTUAL_STOCK
        defaultStockShouldNotBeFound("actualStock.equals=" + UPDATED_ACTUAL_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByActualStockIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where actualStock in DEFAULT_ACTUAL_STOCK or UPDATED_ACTUAL_STOCK
        defaultStockShouldBeFound("actualStock.in=" + DEFAULT_ACTUAL_STOCK + "," + UPDATED_ACTUAL_STOCK);

        // Get all the stockList where actualStock equals to UPDATED_ACTUAL_STOCK
        defaultStockShouldNotBeFound("actualStock.in=" + UPDATED_ACTUAL_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByActualStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where actualStock is not null
        defaultStockShouldBeFound("actualStock.specified=true");

        // Get all the stockList where actualStock is null
        defaultStockShouldNotBeFound("actualStock.specified=false");
    }

    @Test
    @Transactional
    void getAllStocksByActualStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where actualStock is greater than or equal to DEFAULT_ACTUAL_STOCK
        defaultStockShouldBeFound("actualStock.greaterThanOrEqual=" + DEFAULT_ACTUAL_STOCK);

        // Get all the stockList where actualStock is greater than or equal to UPDATED_ACTUAL_STOCK
        defaultStockShouldNotBeFound("actualStock.greaterThanOrEqual=" + UPDATED_ACTUAL_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByActualStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where actualStock is less than or equal to DEFAULT_ACTUAL_STOCK
        defaultStockShouldBeFound("actualStock.lessThanOrEqual=" + DEFAULT_ACTUAL_STOCK);

        // Get all the stockList where actualStock is less than or equal to SMALLER_ACTUAL_STOCK
        defaultStockShouldNotBeFound("actualStock.lessThanOrEqual=" + SMALLER_ACTUAL_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByActualStockIsLessThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where actualStock is less than DEFAULT_ACTUAL_STOCK
        defaultStockShouldNotBeFound("actualStock.lessThan=" + DEFAULT_ACTUAL_STOCK);

        // Get all the stockList where actualStock is less than UPDATED_ACTUAL_STOCK
        defaultStockShouldBeFound("actualStock.lessThan=" + UPDATED_ACTUAL_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByActualStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where actualStock is greater than DEFAULT_ACTUAL_STOCK
        defaultStockShouldNotBeFound("actualStock.greaterThan=" + DEFAULT_ACTUAL_STOCK);

        // Get all the stockList where actualStock is greater than SMALLER_ACTUAL_STOCK
        defaultStockShouldBeFound("actualStock.greaterThan=" + SMALLER_ACTUAL_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByReorderPointIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where reorderPoint equals to DEFAULT_REORDER_POINT
        defaultStockShouldBeFound("reorderPoint.equals=" + DEFAULT_REORDER_POINT);

        // Get all the stockList where reorderPoint equals to UPDATED_REORDER_POINT
        defaultStockShouldNotBeFound("reorderPoint.equals=" + UPDATED_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllStocksByReorderPointIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where reorderPoint in DEFAULT_REORDER_POINT or UPDATED_REORDER_POINT
        defaultStockShouldBeFound("reorderPoint.in=" + DEFAULT_REORDER_POINT + "," + UPDATED_REORDER_POINT);

        // Get all the stockList where reorderPoint equals to UPDATED_REORDER_POINT
        defaultStockShouldNotBeFound("reorderPoint.in=" + UPDATED_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllStocksByReorderPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where reorderPoint is not null
        defaultStockShouldBeFound("reorderPoint.specified=true");

        // Get all the stockList where reorderPoint is null
        defaultStockShouldNotBeFound("reorderPoint.specified=false");
    }

    @Test
    @Transactional
    void getAllStocksByReorderPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where reorderPoint is greater than or equal to DEFAULT_REORDER_POINT
        defaultStockShouldBeFound("reorderPoint.greaterThanOrEqual=" + DEFAULT_REORDER_POINT);

        // Get all the stockList where reorderPoint is greater than or equal to UPDATED_REORDER_POINT
        defaultStockShouldNotBeFound("reorderPoint.greaterThanOrEqual=" + UPDATED_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllStocksByReorderPointIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where reorderPoint is less than or equal to DEFAULT_REORDER_POINT
        defaultStockShouldBeFound("reorderPoint.lessThanOrEqual=" + DEFAULT_REORDER_POINT);

        // Get all the stockList where reorderPoint is less than or equal to SMALLER_REORDER_POINT
        defaultStockShouldNotBeFound("reorderPoint.lessThanOrEqual=" + SMALLER_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllStocksByReorderPointIsLessThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where reorderPoint is less than DEFAULT_REORDER_POINT
        defaultStockShouldNotBeFound("reorderPoint.lessThan=" + DEFAULT_REORDER_POINT);

        // Get all the stockList where reorderPoint is less than UPDATED_REORDER_POINT
        defaultStockShouldBeFound("reorderPoint.lessThan=" + UPDATED_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllStocksByReorderPointIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where reorderPoint is greater than DEFAULT_REORDER_POINT
        defaultStockShouldNotBeFound("reorderPoint.greaterThan=" + DEFAULT_REORDER_POINT);

        // Get all the stockList where reorderPoint is greater than SMALLER_REORDER_POINT
        defaultStockShouldBeFound("reorderPoint.greaterThan=" + SMALLER_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllStocksByMaxStockIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where maxStock equals to DEFAULT_MAX_STOCK
        defaultStockShouldBeFound("maxStock.equals=" + DEFAULT_MAX_STOCK);

        // Get all the stockList where maxStock equals to UPDATED_MAX_STOCK
        defaultStockShouldNotBeFound("maxStock.equals=" + UPDATED_MAX_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByMaxStockIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where maxStock in DEFAULT_MAX_STOCK or UPDATED_MAX_STOCK
        defaultStockShouldBeFound("maxStock.in=" + DEFAULT_MAX_STOCK + "," + UPDATED_MAX_STOCK);

        // Get all the stockList where maxStock equals to UPDATED_MAX_STOCK
        defaultStockShouldNotBeFound("maxStock.in=" + UPDATED_MAX_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByMaxStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where maxStock is not null
        defaultStockShouldBeFound("maxStock.specified=true");

        // Get all the stockList where maxStock is null
        defaultStockShouldNotBeFound("maxStock.specified=false");
    }

    @Test
    @Transactional
    void getAllStocksByMaxStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where maxStock is greater than or equal to DEFAULT_MAX_STOCK
        defaultStockShouldBeFound("maxStock.greaterThanOrEqual=" + DEFAULT_MAX_STOCK);

        // Get all the stockList where maxStock is greater than or equal to UPDATED_MAX_STOCK
        defaultStockShouldNotBeFound("maxStock.greaterThanOrEqual=" + UPDATED_MAX_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByMaxStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where maxStock is less than or equal to DEFAULT_MAX_STOCK
        defaultStockShouldBeFound("maxStock.lessThanOrEqual=" + DEFAULT_MAX_STOCK);

        // Get all the stockList where maxStock is less than or equal to SMALLER_MAX_STOCK
        defaultStockShouldNotBeFound("maxStock.lessThanOrEqual=" + SMALLER_MAX_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByMaxStockIsLessThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where maxStock is less than DEFAULT_MAX_STOCK
        defaultStockShouldNotBeFound("maxStock.lessThan=" + DEFAULT_MAX_STOCK);

        // Get all the stockList where maxStock is less than UPDATED_MAX_STOCK
        defaultStockShouldBeFound("maxStock.lessThan=" + UPDATED_MAX_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksByMaxStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where maxStock is greater than DEFAULT_MAX_STOCK
        defaultStockShouldNotBeFound("maxStock.greaterThan=" + DEFAULT_MAX_STOCK);

        // Get all the stockList where maxStock is greater than SMALLER_MAX_STOCK
        defaultStockShouldBeFound("maxStock.greaterThan=" + SMALLER_MAX_STOCK);
    }

    @Test
    @Transactional
    void getAllStocksBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where section equals to DEFAULT_SECTION
        defaultStockShouldBeFound("section.equals=" + DEFAULT_SECTION);

        // Get all the stockList where section equals to UPDATED_SECTION
        defaultStockShouldNotBeFound("section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllStocksBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where section in DEFAULT_SECTION or UPDATED_SECTION
        defaultStockShouldBeFound("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION);

        // Get all the stockList where section equals to UPDATED_SECTION
        defaultStockShouldNotBeFound("section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllStocksBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where section is not null
        defaultStockShouldBeFound("section.specified=true");

        // Get all the stockList where section is null
        defaultStockShouldNotBeFound("section.specified=false");
    }

    @Test
    @Transactional
    void getAllStocksBySectionContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where section contains DEFAULT_SECTION
        defaultStockShouldBeFound("section.contains=" + DEFAULT_SECTION);

        // Get all the stockList where section contains UPDATED_SECTION
        defaultStockShouldNotBeFound("section.contains=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllStocksBySectionNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where section does not contain DEFAULT_SECTION
        defaultStockShouldNotBeFound("section.doesNotContain=" + DEFAULT_SECTION);

        // Get all the stockList where section does not contain UPDATED_SECTION
        defaultStockShouldBeFound("section.doesNotContain=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllStocksByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where level equals to DEFAULT_LEVEL
        defaultStockShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the stockList where level equals to UPDATED_LEVEL
        defaultStockShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStocksByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultStockShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the stockList where level equals to UPDATED_LEVEL
        defaultStockShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStocksByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where level is not null
        defaultStockShouldBeFound("level.specified=true");

        // Get all the stockList where level is null
        defaultStockShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    void getAllStocksByLevelContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where level contains DEFAULT_LEVEL
        defaultStockShouldBeFound("level.contains=" + DEFAULT_LEVEL);

        // Get all the stockList where level contains UPDATED_LEVEL
        defaultStockShouldNotBeFound("level.contains=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStocksByLevelNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where level does not contain DEFAULT_LEVEL
        defaultStockShouldNotBeFound("level.doesNotContain=" + DEFAULT_LEVEL);

        // Get all the stockList where level does not contain UPDATED_LEVEL
        defaultStockShouldBeFound("level.doesNotContain=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStocksByRackIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where rack equals to DEFAULT_RACK
        defaultStockShouldBeFound("rack.equals=" + DEFAULT_RACK);

        // Get all the stockList where rack equals to UPDATED_RACK
        defaultStockShouldNotBeFound("rack.equals=" + UPDATED_RACK);
    }

    @Test
    @Transactional
    void getAllStocksByRackIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where rack in DEFAULT_RACK or UPDATED_RACK
        defaultStockShouldBeFound("rack.in=" + DEFAULT_RACK + "," + UPDATED_RACK);

        // Get all the stockList where rack equals to UPDATED_RACK
        defaultStockShouldNotBeFound("rack.in=" + UPDATED_RACK);
    }

    @Test
    @Transactional
    void getAllStocksByRackIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where rack is not null
        defaultStockShouldBeFound("rack.specified=true");

        // Get all the stockList where rack is null
        defaultStockShouldNotBeFound("rack.specified=false");
    }

    @Test
    @Transactional
    void getAllStocksByRackContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where rack contains DEFAULT_RACK
        defaultStockShouldBeFound("rack.contains=" + DEFAULT_RACK);

        // Get all the stockList where rack contains UPDATED_RACK
        defaultStockShouldNotBeFound("rack.contains=" + UPDATED_RACK);
    }

    @Test
    @Transactional
    void getAllStocksByRackNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where rack does not contain DEFAULT_RACK
        defaultStockShouldNotBeFound("rack.doesNotContain=" + DEFAULT_RACK);

        // Get all the stockList where rack does not contain UPDATED_RACK
        defaultStockShouldBeFound("rack.doesNotContain=" + UPDATED_RACK);
    }

    @Test
    @Transactional
    void getAllStocksByAlertsIsEqualToSomething() throws Exception {
        Alert alerts;
        if (TestUtil.findAll(em, Alert.class).isEmpty()) {
            stockRepository.saveAndFlush(stock);
            alerts = AlertResourceIT.createEntity(em);
        } else {
            alerts = TestUtil.findAll(em, Alert.class).get(0);
        }
        em.persist(alerts);
        em.flush();
        stock.addAlerts(alerts);
        stockRepository.saveAndFlush(stock);
        Long alertsId = alerts.getId();

        // Get all the stockList where alerts equals to alertsId
        defaultStockShouldBeFound("alertsId.equals=" + alertsId);

        // Get all the stockList where alerts equals to (alertsId + 1)
        defaultStockShouldNotBeFound("alertsId.equals=" + (alertsId + 1));
    }

    @Test
    @Transactional
    void getAllStocksByActionsIsEqualToSomething() throws Exception {
        Action actions;
        if (TestUtil.findAll(em, Action.class).isEmpty()) {
            stockRepository.saveAndFlush(stock);
            actions = ActionResourceIT.createEntity(em);
        } else {
            actions = TestUtil.findAll(em, Action.class).get(0);
        }
        em.persist(actions);
        em.flush();
        stock.addActions(actions);
        stockRepository.saveAndFlush(stock);
        Long actionsId = actions.getId();

        // Get all the stockList where actions equals to actionsId
        defaultStockShouldBeFound("actionsId.equals=" + actionsId);

        // Get all the stockList where actions equals to (actionsId + 1)
        defaultStockShouldNotBeFound("actionsId.equals=" + (actionsId + 1));
    }

    @Test
    @Transactional
    void getAllStocksByArticleIsEqualToSomething() throws Exception {
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            stockRepository.saveAndFlush(stock);
            article = ArticleResourceIT.createEntity(em);
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        em.persist(article);
        em.flush();
        stock.setArticle(article);
        stockRepository.saveAndFlush(stock);
        Long articleId = article.getId();

        // Get all the stockList where article equals to articleId
        defaultStockShouldBeFound("articleId.equals=" + articleId);

        // Get all the stockList where article equals to (articleId + 1)
        defaultStockShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    @Test
    @Transactional
    void getAllStocksByStoreIsEqualToSomething() throws Exception {
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            stockRepository.saveAndFlush(stock);
            store = StoreResourceIT.createEntity(em);
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        em.persist(store);
        em.flush();
        stock.setStore(store);
        stockRepository.saveAndFlush(stock);
        Long storeId = store.getId();

        // Get all the stockList where store equals to storeId
        defaultStockShouldBeFound("storeId.equals=" + storeId);

        // Get all the stockList where store equals to (storeId + 1)
        defaultStockShouldNotBeFound("storeId.equals=" + (storeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStockShouldBeFound(String filter) throws Exception {
        restStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualStock").value(hasItem(DEFAULT_ACTUAL_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].reorderPoint").value(hasItem(DEFAULT_REORDER_POINT.intValue())))
            .andExpect(jsonPath("$.[*].maxStock").value(hasItem(DEFAULT_MAX_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].rack").value(hasItem(DEFAULT_RACK)));

        // Check, that the count call also returns 1
        restStockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStockShouldNotBeFound(String filter) throws Exception {
        restStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .actualStock(UPDATED_ACTUAL_STOCK)
            .reorderPoint(UPDATED_REORDER_POINT)
            .maxStock(UPDATED_MAX_STOCK)
            .section(UPDATED_SECTION)
            .level(UPDATED_LEVEL)
            .rack(UPDATED_RACK);
        StockDTO stockDTO = stockMapper.toDto(updatedStock);

        restStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getActualStock()).isEqualTo(UPDATED_ACTUAL_STOCK);
        assertThat(testStock.getReorderPoint()).isEqualTo(UPDATED_REORDER_POINT);
        assertThat(testStock.getMaxStock()).isEqualTo(UPDATED_MAX_STOCK);
        assertThat(testStock.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testStock.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testStock.getRack()).isEqualTo(UPDATED_RACK);
    }

    @Test
    @Transactional
    void putNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockWithPatch() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock using partial update
        Stock partialUpdatedStock = new Stock();
        partialUpdatedStock.setId(stock.getId());

        partialUpdatedStock.actualStock(UPDATED_ACTUAL_STOCK).reorderPoint(UPDATED_REORDER_POINT);

        restStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStock))
            )
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getActualStock()).isEqualTo(UPDATED_ACTUAL_STOCK);
        assertThat(testStock.getReorderPoint()).isEqualTo(UPDATED_REORDER_POINT);
        assertThat(testStock.getMaxStock()).isEqualTo(DEFAULT_MAX_STOCK);
        assertThat(testStock.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testStock.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testStock.getRack()).isEqualTo(DEFAULT_RACK);
    }

    @Test
    @Transactional
    void fullUpdateStockWithPatch() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock using partial update
        Stock partialUpdatedStock = new Stock();
        partialUpdatedStock.setId(stock.getId());

        partialUpdatedStock
            .actualStock(UPDATED_ACTUAL_STOCK)
            .reorderPoint(UPDATED_REORDER_POINT)
            .maxStock(UPDATED_MAX_STOCK)
            .section(UPDATED_SECTION)
            .level(UPDATED_LEVEL)
            .rack(UPDATED_RACK);

        restStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStock))
            )
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getActualStock()).isEqualTo(UPDATED_ACTUAL_STOCK);
        assertThat(testStock.getReorderPoint()).isEqualTo(UPDATED_REORDER_POINT);
        assertThat(testStock.getMaxStock()).isEqualTo(UPDATED_MAX_STOCK);
        assertThat(testStock.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testStock.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testStock.getRack()).isEqualTo(UPDATED_RACK);
    }

    @Test
    @Transactional
    void patchNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Delete the stock
        restStockMockMvc
            .perform(delete(ENTITY_API_URL_ID, stock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
