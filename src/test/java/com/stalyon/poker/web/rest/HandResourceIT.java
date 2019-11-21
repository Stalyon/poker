package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.Hand;
import com.stalyon.poker.repository.HandRepository;
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
 * Integration tests for the {@link HandResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class HandResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_BUTTON_POSITION = 1;
    private static final Integer UPDATED_BUTTON_POSITION = 2;

    private static final String DEFAULT_MY_CARDS = "AAAAAAAAAA";
    private static final String UPDATED_MY_CARDS = "BBBBBBBBBB";

    private static final String DEFAULT_FLOP_CARDS = "AAAAAAAAAA";
    private static final String UPDATED_FLOP_CARDS = "BBBBBBBBBB";

    private static final String DEFAULT_RIVER_CARDS = "AAAAAAAAAA";
    private static final String UPDATED_RIVER_CARDS = "BBBBBBBBBB";

    private static final String DEFAULT_TURN_CARDS = "AAAAAAAAAA";
    private static final String UPDATED_TURN_CARDS = "BBBBBBBBBB";

    @Autowired
    private HandRepository handRepository;

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

    private MockMvc restHandMockMvc;

    private Hand hand;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HandResource handResource = new HandResource(handRepository);
        this.restHandMockMvc = MockMvcBuilders.standaloneSetup(handResource)
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
    public static Hand createEntity(EntityManager em) {
        Hand hand = new Hand()
            .startDate(DEFAULT_START_DATE)
            .buttonPosition(DEFAULT_BUTTON_POSITION)
            .myCards(DEFAULT_MY_CARDS)
            .flopCards(DEFAULT_FLOP_CARDS)
            .riverCards(DEFAULT_RIVER_CARDS)
            .turnCards(DEFAULT_TURN_CARDS);
        return hand;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hand createUpdatedEntity(EntityManager em) {
        Hand hand = new Hand()
            .startDate(UPDATED_START_DATE)
            .buttonPosition(UPDATED_BUTTON_POSITION)
            .myCards(UPDATED_MY_CARDS)
            .flopCards(UPDATED_FLOP_CARDS)
            .riverCards(UPDATED_RIVER_CARDS)
            .turnCards(UPDATED_TURN_CARDS);
        return hand;
    }

    @BeforeEach
    public void initTest() {
        hand = createEntity(em);
    }

    @Test
    @Transactional
    public void createHand() throws Exception {
        int databaseSizeBeforeCreate = handRepository.findAll().size();

        // Create the Hand
        restHandMockMvc.perform(post("/api/hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hand)))
            .andExpect(status().isCreated());

        // Validate the Hand in the database
        List<Hand> handList = handRepository.findAll();
        assertThat(handList).hasSize(databaseSizeBeforeCreate + 1);
        Hand testHand = handList.get(handList.size() - 1);
        assertThat(testHand.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testHand.getButtonPosition()).isEqualTo(DEFAULT_BUTTON_POSITION);
        assertThat(testHand.getMyCards()).isEqualTo(DEFAULT_MY_CARDS);
        assertThat(testHand.getFlopCards()).isEqualTo(DEFAULT_FLOP_CARDS);
        assertThat(testHand.getRiverCards()).isEqualTo(DEFAULT_RIVER_CARDS);
        assertThat(testHand.getTurnCards()).isEqualTo(DEFAULT_TURN_CARDS);
    }

    @Test
    @Transactional
    public void createHandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = handRepository.findAll().size();

        // Create the Hand with an existing ID
        hand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHandMockMvc.perform(post("/api/hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hand)))
            .andExpect(status().isBadRequest());

        // Validate the Hand in the database
        List<Hand> handList = handRepository.findAll();
        assertThat(handList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllHands() throws Exception {
        // Initialize the database
        handRepository.saveAndFlush(hand);

        // Get all the handList
        restHandMockMvc.perform(get("/api/hands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hand.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].buttonPosition").value(hasItem(DEFAULT_BUTTON_POSITION)))
            .andExpect(jsonPath("$.[*].myCards").value(hasItem(DEFAULT_MY_CARDS)))
            .andExpect(jsonPath("$.[*].flopCards").value(hasItem(DEFAULT_FLOP_CARDS)))
            .andExpect(jsonPath("$.[*].riverCards").value(hasItem(DEFAULT_RIVER_CARDS)))
            .andExpect(jsonPath("$.[*].turnCards").value(hasItem(DEFAULT_TURN_CARDS)));
    }
    
    @Test
    @Transactional
    public void getHand() throws Exception {
        // Initialize the database
        handRepository.saveAndFlush(hand);

        // Get the hand
        restHandMockMvc.perform(get("/api/hands/{id}", hand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hand.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.buttonPosition").value(DEFAULT_BUTTON_POSITION))
            .andExpect(jsonPath("$.myCards").value(DEFAULT_MY_CARDS))
            .andExpect(jsonPath("$.flopCards").value(DEFAULT_FLOP_CARDS))
            .andExpect(jsonPath("$.riverCards").value(DEFAULT_RIVER_CARDS))
            .andExpect(jsonPath("$.turnCards").value(DEFAULT_TURN_CARDS));
    }

    @Test
    @Transactional
    public void getNonExistingHand() throws Exception {
        // Get the hand
        restHandMockMvc.perform(get("/api/hands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHand() throws Exception {
        // Initialize the database
        handRepository.saveAndFlush(hand);

        int databaseSizeBeforeUpdate = handRepository.findAll().size();

        // Update the hand
        Hand updatedHand = handRepository.findById(hand.getId()).get();
        // Disconnect from session so that the updates on updatedHand are not directly saved in db
        em.detach(updatedHand);
        updatedHand
            .startDate(UPDATED_START_DATE)
            .buttonPosition(UPDATED_BUTTON_POSITION)
            .myCards(UPDATED_MY_CARDS)
            .flopCards(UPDATED_FLOP_CARDS)
            .riverCards(UPDATED_RIVER_CARDS)
            .turnCards(UPDATED_TURN_CARDS);

        restHandMockMvc.perform(put("/api/hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHand)))
            .andExpect(status().isOk());

        // Validate the Hand in the database
        List<Hand> handList = handRepository.findAll();
        assertThat(handList).hasSize(databaseSizeBeforeUpdate);
        Hand testHand = handList.get(handList.size() - 1);
        assertThat(testHand.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHand.getButtonPosition()).isEqualTo(UPDATED_BUTTON_POSITION);
        assertThat(testHand.getMyCards()).isEqualTo(UPDATED_MY_CARDS);
        assertThat(testHand.getFlopCards()).isEqualTo(UPDATED_FLOP_CARDS);
        assertThat(testHand.getRiverCards()).isEqualTo(UPDATED_RIVER_CARDS);
        assertThat(testHand.getTurnCards()).isEqualTo(UPDATED_TURN_CARDS);
    }

    @Test
    @Transactional
    public void updateNonExistingHand() throws Exception {
        int databaseSizeBeforeUpdate = handRepository.findAll().size();

        // Create the Hand

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHandMockMvc.perform(put("/api/hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hand)))
            .andExpect(status().isBadRequest());

        // Validate the Hand in the database
        List<Hand> handList = handRepository.findAll();
        assertThat(handList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHand() throws Exception {
        // Initialize the database
        handRepository.saveAndFlush(hand);

        int databaseSizeBeforeDelete = handRepository.findAll().size();

        // Delete the hand
        restHandMockMvc.perform(delete("/api/hands/{id}", hand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hand> handList = handRepository.findAll();
        assertThat(handList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
