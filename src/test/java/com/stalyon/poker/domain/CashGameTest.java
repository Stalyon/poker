package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class CashGameTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashGame.class);
        CashGame cashGame1 = new CashGame();
        cashGame1.setId(1L);
        CashGame cashGame2 = new CashGame();
        cashGame2.setId(cashGame1.getId());
        assertThat(cashGame1).isEqualTo(cashGame2);
        cashGame2.setId(2L);
        assertThat(cashGame1).isNotEqualTo(cashGame2);
        cashGame1.setId(null);
        assertThat(cashGame1).isNotEqualTo(cashGame2);
    }
}
