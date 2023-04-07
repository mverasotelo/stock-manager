package com.mvs.stockmanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mvs.stockmanager.IntegrationTest;
import com.mvs.stockmanager.domain.Alert;
import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.domain.Store;
import com.mvs.stockmanager.domain.enumeration.AlertType;
import com.mvs.stockmanager.repository.AlertRepository;
import com.mvs.stockmanager.service.criteria.AlertCriteria;
import com.mvs.stockmanager.service.dto.AlertDTO;
import com.mvs.stockmanager.service.mapper.AlertMapper;
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
 * Integration tests for the {@link AlertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlertResourceIT {

    private static final Instant DEFAULT_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AlertType DEFAULT_TYPE = AlertType.REORDER_POINT;
    private static final AlertType UPDATED_TYPE = AlertType.STOCKOUT;

    private static final Instant DEFAULT_RECTIFICATION_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECTIFICATION_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/alerts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AlertMapper alertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertMockMvc;

    private Alert alert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createEntity(EntityManager em) {
        Alert alert = new Alert().datetime(DEFAULT_DATETIME).type(DEFAULT_TYPE).rectificationDatetime(DEFAULT_RECTIFICATION_DATETIME);
        return alert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createUpdatedEntity(EntityManager em) {
        Alert alert = new Alert().datetime(UPDATED_DATETIME).type(UPDATED_TYPE).rectificationDatetime(UPDATED_RECTIFICATION_DATETIME);
        return alert;
    }

    @BeforeEach
    public void initTest() {
        alert = createEntity(em);
    }

    @Test
    @Transactional
    void createAlert() throws Exception {
        int databaseSizeBeforeCreate = alertRepository.findAll().size();
        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);
        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isCreated());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeCreate + 1);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getDatetime()).isEqualTo(DEFAULT_DATETIME);
        assertThat(testAlert.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAlert.getRectificationDatetime()).isEqualTo(DEFAULT_RECTIFICATION_DATETIME);
    }

    @Test
    @Transactional
    void createAlertWithExistingId() throws Exception {
        // Create the Alert with an existing ID
        alert.setId(1L);
        AlertDTO alertDTO = alertMapper.toDto(alert);

        int databaseSizeBeforeCreate = alertRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlerts() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alert.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(DEFAULT_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rectificationDatetime").value(hasItem(DEFAULT_RECTIFICATION_DATETIME.toString())));
    }

    @Test
    @Transactional
    void getAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get the alert
        restAlertMockMvc
            .perform(get(ENTITY_API_URL_ID, alert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alert.getId().intValue()))
            .andExpect(jsonPath("$.datetime").value(DEFAULT_DATETIME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.rectificationDatetime").value(DEFAULT_RECTIFICATION_DATETIME.toString()));
    }

    @Test
    @Transactional
    void getAlertsByIdFiltering() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        Long id = alert.getId();

        defaultAlertShouldBeFound("id.equals=" + id);
        defaultAlertShouldNotBeFound("id.notEquals=" + id);

        defaultAlertShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlertShouldNotBeFound("id.greaterThan=" + id);

        defaultAlertShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlertShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlertsByDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where datetime equals to DEFAULT_DATETIME
        defaultAlertShouldBeFound("datetime.equals=" + DEFAULT_DATETIME);

        // Get all the alertList where datetime equals to UPDATED_DATETIME
        defaultAlertShouldNotBeFound("datetime.equals=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllAlertsByDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where datetime in DEFAULT_DATETIME or UPDATED_DATETIME
        defaultAlertShouldBeFound("datetime.in=" + DEFAULT_DATETIME + "," + UPDATED_DATETIME);

        // Get all the alertList where datetime equals to UPDATED_DATETIME
        defaultAlertShouldNotBeFound("datetime.in=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllAlertsByDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where datetime is not null
        defaultAlertShouldBeFound("datetime.specified=true");

        // Get all the alertList where datetime is null
        defaultAlertShouldNotBeFound("datetime.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where type equals to DEFAULT_TYPE
        defaultAlertShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the alertList where type equals to UPDATED_TYPE
        defaultAlertShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAlertsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAlertShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the alertList where type equals to UPDATED_TYPE
        defaultAlertShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAlertsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where type is not null
        defaultAlertShouldBeFound("type.specified=true");

        // Get all the alertList where type is null
        defaultAlertShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByRectificationDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where rectificationDatetime equals to DEFAULT_RECTIFICATION_DATETIME
        defaultAlertShouldBeFound("rectificationDatetime.equals=" + DEFAULT_RECTIFICATION_DATETIME);

        // Get all the alertList where rectificationDatetime equals to UPDATED_RECTIFICATION_DATETIME
        defaultAlertShouldNotBeFound("rectificationDatetime.equals=" + UPDATED_RECTIFICATION_DATETIME);
    }

    @Test
    @Transactional
    void getAllAlertsByRectificationDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where rectificationDatetime in DEFAULT_RECTIFICATION_DATETIME or UPDATED_RECTIFICATION_DATETIME
        defaultAlertShouldBeFound("rectificationDatetime.in=" + DEFAULT_RECTIFICATION_DATETIME + "," + UPDATED_RECTIFICATION_DATETIME);

        // Get all the alertList where rectificationDatetime equals to UPDATED_RECTIFICATION_DATETIME
        defaultAlertShouldNotBeFound("rectificationDatetime.in=" + UPDATED_RECTIFICATION_DATETIME);
    }

    @Test
    @Transactional
    void getAllAlertsByRectificationDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where rectificationDatetime is not null
        defaultAlertShouldBeFound("rectificationDatetime.specified=true");

        // Get all the alertList where rectificationDatetime is null
        defaultAlertShouldNotBeFound("rectificationDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByProviderIsEqualToSomething() throws Exception {
        Store provider;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            alertRepository.saveAndFlush(alert);
            provider = StoreResourceIT.createEntity(em);
        } else {
            provider = TestUtil.findAll(em, Store.class).get(0);
        }
        em.persist(provider);
        em.flush();
        alert.setProvider(provider);
        alertRepository.saveAndFlush(alert);
        Long providerId = provider.getId();

        // Get all the alertList where provider equals to providerId
        defaultAlertShouldBeFound("providerId.equals=" + providerId);

        // Get all the alertList where provider equals to (providerId + 1)
        defaultAlertShouldNotBeFound("providerId.equals=" + (providerId + 1));
    }

    @Test
    @Transactional
    void getAllAlertsByStockIsEqualToSomething() throws Exception {
        Stock stock;
        if (TestUtil.findAll(em, Stock.class).isEmpty()) {
            alertRepository.saveAndFlush(alert);
            stock = StockResourceIT.createEntity(em);
        } else {
            stock = TestUtil.findAll(em, Stock.class).get(0);
        }
        em.persist(stock);
        em.flush();
        alert.setStock(stock);
        alertRepository.saveAndFlush(alert);
        Long stockId = stock.getId();

        // Get all the alertList where stock equals to stockId
        defaultAlertShouldBeFound("stockId.equals=" + stockId);

        // Get all the alertList where stock equals to (stockId + 1)
        defaultAlertShouldNotBeFound("stockId.equals=" + (stockId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlertShouldBeFound(String filter) throws Exception {
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alert.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(DEFAULT_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rectificationDatetime").value(hasItem(DEFAULT_RECTIFICATION_DATETIME.toString())));

        // Check, that the count call also returns 1
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlertShouldNotBeFound(String filter) throws Exception {
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlert() throws Exception {
        // Get the alert
        restAlertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Update the alert
        Alert updatedAlert = alertRepository.findById(alert.getId()).get();
        // Disconnect from session so that the updates on updatedAlert are not directly saved in db
        em.detach(updatedAlert);
        updatedAlert.datetime(UPDATED_DATETIME).type(UPDATED_TYPE).rectificationDatetime(UPDATED_RECTIFICATION_DATETIME);
        AlertDTO alertDTO = alertMapper.toDto(updatedAlert);

        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testAlert.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAlert.getRectificationDatetime()).isEqualTo(UPDATED_RECTIFICATION_DATETIME);
    }

    @Test
    @Transactional
    void putNonExistingAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlertWithPatch() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Update the alert using partial update
        Alert partialUpdatedAlert = new Alert();
        partialUpdatedAlert.setId(alert.getId());

        partialUpdatedAlert.datetime(UPDATED_DATETIME).type(UPDATED_TYPE);

        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlert))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testAlert.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAlert.getRectificationDatetime()).isEqualTo(DEFAULT_RECTIFICATION_DATETIME);
    }

    @Test
    @Transactional
    void fullUpdateAlertWithPatch() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Update the alert using partial update
        Alert partialUpdatedAlert = new Alert();
        partialUpdatedAlert.setId(alert.getId());

        partialUpdatedAlert.datetime(UPDATED_DATETIME).type(UPDATED_TYPE).rectificationDatetime(UPDATED_RECTIFICATION_DATETIME);

        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlert))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testAlert.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAlert.getRectificationDatetime()).isEqualTo(UPDATED_RECTIFICATION_DATETIME);
    }

    @Test
    @Transactional
    void patchNonExistingAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeDelete = alertRepository.findAll().size();

        // Delete the alert
        restAlertMockMvc
            .perform(delete(ENTITY_API_URL_ID, alert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
