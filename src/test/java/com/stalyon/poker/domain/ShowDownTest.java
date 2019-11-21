package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class ShowDownTest {

    @Test
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
