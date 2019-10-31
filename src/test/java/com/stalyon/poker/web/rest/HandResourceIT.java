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
            .startDate(DEFAULT_START_DATE);
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
            .startDate(UPDATED_START_DATE);
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
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())));
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
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()));
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
            .startDate(UPDATED_START_DATE);

        restHandMockMvc.perform(put("/api/hands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHand)))
            .andExpect(status().isOk());

        // Validate the Hand in the database
        List<Hand> handList = handRepository.findAll();
        assertThat(handList).hasSize(databaseSizeBeforeUpdate);
        Hand testHand = handList.get(handList.size() - 1);
        assertThat(testHand.getStartDate()).isEqualTo(UPDATED_START_DATE);
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

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hand.class);
        Hand hand1 = new Hand();
        hand1.setId(1L);
        Hand hand2 = new Hand();
        hand2.setId(hand1.getId());
        assertThat(hand1).isEqualTo(hand2);
        hand2.setId(2L);
        assertThat(hand1).isNotEqualTo(hand2);
        hand1.setId(null);
        assertThat(hand1).isNotEqualTo(hand2);
    }
}
