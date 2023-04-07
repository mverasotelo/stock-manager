package com.mvs.stockmanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mvs.stockmanager.IntegrationTest;
import com.mvs.stockmanager.domain.Action;
import com.mvs.stockmanager.domain.Employee;
import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.domain.Store;
import com.mvs.stockmanager.domain.enumeration.ActionType;
import com.mvs.stockmanager.repository.ActionRepository;
import com.mvs.stockmanager.service.criteria.ActionCriteria;
import com.mvs.stockmanager.service.dto.ActionDTO;
import com.mvs.stockmanager.service.mapper.ActionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ActionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ActionType DEFAULT_TYPE = ActionType.IN;
    private static final ActionType UPDATED_TYPE = ActionType.OUT;

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;
    private static final Long SMALLER_QUANTITY = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActionMockMvc;

    private Action action;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Action createEntity(EntityManager em) {
        Action action = new Action().code(DEFAULT_CODE).datetime(DEFAULT_DATETIME).type(DEFAULT_TYPE).quantity(DEFAULT_QUANTITY);
        return action;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Action createUpdatedEntity(EntityManager em) {
        Action action = new Action().code(UPDATED_CODE).datetime(UPDATED_DATETIME).type(UPDATED_TYPE).quantity(UPDATED_QUANTITY);
        return action;
    }

    @BeforeEach
    public void initTest() {
        action = createEntity(em);
    }

    @Test
    @Transactional
    void createAction() throws Exception {
        int databaseSizeBeforeCreate = actionRepository.findAll().size();
        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);
        restActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actionDTO)))
            .andExpect(status().isCreated());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeCreate + 1);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAction.getDatetime()).isEqualTo(DEFAULT_DATETIME);
        assertThat(testAction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createActionWithExistingId() throws Exception {
        // Create the Action with an existing ID
        action.setId(1L);
        ActionDTO actionDTO = actionMapper.toDto(action);

        int databaseSizeBeforeCreate = actionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActions() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList
        restActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(action.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(DEFAULT_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())));
    }

    @Test
    @Transactional
    void getAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get the action
        restActionMockMvc
            .perform(get(ENTITY_API_URL_ID, action.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(action.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.datetime").value(DEFAULT_DATETIME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()));
    }

    @Test
    @Transactional
    void getActionsByIdFiltering() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        Long id = action.getId();

        defaultActionShouldBeFound("id.equals=" + id);
        defaultActionShouldNotBeFound("id.notEquals=" + id);

        defaultActionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultActionShouldNotBeFound("id.greaterThan=" + id);

        defaultActionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultActionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllActionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where code equals to DEFAULT_CODE
        defaultActionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the actionList where code equals to UPDATED_CODE
        defaultActionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllActionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultActionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the actionList where code equals to UPDATED_CODE
        defaultActionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllActionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where code is not null
        defaultActionShouldBeFound("code.specified=true");

        // Get all the actionList where code is null
        defaultActionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllActionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where code contains DEFAULT_CODE
        defaultActionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the actionList where code contains UPDATED_CODE
        defaultActionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllActionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where code does not contain DEFAULT_CODE
        defaultActionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the actionList where code does not contain UPDATED_CODE
        defaultActionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllActionsByDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where datetime equals to DEFAULT_DATETIME
        defaultActionShouldBeFound("datetime.equals=" + DEFAULT_DATETIME);

        // Get all the actionList where datetime equals to UPDATED_DATETIME
        defaultActionShouldNotBeFound("datetime.equals=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllActionsByDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where datetime in DEFAULT_DATETIME or UPDATED_DATETIME
        defaultActionShouldBeFound("datetime.in=" + DEFAULT_DATETIME + "," + UPDATED_DATETIME);

        // Get all the actionList where datetime equals to UPDATED_DATETIME
        defaultActionShouldNotBeFound("datetime.in=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllActionsByDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where datetime is not null
        defaultActionShouldBeFound("datetime.specified=true");

        // Get all the actionList where datetime is null
        defaultActionShouldNotBeFound("datetime.specified=false");
    }

    @Test
    @Transactional
    void getAllActionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where type equals to DEFAULT_TYPE
        defaultActionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the actionList where type equals to UPDATED_TYPE
        defaultActionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllActionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultActionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the actionList where type equals to UPDATED_TYPE
        defaultActionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllActionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where type is not null
        defaultActionShouldBeFound("type.specified=true");

        // Get all the actionList where type is null
        defaultActionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllActionsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where quantity equals to DEFAULT_QUANTITY
        defaultActionShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the actionList where quantity equals to UPDATED_QUANTITY
        defaultActionShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllActionsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultActionShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the actionList where quantity equals to UPDATED_QUANTITY
        defaultActionShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllActionsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where quantity is not null
        defaultActionShouldBeFound("quantity.specified=true");

        // Get all the actionList where quantity is null
        defaultActionShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllActionsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultActionShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the actionList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultActionShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllActionsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultActionShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the actionList where quantity is less than or equal to SMALLER_QUANTITY
        defaultActionShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllActionsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where quantity is less than DEFAULT_QUANTITY
        defaultActionShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the actionList where quantity is less than UPDATED_QUANTITY
        defaultActionShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllActionsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList where quantity is greater than DEFAULT_QUANTITY
        defaultActionShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the actionList where quantity is greater than SMALLER_QUANTITY
        defaultActionShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllActionsByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            actionRepository.saveAndFlush(action);
            employee = EmployeeResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        action.setEmployee(employee);
        actionRepository.saveAndFlush(action);
        Long employeeId = employee.getId();

        // Get all the actionList where employee equals to employeeId
        defaultActionShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the actionList where employee equals to (employeeId + 1)
        defaultActionShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllActionsByStockIsEqualToSomething() throws Exception {
        Stock stock;
        if (TestUtil.findAll(em, Stock.class).isEmpty()) {
            actionRepository.saveAndFlush(action);
            stock = StockResourceIT.createEntity(em);
        } else {
            stock = TestUtil.findAll(em, Stock.class).get(0);
        }
        em.persist(stock);
        em.flush();
        action.setStock(stock);
        actionRepository.saveAndFlush(action);
        Long stockId = stock.getId();

        // Get all the actionList where stock equals to stockId
        defaultActionShouldBeFound("stockId.equals=" + stockId);

        // Get all the actionList where stock equals to (stockId + 1)
        defaultActionShouldNotBeFound("stockId.equals=" + (stockId + 1));
    }

    @Test
    @Transactional
    void getAllActionsByStoreIsEqualToSomething() throws Exception {
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            actionRepository.saveAndFlush(action);
            store = StoreResourceIT.createEntity(em);
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        em.persist(store);
        em.flush();
        action.setStore(store);
        actionRepository.saveAndFlush(action);
        Long storeId = store.getId();

        // Get all the actionList where store equals to storeId
        defaultActionShouldBeFound("storeId.equals=" + storeId);

        // Get all the actionList where store equals to (storeId + 1)
        defaultActionShouldNotBeFound("storeId.equals=" + (storeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActionShouldBeFound(String filter) throws Exception {
        restActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(action.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(DEFAULT_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())));

        // Check, that the count call also returns 1
        restActionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActionShouldNotBeFound(String filter) throws Exception {
        restActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAction() throws Exception {
        // Get the action
        restActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action
        Action updatedAction = actionRepository.findById(action.getId()).get();
        // Disconnect from session so that the updates on updatedAction are not directly saved in db
        em.detach(updatedAction);
        updatedAction.code(UPDATED_CODE).datetime(UPDATED_DATETIME).type(UPDATED_TYPE).quantity(UPDATED_QUANTITY);
        ActionDTO actionDTO = actionMapper.toDto(updatedAction);

        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAction.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActionWithPatch() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action using partial update
        Action partialUpdatedAction = new Action();
        partialUpdatedAction.setId(action.getId());

        partialUpdatedAction.code(UPDATED_CODE).type(UPDATED_TYPE);

        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAction))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAction.getDatetime()).isEqualTo(DEFAULT_DATETIME);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateActionWithPatch() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action using partial update
        Action partialUpdatedAction = new Action();
        partialUpdatedAction.setId(action.getId());

        partialUpdatedAction.code(UPDATED_CODE).datetime(UPDATED_DATETIME).type(UPDATED_TYPE).quantity(UPDATED_QUANTITY);

        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAction))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAction.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeDelete = actionRepository.findAll().size();

        // Delete the action
        restActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, action.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
