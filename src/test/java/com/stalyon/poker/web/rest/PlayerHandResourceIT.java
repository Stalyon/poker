package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.PlayerHand;
import com.stalyon.poker.repository.PlayerHandRepository;
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
 * Integration tests for the {@link PlayerHandResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class PlayerHandResourceIT {

    private static final Boolean DEFAULT_CALLS_PF = false;
    private static final Boolean UPDATED_CALLS_PF = true;

    private static final Boolean DEFAULT_RAISES_PF = false;
    private static final Boolean UPDATED_RAISES_PF = true;

    private static final Boolean DEFAULT_THREE_BET_PF = false;
    private static final Boolean UPDATED_THREE_BET_PF = true;

    private static final Boolean DEFAULT_CALLS_FLOP = false;
    private static final Boolean UPDATED_CALLS_FLOP = true;

    private static final Boolean DEFAULT_BETS_FLOP = false;
    private static final Boolean UPDATED_BETS_FLOP = true;

    private static final Boolean DEFAULT_RAISES_FLOP = false;
    private static final Boolean UPDATED_RAISES_FLOP = true;

    @Autowired
    private PlayerHandRepository playerHandRepository;

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

    private MockMvc restPlayerHandMockMvc;

    private PlayerHand playerHand;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerHandResource playerHandResource = new PlayerHandResource(playerHandRepository);
        this.restPlayerHandMockMvc = MockMvcBuilders.standaloneSetup(playerHandResource)
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
    public static PlayerHand createEntity(EntityManager em) {
        PlayerHand playerHand = new PlayerHand()
            .callsPf(DEFAULT_CALLS_PF)
            .raisesPf(DEFAULT_RAISES_PF)
            .threeBetPf(DEFAULT_THREE_BET_PF)
            .callsFlop(DEFAULT_CALLS_FLOP)
            .betsFlop(DEFAULT_BETS_FLOP)
            .raisesFlop(DEFAULT_RAISES_FLOP);
        return playerHand;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerHand createUpdatedEntity(EntityManager em) {
        PlayerHand playerHand = new PlayerHand()
            .callsPf(UPDATED_CALLS_PF)
            .raisesPf(UPDATED_RAISES_PF)
            .threeBetPf(UPDATED_THREE_BET_PF)
            .callsFlop(UPDATED_CALLS_FLOP)
            .betsFlop(UPDATED_BETS_FLOP)
            .raisesFlop(UPDATED_RAISES_FLOP);
        return playerHand;
    }

    @BeforeEach
    public void initTest() {
        playerHand = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayerHand() throws Exception {
        int databaseSizeBeforeCreate = playerHandRepository.findAll().size();

        // Create the PlayerHand
        restPlayerHandMockMvc.perform(post("/api/player-hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerHand)))
            .andExpect(status().isCreated());

        // Validate the PlayerHand in the database
        List<PlayerHand> playerHandList = playerHandRepository.findAll();
        assertThat(playerHandList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerHand testPlayerHand = playerHandList.get(playerHandList.size() - 1);
        assertThat(testPlayerHand.isCallsPf()).isEqualTo(DEFAULT_CALLS_PF);
        assertThat(testPlayerHand.isRaisesPf()).isEqualTo(DEFAULT_RAISES_PF);
        assertThat(testPlayerHand.isThreeBetPf()).isEqualTo(DEFAULT_THREE_BET_PF);
        assertThat(testPlayerHand.isCallsFlop()).isEqualTo(DEFAULT_CALLS_FLOP);
        assertThat(testPlayerHand.isBetsFlop()).isEqualTo(DEFAULT_BETS_FLOP);
        assertThat(testPlayerHand.isRaisesFlop()).isEqualTo(DEFAULT_RAISES_FLOP);
    }

    @Test
    @Transactional
    public void createPlayerHandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerHandRepository.findAll().size();

        // Create the PlayerHand with an existing ID
        playerHand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerHandMockMvc.perform(post("/api/player-hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerHand)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerHand in the database
        List<PlayerHand> playerHandList = playerHandRepository.findAll();
        assertThat(playerHandList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlayerHands() throws Exception {
        // Initialize the database
        playerHandRepository.saveAndFlush(playerHand);

        // Get all the playerHandList
        restPlayerHandMockMvc.perform(get("/api/player-hands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerHand.getId().intValue())))
            .andExpect(jsonPath("$.[*].callsPf").value(hasItem(DEFAULT_CALLS_PF.booleanValue())))
            .andExpect(jsonPath("$.[*].raisesPf").value(hasItem(DEFAULT_RAISES_PF.booleanValue())))
            .andExpect(jsonPath("$.[*].threeBetPf").value(hasItem(DEFAULT_THREE_BET_PF.booleanValue())))
            .andExpect(jsonPath("$.[*].callsFlop").value(hasItem(DEFAULT_CALLS_FLOP.booleanValue())))
            .andExpect(jsonPath("$.[*].betsFlop").value(hasItem(DEFAULT_BETS_FLOP.booleanValue())))
            .andExpect(jsonPath("$.[*].raisesFlop").value(hasItem(DEFAULT_RAISES_FLOP.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPlayerHand() throws Exception {
        // Initialize the database
        playerHandRepository.saveAndFlush(playerHand);

        // Get the playerHand
        restPlayerHandMockMvc.perform(get("/api/player-hands/{id}", playerHand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(playerHand.getId().intValue()))
            .andExpect(jsonPath("$.callsPf").value(DEFAULT_CALLS_PF.booleanValue()))
            .andExpect(jsonPath("$.raisesPf").value(DEFAULT_RAISES_PF.booleanValue()))
            .andExpect(jsonPath("$.threeBetPf").value(DEFAULT_THREE_BET_PF.booleanValue()))
            .andExpect(jsonPath("$.callsFlop").value(DEFAULT_CALLS_FLOP.booleanValue()))
            .andExpect(jsonPath("$.betsFlop").value(DEFAULT_BETS_FLOP.booleanValue()))
            .andExpect(jsonPath("$.raisesFlop").value(DEFAULT_RAISES_FLOP.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlayerHand() throws Exception {
        // Get the playerHand
        restPlayerHandMockMvc.perform(get("/api/player-hands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayerHand() throws Exception {
        // Initialize the database
        playerHandRepository.saveAndFlush(playerHand);

        int databaseSizeBeforeUpdate = playerHandRepository.findAll().size();

        // Update the playerHand
        PlayerHand updatedPlayerHand = playerHandRepository.findById(playerHand.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerHand are not directly saved in db
        em.detach(updatedPlayerHand);
        updatedPlayerHand
            .callsPf(UPDATED_CALLS_PF)
            .raisesPf(UPDATED_RAISES_PF)
            .threeBetPf(UPDATED_THREE_BET_PF)
            .callsFlop(UPDATED_CALLS_FLOP)
            .betsFlop(UPDATED_BETS_FLOP)
            .raisesFlop(UPDATED_RAISES_FLOP);

        restPlayerHandMockMvc.perform(put("/api/player-hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlayerHand)))
            .andExpect(status().isOk());

        // Validate the PlayerHand in the database
        List<PlayerHand> playerHandList = playerHandRepository.findAll();
        assertThat(playerHandList).hasSize(databaseSizeBeforeUpdate);
        PlayerHand testPlayerHand = playerHandList.get(playerHandList.size() - 1);
        assertThat(testPlayerHand.isCallsPf()).isEqualTo(UPDATED_CALLS_PF);
        assertThat(testPlayerHand.isRaisesPf()).isEqualTo(UPDATED_RAISES_PF);
        assertThat(testPlayerHand.isThreeBetPf()).isEqualTo(UPDATED_THREE_BET_PF);
        assertThat(testPlayerHand.isCallsFlop()).isEqualTo(UPDATED_CALLS_FLOP);
        assertThat(testPlayerHand.isBetsFlop()).isEqualTo(UPDATED_BETS_FLOP);
        assertThat(testPlayerHand.isRaisesFlop()).isEqualTo(UPDATED_RAISES_FLOP);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayerHand() throws Exception {
        int databaseSizeBeforeUpdate = playerHandRepository.findAll().size();

        // Create the PlayerHand

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerHandMockMvc.perform(put("/api/player-hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerHand)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerHand in the database
        List<PlayerHand> playerHandList = playerHandRepository.findAll();
        assertThat(playerHandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayerHand() throws Exception {
        // Initialize the database
        playerHandRepository.saveAndFlush(playerHand);

        int databaseSizeBeforeDelete = playerHandRepository.findAll().size();

        // Delete the playerHand
        restPlayerHandMockMvc.perform(delete("/api/player-hands/{id}", playerHand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerHand> playerHandList = playerHandRepository.findAll();
        assertThat(playerHandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
