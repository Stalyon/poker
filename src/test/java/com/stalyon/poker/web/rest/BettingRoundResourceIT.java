package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.BettingRound;
import com.stalyon.poker.repository.BettingRoundRepository;
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

/**
 * Integration tests for the {@link BettingRoundResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class BettingRoundResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BettingRoundRepository bettingRoundRepository;

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

    private MockMvc restBettingRoundMockMvc;

    private BettingRound bettingRound;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BettingRoundResource bettingRoundResource = new BettingRoundResource(bettingRoundRepository);
        this.restBettingRoundMockMvc = MockMvcBuilders.standaloneSetup(bettingRoundResource)
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
    public static BettingRound createEntity(EntityManager em) {
        BettingRound bettingRound = new BettingRound()
            .description(DEFAULT_DESCRIPTION);
        return bettingRound;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BettingRound createUpdatedEntity(EntityManager em) {
        BettingRound bettingRound = new BettingRound()
            .description(UPDATED_DESCRIPTION);
        return bettingRound;
    }

    @BeforeEach
    public void initTest() {
        bettingRound = createEntity(em);
    }

    @Test
    @Transactional
    public void createBettingRound() throws Exception {
        int databaseSizeBeforeCreate = bettingRoundRepository.findAll().size();

        // Create the BettingRound
        restBettingRoundMockMvc.perform(post("/api/betting-rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bettingRound)))
            .andExpect(status().isCreated());

        // Validate the BettingRound in the database
        List<BettingRound> bettingRoundList = bettingRoundRepository.findAll();
        assertThat(bettingRoundList).hasSize(databaseSizeBeforeCreate + 1);
        BettingRound testBettingRound = bettingRoundList.get(bettingRoundList.size() - 1);
        assertThat(testBettingRound.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createBettingRoundWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bettingRoundRepository.findAll().size();

        // Create the BettingRound with an existing ID
        bettingRound.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBettingRoundMockMvc.perform(post("/api/betting-rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bettingRound)))
            .andExpect(status().isBadRequest());

        // Validate the BettingRound in the database
        List<BettingRound> bettingRoundList = bettingRoundRepository.findAll();
        assertThat(bettingRoundList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBettingRounds() throws Exception {
        // Initialize the database
        bettingRoundRepository.saveAndFlush(bettingRound);

        // Get all the bettingRoundList
        restBettingRoundMockMvc.perform(get("/api/betting-rounds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bettingRound.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getBettingRound() throws Exception {
        // Initialize the database
        bettingRoundRepository.saveAndFlush(bettingRound);

        // Get the bettingRound
        restBettingRoundMockMvc.perform(get("/api/betting-rounds/{id}", bettingRound.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bettingRound.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingBettingRound() throws Exception {
        // Get the bettingRound
        restBettingRoundMockMvc.perform(get("/api/betting-rounds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBettingRound() throws Exception {
        // Initialize the database
        bettingRoundRepository.saveAndFlush(bettingRound);

        int databaseSizeBeforeUpdate = bettingRoundRepository.findAll().size();

        // Update the bettingRound
        BettingRound updatedBettingRound = bettingRoundRepository.findById(bettingRound.getId()).get();
        // Disconnect from session so that the updates on updatedBettingRound are not directly saved in db
        em.detach(updatedBettingRound);
        updatedBettingRound
            .description(UPDATED_DESCRIPTION);

        restBettingRoundMockMvc.perform(put("/api/betting-rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBettingRound)))
            .andExpect(status().isOk());

        // Validate the BettingRound in the database
        List<BettingRound> bettingRoundList = bettingRoundRepository.findAll();
        assertThat(bettingRoundList).hasSize(databaseSizeBeforeUpdate);
        BettingRound testBettingRound = bettingRoundList.get(bettingRoundList.size() - 1);
        assertThat(testBettingRound.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingBettingRound() throws Exception {
        int databaseSizeBeforeUpdate = bettingRoundRepository.findAll().size();

        // Create the BettingRound

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBettingRoundMockMvc.perform(put("/api/betting-rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bettingRound)))
            .andExpect(status().isBadRequest());

        // Validate the BettingRound in the database
        List<BettingRound> bettingRoundList = bettingRoundRepository.findAll();
        assertThat(bettingRoundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBettingRound() throws Exception {
        // Initialize the database
        bettingRoundRepository.saveAndFlush(bettingRound);

        int databaseSizeBeforeDelete = bettingRoundRepository.findAll().size();

        // Delete the bettingRound
        restBettingRoundMockMvc.perform(delete("/api/betting-rounds/{id}", bettingRound.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BettingRound> bettingRoundList = bettingRoundRepository.findAll();
        assertThat(bettingRoundList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BettingRound.class);
        BettingRound bettingRound1 = new BettingRound();
        bettingRound1.setId(1L);
        BettingRound bettingRound2 = new BettingRound();
        bettingRound2.setId(bettingRound1.getId());
        assertThat(bettingRound1).isEqualTo(bettingRound2);
        bettingRound2.setId(2L);
        assertThat(bettingRound1).isNotEqualTo(bettingRound2);
        bettingRound1.setId(null);
        assertThat(bettingRound1).isNotEqualTo(bettingRound2);
    }
}
