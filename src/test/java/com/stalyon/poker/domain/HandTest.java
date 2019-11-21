package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class HandTest {

    @Test
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
