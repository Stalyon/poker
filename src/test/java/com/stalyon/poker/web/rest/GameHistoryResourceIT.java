package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.GameHistory;
import com.stalyon.poker.repository.GameHistoryRepository;
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

import com.stalyon.poker.domain.enumeration.GameType;
/**
 * Integration tests for the {@link GameHistoryResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class GameHistoryResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final GameType DEFAULT_TYPE = GameType.TOURNOI;
    private static final GameType UPDATED_TYPE = GameType.SIT_AND_GO;

    private static final Double DEFAULT_NET_RESULT = 1D;
    private static final Double UPDATED_NET_RESULT = 2D;

    @Autowired
    private GameHistoryRepository gameHistoryRepository;

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

    private MockMvc restGameHistoryMockMvc;

    private GameHistory gameHistory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GameHistoryResource gameHistoryResource = new GameHistoryResource(gameHistoryRepository);
        this.restGameHistoryMockMvc = MockMvcBuilders.standaloneSetup(gameHistoryResource)
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
    public static GameHistory createEntity(EntityManager em) {
        GameHistory gameHistory = new GameHistory()
            .startDate(DEFAULT_START_DATE)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .netResult(DEFAULT_NET_RESULT);
        return gameHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameHistory createUpdatedEntity(EntityManager em) {
        GameHistory gameHistory = new GameHistory()
            .startDate(UPDATED_START_DATE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .netResult(UPDATED_NET_RESULT);
        return gameHistory;
    }

    @BeforeEach
    public void initTest() {
        gameHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createGameHistory() throws Exception {
        int databaseSizeBeforeCreate = gameHistoryRepository.findAll().size();

        // Create the GameHistory
        restGameHistoryMockMvc.perform(post("/api/game-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameHistory)))
            .andExpect(status().isCreated());

        // Validate the GameHistory in the database
        List<GameHistory> gameHistoryList = gameHistoryRepository.findAll();
        assertThat(gameHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        GameHistory testGameHistory = gameHistoryList.get(gameHistoryList.size() - 1);
        assertThat(testGameHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testGameHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGameHistory.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGameHistory.getNetResult()).isEqualTo(DEFAULT_NET_RESULT);
    }

    @Test
    @Transactional
    public void createGameHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gameHistoryRepository.findAll().size();

        // Create the GameHistory with an existing ID
        gameHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameHistoryMockMvc.perform(post("/api/game-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameHistory)))
            .andExpect(status().isBadRequest());

        // Validate the GameHistory in the database
        List<GameHistory> gameHistoryList = gameHistoryRepository.findAll();
        assertThat(gameHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameHistoryRepository.findAll().size();
        // set the field null
        gameHistory.setStartDate(null);

        // Create the GameHistory, which fails.

        restGameHistoryMockMvc.perform(post("/api/game-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameHistory)))
            .andExpect(status().isBadRequest());

        List<GameHistory> gameHistoryList = gameHistoryRepository.findAll();
        assertThat(gameHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameHistoryRepository.findAll().size();
        // set the field null
        gameHistory.setName(null);

        // Create the GameHistory, which fails.

        restGameHistoryMockMvc.perform(post("/api/game-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameHistory)))
            .andExpect(status().isBadRequest());

        List<GameHistory> gameHistoryList = gameHistoryRepository.findAll();
        assertThat(gameHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGameHistories() throws Exception {
        // Initialize the database
        gameHistoryRepository.saveAndFlush(gameHistory);

        // Get all the gameHistoryList
        restGameHistoryMockMvc.perform(get("/api/game-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].netResult").value(hasItem(DEFAULT_NET_RESULT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getGameHistory() throws Exception {
        // Initialize the database
        gameHistoryRepository.saveAndFlush(gameHistory);

        // Get the gameHistory
        restGameHistoryMockMvc.perform(get("/api/game-histories/{id}", gameHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gameHistory.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.netResult").value(DEFAULT_NET_RESULT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGameHistory() throws Exception {
        // Get the gameHistory
        restGameHistoryMockMvc.perform(get("/api/game-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGameHistory() throws Exception {
        // Initialize the database
        gameHistoryRepository.saveAndFlush(gameHistory);

        int databaseSizeBeforeUpdate = gameHistoryRepository.findAll().size();

        // Update the gameHistory
        GameHistory updatedGameHistory = gameHistoryRepository.findById(gameHistory.getId()).get();
        // Disconnect from session so that the updates on updatedGameHistory are not directly saved in db
        em.detach(updatedGameHistory);
        updatedGameHistory
            .startDate(UPDATED_START_DATE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .netResult(UPDATED_NET_RESULT);

        restGameHistoryMockMvc.perform(put("/api/game-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGameHistory)))
            .andExpect(status().isOk());

        // Validate the GameHistory in the database
        List<GameHistory> gameHistoryList = gameHistoryRepository.findAll();
        assertThat(gameHistoryList).hasSize(databaseSizeBeforeUpdate);
        GameHistory testGameHistory = gameHistoryList.get(gameHistoryList.size() - 1);
        assertThat(testGameHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testGameHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGameHistory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGameHistory.getNetResult()).isEqualTo(UPDATED_NET_RESULT);
    }

    @Test
    @Transactional
    public void updateNonExistingGameHistory() throws Exception {
        int databaseSizeBeforeUpdate = gameHistoryRepository.findAll().size();

        // Create the GameHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameHistoryMockMvc.perform(put("/api/game-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameHistory)))
            .andExpect(status().isBadRequest());

        // Validate the GameHistory in the database
        List<GameHistory> gameHistoryList = gameHistoryRepository.findAll();
        assertThat(gameHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGameHistory() throws Exception {
        // Initialize the database
        gameHistoryRepository.saveAndFlush(gameHistory);

        int databaseSizeBeforeDelete = gameHistoryRepository.findAll().size();

        // Delete the gameHistory
        restGameHistoryMockMvc.perform(delete("/api/game-histories/{id}", gameHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameHistory> gameHistoryList = gameHistoryRepository.findAll();
        assertThat(gameHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
