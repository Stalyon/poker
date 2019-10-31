package com.stalyon.poker.web.rest;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.domain.ShowDown;
import com.stalyon.poker.repository.ShowDownRepository;
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
 * Integration tests for the {@link ShowDownResource} REST controller.
 */
@SpringBootTest(classes = PokerApp.class)
public class ShowDownResourceIT {

    private static final String DEFAULT_CARDS = "AAAAAAAAAA";
    private static final String UPDATED_CARDS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WINS = false;
    private static final Boolean UPDATED_WINS = true;

    @Autowired
    private ShowDownRepository showDownRepository;

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

    private MockMvc restShowDownMockMvc;

    private ShowDown showDown;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShowDownResource showDownResource = new ShowDownResource(showDownRepository);
        this.restShowDownMockMvc = MockMvcBuilders.standaloneSetup(showDownResource)
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
    public static ShowDown createEntity(EntityManager em) {
        ShowDown showDown = new ShowDown()
            .cards(DEFAULT_CARDS)
            .wins(DEFAULT_WINS);
        return showDown;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShowDown createUpdatedEntity(EntityManager em) {
        ShowDown showDown = new ShowDown()
            .cards(UPDATED_CARDS)
            .wins(UPDATED_WINS);
        return showDown;
    }

    @BeforeEach
    public void initTest() {
        showDown = createEntity(em);
    }

    @Test
    @Transactional
    public void createShowDown() throws Exception {
        int databaseSizeBeforeCreate = showDownRepository.findAll().size();

        // Create the ShowDown
        restShowDownMockMvc.perform(post("/api/show-downs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(showDown)))
            .andExpect(status().isCreated());

        // Validate the ShowDown in the database
        List<ShowDown> showDownList = showDownRepository.findAll();
        assertThat(showDownList).hasSize(databaseSizeBeforeCreate + 1);
        ShowDown testShowDown = showDownList.get(showDownList.size() - 1);
        assertThat(testShowDown.getCards()).isEqualTo(DEFAULT_CARDS);
        assertThat(testShowDown.isWins()).isEqualTo(DEFAULT_WINS);
    }

    @Test
    @Transactional
    public void createShowDownWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = showDownRepository.findAll().size();

        // Create the ShowDown with an existing ID
        showDown.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShowDownMockMvc.perform(post("/api/show-downs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(showDown)))
            .andExpect(status().isBadRequest());

        // Validate the ShowDown in the database
        List<ShowDown> showDownList = showDownRepository.findAll();
        assertThat(showDownList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShowDowns() throws Exception {
        // Initialize the database
        showDownRepository.saveAndFlush(showDown);

        // Get all the showDownList
        restShowDownMockMvc.perform(get("/api/show-downs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(showDown.getId().intValue())))
            .andExpect(jsonPath("$.[*].cards").value(hasItem(DEFAULT_CARDS)))
            .andExpect(jsonPath("$.[*].wins").value(hasItem(DEFAULT_WINS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getShowDown() throws Exception {
        // Initialize the database
        showDownRepository.saveAndFlush(showDown);

        // Get the showDown
        restShowDownMockMvc.perform(get("/api/show-downs/{id}", showDown.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(showDown.getId().intValue()))
            .andExpect(jsonPath("$.cards").value(DEFAULT_CARDS))
            .andExpect(jsonPath("$.wins").value(DEFAULT_WINS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShowDown() throws Exception {
        // Get the showDown
        restShowDownMockMvc.perform(get("/api/show-downs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShowDown() throws Exception {
        // Initialize the database
        showDownRepository.saveAndFlush(showDown);

        int databaseSizeBeforeUpdate = showDownRepository.findAll().size();

        // Update the showDown
        ShowDown updatedShowDown = showDownRepository.findById(showDown.getId()).get();
        // Disconnect from session so that the updates on updatedShowDown are not directly saved in db
        em.detach(updatedShowDown);
        updatedShowDown
            .cards(UPDATED_CARDS)
            .wins(UPDATED_WINS);

        restShowDownMockMvc.perform(put("/api/show-downs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShowDown)))
            .andExpect(status().isOk());

        // Validate the ShowDown in the database
        List<ShowDown> showDownList = showDownRepository.findAll();
        assertThat(showDownList).hasSize(databaseSizeBeforeUpdate);
        ShowDown testShowDown = showDownList.get(showDownList.size() - 1);
        assertThat(testShowDown.getCards()).isEqualTo(UPDATED_CARDS);
        assertThat(testShowDown.isWins()).isEqualTo(UPDATED_WINS);
    }

    @Test
    @Transactional
    public void updateNonExistingShowDown() throws Exception {
        int databaseSizeBeforeUpdate = showDownRepository.findAll().size();

        // Create the ShowDown

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowDownMockMvc.perform(put("/api/show-downs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(showDown)))
            .andExpect(status().isBadRequest());

        // Validate the ShowDown in the database
        List<ShowDown> showDownList = showDownRepository.findAll();
        assertThat(showDownList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShowDown() throws Exception {
        // Initialize the database
        showDownRepository.saveAndFlush(showDown);

        int databaseSizeBeforeDelete = showDownRepository.findAll().size();

        // Delete the showDown
        restShowDownMockMvc.perform(delete("/api/show-downs/{id}", showDown.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShowDown> showDownList = showDownRepository.findAll();
        assertThat(showDownList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShowDown.class);
        ShowDown showDown1 = new ShowDown();
        showDown1.setId(1L);
        ShowDown showDown2 = new ShowDown();
        showDown2.setId(showDown1.getId());
        assertThat(showDown1).isEqualTo(showDown2);
        showDown2.setId(2L);
        assertThat(showDown1).isNotEqualTo(showDown2);
        showDown1.setId(null);
        assertThat(showDown1).isNotEqualTo(showDown2);
    }
}
