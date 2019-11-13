package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.Tournoi;
import com.stalyon.poker.repository.TournoiRepository;
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
 * Integration tests for the {@link TournoiResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class TournoiResourceIT {

    private static final Double DEFAULT_BUY_IN = 1D;
    private static final Double UPDATED_BUY_IN = 2D;

    private static final Double DEFAULT_REBUY = 1D;
    private static final Double UPDATED_REBUY = 2D;

    private static final Integer DEFAULT_RANKING = 1;
    private static final Integer UPDATED_RANKING = 2;

    private static final Double DEFAULT_PROFIT = 1D;
    private static final Double UPDATED_PROFIT = 2D;

    private static final Double DEFAULT_BOUNTY = 1D;
    private static final Double UPDATED_BOUNTY = 2D;

    @Autowired
    private TournoiRepository tournoiRepository;

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

    private MockMvc restTournoiMockMvc;

    private Tournoi tournoi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TournoiResource tournoiResource = new TournoiResource(tournoiRepository);
        this.restTournoiMockMvc = MockMvcBuilders.standaloneSetup(tournoiResource)
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
    public static Tournoi createEntity(EntityManager em) {
        Tournoi tournoi = new Tournoi()
            .buyIn(DEFAULT_BUY_IN)
            .rebuy(DEFAULT_REBUY)
            .ranking(DEFAULT_RANKING)
            .profit(DEFAULT_PROFIT)
            .bounty(DEFAULT_BOUNTY);
        return tournoi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tournoi createUpdatedEntity(EntityManager em) {
        Tournoi tournoi = new Tournoi()
            .buyIn(UPDATED_BUY_IN)
            .rebuy(UPDATED_REBUY)
            .ranking(UPDATED_RANKING)
            .profit(UPDATED_PROFIT)
            .bounty(UPDATED_BOUNTY);
        return tournoi;
    }

    @BeforeEach
    public void initTest() {
        tournoi = createEntity(em);
    }

    @Test
    @Transactional
    public void createTournoi() throws Exception {
        int databaseSizeBeforeCreate = tournoiRepository.findAll().size();

        // Create the Tournoi
        restTournoiMockMvc.perform(post("/api/tournois")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournoi)))
            .andExpect(status().isCreated());

        // Validate the Tournoi in the database
        List<Tournoi> tournoiList = tournoiRepository.findAll();
        assertThat(tournoiList).hasSize(databaseSizeBeforeCreate + 1);
        Tournoi testTournoi = tournoiList.get(tournoiList.size() - 1);
        assertThat(testTournoi.getBuyIn()).isEqualTo(DEFAULT_BUY_IN);
        assertThat(testTournoi.getRebuy()).isEqualTo(DEFAULT_REBUY);
        assertThat(testTournoi.getRanking()).isEqualTo(DEFAULT_RANKING);
        assertThat(testTournoi.getProfit()).isEqualTo(DEFAULT_PROFIT);
        assertThat(testTournoi.getBounty()).isEqualTo(DEFAULT_BOUNTY);
    }

    @Test
    @Transactional
    public void createTournoiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tournoiRepository.findAll().size();

        // Create the Tournoi with an existing ID
        tournoi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTournoiMockMvc.perform(post("/api/tournois")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournoi)))
            .andExpect(status().isBadRequest());

        // Validate the Tournoi in the database
        List<Tournoi> tournoiList = tournoiRepository.findAll();
        assertThat(tournoiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTournois() throws Exception {
        // Initialize the database
        tournoiRepository.saveAndFlush(tournoi);

        // Get all the tournoiList
        restTournoiMockMvc.perform(get("/api/tournois?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournoi.getId().intValue())))
            .andExpect(jsonPath("$.[*].buyIn").value(hasItem(DEFAULT_BUY_IN.doubleValue())))
            .andExpect(jsonPath("$.[*].rebuy").value(hasItem(DEFAULT_REBUY.doubleValue())))
            .andExpect(jsonPath("$.[*].ranking").value(hasItem(DEFAULT_RANKING)))
            .andExpect(jsonPath("$.[*].profit").value(hasItem(DEFAULT_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].bounty").value(hasItem(DEFAULT_BOUNTY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getTournoi() throws Exception {
        // Initialize the database
        tournoiRepository.saveAndFlush(tournoi);

        // Get the tournoi
        restTournoiMockMvc.perform(get("/api/tournois/{id}", tournoi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tournoi.getId().intValue()))
            .andExpect(jsonPath("$.buyIn").value(DEFAULT_BUY_IN.doubleValue()))
            .andExpect(jsonPath("$.rebuy").value(DEFAULT_REBUY.doubleValue()))
            .andExpect(jsonPath("$.ranking").value(DEFAULT_RANKING))
            .andExpect(jsonPath("$.profit").value(DEFAULT_PROFIT.doubleValue()))
            .andExpect(jsonPath("$.bounty").value(DEFAULT_BOUNTY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTournoi() throws Exception {
        // Get the tournoi
        restTournoiMockMvc.perform(get("/api/tournois/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTournoi() throws Exception {
        // Initialize the database
        tournoiRepository.saveAndFlush(tournoi);

        int databaseSizeBeforeUpdate = tournoiRepository.findAll().size();

        // Update the tournoi
        Tournoi updatedTournoi = tournoiRepository.findById(tournoi.getId()).get();
        // Disconnect from session so that the updates on updatedTournoi are not directly saved in db
        em.detach(updatedTournoi);
        updatedTournoi
            .buyIn(UPDATED_BUY_IN)
            .rebuy(UPDATED_REBUY)
            .ranking(UPDATED_RANKING)
            .profit(UPDATED_PROFIT)
            .bounty(UPDATED_BOUNTY);

        restTournoiMockMvc.perform(put("/api/tournois")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTournoi)))
            .andExpect(status().isOk());

        // Validate the Tournoi in the database
        List<Tournoi> tournoiList = tournoiRepository.findAll();
        assertThat(tournoiList).hasSize(databaseSizeBeforeUpdate);
        Tournoi testTournoi = tournoiList.get(tournoiList.size() - 1);
        assertThat(testTournoi.getBuyIn()).isEqualTo(UPDATED_BUY_IN);
        assertThat(testTournoi.getRebuy()).isEqualTo(UPDATED_REBUY);
        assertThat(testTournoi.getRanking()).isEqualTo(UPDATED_RANKING);
        assertThat(testTournoi.getProfit()).isEqualTo(UPDATED_PROFIT);
        assertThat(testTournoi.getBounty()).isEqualTo(UPDATED_BOUNTY);
    }

    @Test
    @Transactional
    public void updateNonExistingTournoi() throws Exception {
        int databaseSizeBeforeUpdate = tournoiRepository.findAll().size();

        // Create the Tournoi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournoiMockMvc.perform(put("/api/tournois")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournoi)))
            .andExpect(status().isBadRequest());

        // Validate the Tournoi in the database
        List<Tournoi> tournoiList = tournoiRepository.findAll();
        assertThat(tournoiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTournoi() throws Exception {
        // Initialize the database
        tournoiRepository.saveAndFlush(tournoi);

        int databaseSizeBeforeDelete = tournoiRepository.findAll().size();

        // Delete the tournoi
        restTournoiMockMvc.perform(delete("/api/tournois/{id}", tournoi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tournoi> tournoiList = tournoiRepository.findAll();
        assertThat(tournoiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
