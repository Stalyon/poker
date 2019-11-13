package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class GameHistoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameHistory.class);
        GameHistory gameHistory1 = new GameHistory();
        gameHistory1.setId(1L);
        GameHistory gameHistory2 = new GameHistory();
        gameHistory2.setId(gameHistory1.getId());
        assertThat(gameHistory1).isEqualTo(gameHistory2);
        gameHistory2.setId(2L);
        assertThat(gameHistory1).isNotEqualTo(gameHistory2);
        gameHistory1.setId(null);
        assertThat(gameHistory1).isNotEqualTo(gameHistory2);
    }
}
