package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class PlayerActionTest {

    @Test
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
