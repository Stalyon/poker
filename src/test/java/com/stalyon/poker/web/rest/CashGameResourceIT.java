package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.CashGame;
import com.stalyon.poker.repository.CashGameRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.stalyon.poker.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CashGameResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class CashGameResourceIT {

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PROFIT = 1D;
    private static final Double UPDATED_PROFIT = 2D;

    private static final String DEFAULT_TABLE = "AAAAAAAAAA";
    private static final String UPDATED_TABLE = "BBBBBBBBBB";

    private static final String DEFAULT_SB_BB = "AAAAAAAAAA";
    private static final String UPDATED_SB_BB = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_HANDS = 1;
    private static final Integer UPDATED_NB_HANDS = 2;

    @Autowired
    private CashGameRepository cashGameRepository;

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

    private MockMvc restCashGameMockMvc;

    private CashGame cashGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashGameResource cashGameResource = new CashGameResource(cashGameRepository);
        this.restCashGameMockMvc = MockMvcBuilders.standaloneSetup(cashGameResource)
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
    public static CashGame createEntity(EntityManager em) {
        CashGame cashGame = new CashGame()
            .endDate(DEFAULT_END_DATE)
            .profit(DEFAULT_PROFIT)
            .table(DEFAULT_TABLE)
            .sbBb(DEFAULT_SB_BB)
            .nbHands(DEFAULT_NB_HANDS);
        return cashGame;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashGame createUpdatedEntity(EntityManager em) {
        CashGame cashGame = new CashGame()
            .endDate(UPDATED_END_DATE)
            .profit(UPDATED_PROFIT)
            .table(UPDATED_TABLE)
            .sbBb(UPDATED_SB_BB)
            .nbHands(UPDATED_NB_HANDS);
        return cashGame;
    }

    @BeforeEach
    public void initTest() {
        cashGame = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashGame() throws Exception {
        int databaseSizeBeforeCreate = cashGameRepository.findAll().size();

        // Create the CashGame
        restCashGameMockMvc.perform(post("/api/cash-games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashGame)))
            .andExpect(status().isCreated());

        // Validate the CashGame in the database
        List<CashGame> cashGameList = cashGameRepository.findAll();
        assertThat(cashGameList).hasSize(databaseSizeBeforeCreate + 1);
        CashGame testCashGame = cashGameList.get(cashGameList.size() - 1);
        assertThat(testCashGame.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCashGame.getProfit()).isEqualTo(DEFAULT_PROFIT);
        assertThat(testCashGame.getTable()).isEqualTo(DEFAULT_TABLE);
        assertThat(testCashGame.getSbBb()).isEqualTo(DEFAULT_SB_BB);
        assertThat(testCashGame.getNbHands()).isEqualTo(DEFAULT_NB_HANDS);
    }

    @Test
    @Transactional
    public void createCashGameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashGameRepository.findAll().size();

        // Create the CashGame with an existing ID
        cashGame.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashGameMockMvc.perform(post("/api/cash-games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashGame)))
            .andExpect(status().isBadRequest());

        // Validate the CashGame in the database
        List<CashGame> cashGameList = cashGameRepository.findAll();
        assertThat(cashGameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCashGames() throws Exception {
        // Initialize the database
        cashGameRepository.saveAndFlush(cashGame);

        // Get all the cashGameList
        restCashGameMockMvc.perform(get("/api/cash-games?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashGame.getId().intValue())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].profit").value(hasItem(DEFAULT_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].table").value(hasItem(DEFAULT_TABLE)))
            .andExpect(jsonPath("$.[*].sbBb").value(hasItem(DEFAULT_SB_BB)))
            .andExpect(jsonPath("$.[*].nbHands").value(hasItem(DEFAULT_NB_HANDS)));
    }
    
    @Test
    @Transactional
    public void getCashGame() throws Exception {
        // Initialize the database
        cashGameRepository.saveAndFlush(cashGame);

        // Get the cashGame
        restCashGameMockMvc.perform(get("/api/cash-games/{id}", cashGame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashGame.getId().intValue()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.profit").value(DEFAULT_PROFIT.doubleValue()))
            .andExpect(jsonPath("$.table").value(DEFAULT_TABLE))
            .andExpect(jsonPath("$.sbBb").value(DEFAULT_SB_BB))
            .andExpect(jsonPath("$.nbHands").value(DEFAULT_NB_HANDS));
    }

    @Test
    @Transactional
    public void getNonExistingCashGame() throws Exception {
        // Get the cashGame
        restCashGameMockMvc.perform(get("/api/cash-games/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashGame() throws Exception {
        // Initialize the database
        cashGameRepository.saveAndFlush(cashGame);

        int databaseSizeBeforeUpdate = cashGameRepository.findAll().size();

        // Update the cashGame
        CashGame updatedCashGame = cashGameRepository.findById(cashGame.getId()).get();
        // Disconnect from session so that the updates on updatedCashGame are not directly saved in db
        em.detach(updatedCashGame);
        updatedCashGame
            .endDate(UPDATED_END_DATE)
            .profit(UPDATED_PROFIT)
            .table(UPDATED_TABLE)
            .sbBb(UPDATED_SB_BB)
            .nbHands(UPDATED_NB_HANDS);

        restCashGameMockMvc.perform(put("/api/cash-games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashGame)))
            .andExpect(status().isOk());

        // Validate the CashGame in the database
        List<CashGame> cashGameList = cashGameRepository.findAll();
        assertThat(cashGameList).hasSize(databaseSizeBeforeUpdate);
        CashGame testCashGame = cashGameList.get(cashGameList.size() - 1);
        assertThat(testCashGame.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCashGame.getProfit()).isEqualTo(UPDATED_PROFIT);
        assertThat(testCashGame.getTable()).isEqualTo(UPDATED_TABLE);
        assertThat(testCashGame.getSbBb()).isEqualTo(UPDATED_SB_BB);
        assertThat(testCashGame.getNbHands()).isEqualTo(UPDATED_NB_HANDS);
    }

    @Test
    @Transactional
    public void updateNonExistingCashGame() throws Exception {
        int databaseSizeBeforeUpdate = cashGameRepository.findAll().size();

        // Create the CashGame

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashGameMockMvc.perform(put("/api/cash-games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashGame)))
            .andExpect(status().isBadRequest());

        // Validate the CashGame in the database
        List<CashGame> cashGameList = cashGameRepository.findAll();
        assertThat(cashGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashGame() throws Exception {
        // Initialize the database
        cashGameRepository.saveAndFlush(cashGame);

        int databaseSizeBeforeDelete = cashGameRepository.findAll().size();

        // Delete the cashGame
        restCashGameMockMvc.perform(delete("/api/cash-games/{id}", cashGame.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashGame> cashGameList = cashGameRepository.findAll();
        assertThat(cashGameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
