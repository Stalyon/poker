package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.PlayerAction;
import com.stalyon.poker.repository.PlayerActionRepository;
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

import com.stalyon.poker.domain.enumeration.BettingRound;
import com.stalyon.poker.domain.enumeration.Action;
/**
 * Integration tests for the {@link PlayerActionResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class PlayerActionResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final BettingRound DEFAULT_BETTING_ROUND = BettingRound.ANTE_BLINDS;
    private static final BettingRound UPDATED_BETTING_ROUND = BettingRound.PRE_FLOP;

    private static final Action DEFAULT_ACTION = Action.CALLS;
    private static final Action UPDATED_ACTION = Action.FOLDS;

    @Autowired
    private PlayerActionRepository playerActionRepository;

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

    private MockMvc restPlayerActionMockMvc;

    private PlayerAction playerAction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerActionResource playerActionResource = new PlayerActionResource(playerActionRepository);
        this.restPlayerActionMockMvc = MockMvcBuilders.standaloneSetup(playerActionResource)
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
    public static PlayerAction createEntity(EntityManager em) {
        PlayerAction playerAction = new PlayerAction()
            .amount(DEFAULT_AMOUNT)
            .bettingRound(DEFAULT_BETTING_ROUND)
            .action(DEFAULT_ACTION);
        return playerAction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerAction createUpdatedEntity(EntityManager em) {
        PlayerAction playerAction = new PlayerAction()
            .amount(UPDATED_AMOUNT)
            .bettingRound(UPDATED_BETTING_ROUND)
            .action(UPDATED_ACTION);
        return playerAction;
    }

    @BeforeEach
    public void initTest() {
        playerAction = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayerAction() throws Exception {
        int databaseSizeBeforeCreate = playerActionRepository.findAll().size();

        // Create the PlayerAction
        restPlayerActionMockMvc.perform(post("/api/player-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerAction)))
            .andExpect(status().isCreated());

        // Validate the PlayerAction in the database
        List<PlayerAction> playerActionList = playerActionRepository.findAll();
        assertThat(playerActionList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerAction testPlayerAction = playerActionList.get(playerActionList.size() - 1);
        assertThat(testPlayerAction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPlayerAction.getBettingRound()).isEqualTo(DEFAULT_BETTING_ROUND);
        assertThat(testPlayerAction.getAction()).isEqualTo(DEFAULT_ACTION);
    }

    @Test
    @Transactional
    public void createPlayerActionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerActionRepository.findAll().size();

        // Create the PlayerAction with an existing ID
        playerAction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerActionMockMvc.perform(post("/api/player-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerAction)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerAction in the database
        List<PlayerAction> playerActionList = playerActionRepository.findAll();
        assertThat(playerActionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlayerActions() throws Exception {
        // Initialize the database
        playerActionRepository.saveAndFlush(playerAction);

        // Get all the playerActionList
        restPlayerActionMockMvc.perform(get("/api/player-actions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].bettingRound").value(hasItem(DEFAULT_BETTING_ROUND.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())));
    }
    
    @Test
    @Transactional
    public void getPlayerAction() throws Exception {
        // Initialize the database
        playerActionRepository.saveAndFlush(playerAction);

        // Get the playerAction
        restPlayerActionMockMvc.perform(get("/api/player-actions/{id}", playerAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(playerAction.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.bettingRound").value(DEFAULT_BETTING_ROUND.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlayerAction() throws Exception {
        // Get the playerAction
        restPlayerActionMockMvc.perform(get("/api/player-actions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayerAction() throws Exception {
        // Initialize the database
        playerActionRepository.saveAndFlush(playerAction);

        int databaseSizeBeforeUpdate = playerActionRepository.findAll().size();

        // Update the playerAction
        PlayerAction updatedPlayerAction = playerActionRepository.findById(playerAction.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerAction are not directly saved in db
        em.detach(updatedPlayerAction);
        updatedPlayerAction
            .amount(UPDATED_AMOUNT)
            .bettingRound(UPDATED_BETTING_ROUND)
            .action(UPDATED_ACTION);

        restPlayerActionMockMvc.perform(put("/api/player-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlayerAction)))
            .andExpect(status().isOk());

        // Validate the PlayerAction in the database
        List<PlayerAction> playerActionList = playerActionRepository.findAll();
        assertThat(playerActionList).hasSize(databaseSizeBeforeUpdate);
        PlayerAction testPlayerAction = playerActionList.get(playerActionList.size() - 1);
        assertThat(testPlayerAction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPlayerAction.getBettingRound()).isEqualTo(UPDATED_BETTING_ROUND);
        assertThat(testPlayerAction.getAction()).isEqualTo(UPDATED_ACTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayerAction() throws Exception {
        int databaseSizeBeforeUpdate = playerActionRepository.findAll().size();

        // Create the PlayerAction

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerActionMockMvc.perform(put("/api/player-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerAction)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerAction in the database
        List<PlayerAction> playerActionList = playerActionRepository.findAll();
        assertThat(playerActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayerAction() throws Exception {
        // Initialize the database
        playerActionRepository.saveAndFlush(playerAction);

        int databaseSizeBeforeDelete = playerActionRepository.findAll().size();

        // Delete the playerAction
        restPlayerActionMockMvc.perform(delete("/api/player-actions/{id}", playerAction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerAction> playerActionList = playerActionRepository.findAll();
        assertThat(playerActionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerAction.class);
        PlayerAction playerAction1 = new PlayerAction();
        playerAction1.setId(1L);
        PlayerAction playerAction2 = new PlayerAction();
        playerAction2.setId(playerAction1.getId());
        assertThat(playerAction1).isEqualTo(playerAction2);
        playerAction2.setId(2L);
        assertThat(playerAction1).isNotEqualTo(playerAction2);
        playerAction1.setId(null);
        assertThat(playerAction1).isNotEqualTo(playerAction2);
    }
}
