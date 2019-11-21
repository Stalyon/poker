package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class ParseHistoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParseHistory.class);
        ParseHistory parseHistory1 = new ParseHistory();
        parseHistory1.setId(1L);
        ParseHistory parseHistory2 = new ParseHistory();
        parseHistory2.setId(parseHistory1.getId());
        assertThat(parseHistory1).isEqualTo(parseHistory2);
        parseHistory2.setId(2L);
        assertThat(parseHistory1).isNotEqualTo(parseHistory2);
        parseHistory1.setId(null);
        assertThat(parseHistory1).isNotEqualTo(parseHistory2);
    }
}
