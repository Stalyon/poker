package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class TournoiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tournoi.class);
        Tournoi tournoi1 = new Tournoi();
        tournoi1.setId(1L);
        Tournoi tournoi2 = new Tournoi();
        tournoi2.setId(tournoi1.getId());
        assertThat(tournoi1).isEqualTo(tournoi2);
        tournoi2.setId(2L);
        assertThat(tournoi1).isNotEqualTo(tournoi2);
        tournoi1.setId(null);
        assertThat(tournoi1).isNotEqualTo(tournoi2);
    }
}
