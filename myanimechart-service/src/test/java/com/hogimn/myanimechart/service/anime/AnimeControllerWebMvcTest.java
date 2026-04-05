package com.hogimn.myanimechart.service.anime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = AnimeControllerMvcSliceTestApplication.class,
        properties = "spring.cloud.discovery.enabled=false")
@AutoConfigureMockMvc
class AnimeControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeService animeService;

    @Test
    void getByKeyword_blankParam_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/anime").param("keyword", "   "))
                .andExpect(status().isBadRequest());
        verify(animeService, never()).getByKeyword(anyString());
    }

    @Test
    void getByKeyword_missingParam_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/anime"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByYearAndSeason_invalidSeason_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/anime/by-year-and-season/2024/notaseason"))
                .andExpect(status().isBadRequest());
        verify(animeService, never()).getByYearAndSeason(anyInt(), anyString());
    }

    @Test
    void getByYearAndSeason_yearOutOfRange_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/anime/by-year-and-season/1800/spring"))
                .andExpect(status().isBadRequest());
        verify(animeService, never()).getByYearAndSeason(anyInt(), anyString());
    }
}
