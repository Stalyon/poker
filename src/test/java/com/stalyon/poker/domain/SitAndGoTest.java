package com.stalyon.poker.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.stalyon.poker.web.rest.TestUtil;

public class SitAndGoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SitAndGo.class);
        SitAndGo sitAndGo1 = new SitAndGo();
        sitAndGo1.setId(1L);
        SitAndGo sitAndGo2 = new SitAndGo();
        sitAndGo2.setId(sitAndGo1.getId());
        assertThat(sitAndGo1).isEqualTo(sitAndGo2);
        sitAndGo2.setId(2L);
        assertThat(sitAndGo1).isNotEqualTo(sitAndGo2);
        sitAndGo1.setId(null);
        assertThat(sitAndGo1).isNotEqualTo(sitAndGo2);
    }
}
