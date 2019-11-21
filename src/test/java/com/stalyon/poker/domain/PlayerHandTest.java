package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class PlayerHandTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerHand.class);
        PlayerHand playerHand1 = new PlayerHand();
        playerHand1.setId(1L);
        PlayerHand playerHand2 = new PlayerHand();
        playerHand2.setId(playerHand1.getId());
        assertThat(playerHand1).isEqualTo(playerHand2);
        playerHand2.setId(2L);
        assertThat(playerHand1).isNotEqualTo(playerHand2);
        playerHand1.setId(null);
        assertThat(playerHand1).isNotEqualTo(playerHand2);
    }
}
