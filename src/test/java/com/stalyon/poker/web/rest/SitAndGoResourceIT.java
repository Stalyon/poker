package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.SitAndGo;
import com.stalyon.poker.repository.SitAndGoRepository;
import com.stalyon.poker.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.stalyon.poker.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.stalyon.poker.domain.enumeration.SitAndGoFormat;
/**
 * Integration tests for the {@link SitAndGoResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class SitAndGoResourceIT {

    private static final SitAndGoFormat DEFAULT_FORMAT = SitAndGoFormat.EXPRESSO;
    private static final SitAndGoFormat UPDATED_FORMAT = SitAndGoFormat.SNG;

    private static final Double DEFAULT_BUY_IN = 1D;
    private static final Double UPDATED_BUY_IN = 2D;

    private static final Integer DEFAULT_RANKING = 1;
    private static final Integer UPDATED_RANKING = 2;

    private static final Double DEFAULT_PROFIT = 1D;
    private static final Double UPDATED_PROFIT = 2D;

    private static final Double DEFAULT_BOUNTY = 1D;
    private static final Double UPDATED_BOUNTY = 2D;

    @Autowired
    private SitAndGoRepository sitAndGoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSitAndGoMockMvc;

    private SitAndGo sitAndGo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SitAndGoResource sitAndGoResource = new SitAndGoResource(sitAndGoRepository);
        this.restSitAndGoMockMvc = MockMvcBuilders.standaloneSetup(sitAndGoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SitAndGo createEntity(EntityManager em) {
        SitAndGo sitAndGo = new SitAndGo()
            .format(DEFAULT_FORMAT)
            .buyIn(DEFAULT_BUY_IN)
            .ranking(DEFAULT_RANKING)
            .profit(DEFAULT_PROFIT)
            .bounty(DEFAULT_BOUNTY);
        return sitAndGo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SitAndGo createUpdatedEntity(EntityManager em) {
        SitAndGo sitAndGo = new SitAndGo()
            .format(UPDATED_FORMAT)
            .buyIn(UPDATED_BUY_IN)
            .ranking(UPDATED_RANKING)
            .profit(UPDATED_PROFIT)
            .bounty(UPDATED_BOUNTY);
        return sitAndGo;
    }

    @BeforeEach
    public void initTest() {
        sitAndGo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSitAndGo() throws Exception {
        int databaseSizeBeforeCreate = sitAndGoRepository.findAll().size();

        // Create the SitAndGo
        restSitAndGoMockMvc.perform(post("/api/sit-and-gos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sitAndGo)))
            .andExpect(status().isCreated());

        // Validate the SitAndGo in the database
        List<SitAndGo> sitAndGoList = sitAndGoRepository.findAll();
        assertThat(sitAndGoList).hasSize(databaseSizeBeforeCreate + 1);
        SitAndGo testSitAndGo = sitAndGoList.get(sitAndGoList.size() - 1);
        assertThat(testSitAndGo.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testSitAndGo.getBuyIn()).isEqualTo(DEFAULT_BUY_IN);
        assertThat(testSitAndGo.getRanking()).isEqualTo(DEFAULT_RANKING);
        assertThat(testSitAndGo.getProfit()).isEqualTo(DEFAULT_PROFIT);
        assertThat(testSitAndGo.getBounty()).isEqualTo(DEFAULT_BOUNTY);
    }

    @Test
    @Transactional
    public void createSitAndGoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sitAndGoRepository.findAll().size();

        // Create the SitAndGo with an existing ID
        sitAndGo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSitAndGoMockMvc.perform(post("/api/sit-and-gos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sitAndGo)))
            .andExpect(status().isBadRequest());

        // Validate the SitAndGo in the database
        List<SitAndGo> sitAndGoList = sitAndGoRepository.findAll();
        assertThat(sitAndGoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSitAndGos() throws Exception {
        // Initialize the database
        sitAndGoRepository.saveAndFlush(sitAndGo);

        // Get all the sitAndGoList
        restSitAndGoMockMvc.perform(get("/api/sit-and-gos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sitAndGo.getId().intValue())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].buyIn").value(hasItem(DEFAULT_BUY_IN.doubleValue())))
            .andExpect(jsonPath("$.[*].ranking").value(hasItem(DEFAULT_RANKING)))
            .andExpect(jsonPath("$.[*].profit").value(hasItem(DEFAULT_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].bounty").value(hasItem(DEFAULT_BOUNTY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSitAndGo() throws Exception {
        // Initialize the database
        sitAndGoRepository.saveAndFlush(sitAndGo);

        // Get the sitAndGo
        restSitAndGoMockMvc.perform(get("/api/sit-and-gos/{id}", sitAndGo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sitAndGo.getId().intValue()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.buyIn").value(DEFAULT_BUY_IN.doubleValue()))
            .andExpect(jsonPath("$.ranking").value(DEFAULT_RANKING))
            .andExpect(jsonPath("$.profit").value(DEFAULT_PROFIT.doubleValue()))
            .andExpect(jsonPath("$.bounty").value(DEFAULT_BOUNTY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSitAndGo() throws Exception {
        // Get the sitAndGo
        restSitAndGoMockMvc.perform(get("/api/sit-and-gos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSitAndGo() throws Exception {
        // Initialize the database
        sitAndGoRepository.saveAndFlush(sitAndGo);

        int databaseSizeBeforeUpdate = sitAndGoRepository.findAll().size();

        // Update the sitAndGo
        SitAndGo updatedSitAndGo = sitAndGoRepository.findById(sitAndGo.getId()).get();
        // Disconnect from session so that the updates on updatedSitAndGo are not directly saved in db
        em.detach(updatedSitAndGo);
        updatedSitAndGo
            .format(UPDATED_FORMAT)
            .buyIn(UPDATED_BUY_IN)
            .ranking(UPDATED_RANKING)
            .profit(UPDATED_PROFIT)
            .bounty(UPDATED_BOUNTY);

        restSitAndGoMockMvc.perform(put("/api/sit-and-gos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSitAndGo)))
            .andExpect(status().isOk());

        // Validate the SitAndGo in the database
        List<SitAndGo> sitAndGoList = sitAndGoRepository.findAll();
        assertThat(sitAndGoList).hasSize(databaseSizeBeforeUpdate);
        SitAndGo testSitAndGo = sitAndGoList.get(sitAndGoList.size() - 1);
        assertThat(testSitAndGo.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testSitAndGo.getBuyIn()).isEqualTo(UPDATED_BUY_IN);
        assertThat(testSitAndGo.getRanking()).isEqualTo(UPDATED_RANKING);
        assertThat(testSitAndGo.getProfit()).isEqualTo(UPDATED_PROFIT);
        assertThat(testSitAndGo.getBounty()).isEqualTo(UPDATED_BOUNTY);
    }

    @Test
    @Transactional
    public void updateNonExistingSitAndGo() throws Exception {
        int databaseSizeBeforeUpdate = sitAndGoRepository.findAll().size();

        // Create the SitAndGo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSitAndGoMockMvc.perform(put("/api/sit-and-gos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sitAndGo)))
            .andExpect(status().isBadRequest());

        // Validate the SitAndGo in the database
        List<SitAndGo> sitAndGoList = sitAndGoRepository.findAll();
        assertThat(sitAndGoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSitAndGo() throws Exception {
        // Initialize the database
        sitAndGoRepository.saveAndFlush(sitAndGo);

        int databaseSizeBeforeDelete = sitAndGoRepository.findAll().size();

        // Delete the sitAndGo
        restSitAndGoMockMvc.perform(delete("/api/sit-and-gos/{id}", sitAndGo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SitAndGo> sitAndGoList = sitAndGoRepository.findAll();
        assertThat(sitAndGoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
