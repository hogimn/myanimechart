package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.domain.anime.AnimeSeason;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnimeSeasonParseTest {

    @Test
    void parse_acceptsCaseInsensitive() {
        assertThat(AnimeSeason.parse("spring")).isEqualTo(AnimeSeason.SPRING);
        assertThat(AnimeSeason.parse("SUMMER")).isEqualTo(AnimeSeason.SUMMER);
        assertThat(AnimeSeason.parse("  Fall ")).isEqualTo(AnimeSeason.FALL);
    }

    @Test
    void apiValue_isLowercaseMalSeason() {
        assertThat(AnimeSeason.WINTER.apiValue()).isEqualTo("winter");
    }

    @Test
    void parse_rejectsUnknown() {
        assertThatThrownBy(() -> AnimeSeason.parse("autumn"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("spring");
    }

    @Test
    void parse_rejectsBlank() {
        assertThatThrownBy(() -> AnimeSeason.parse(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
