package com.example.mongoback.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.mongoback.dto.GameDTO;
import com.example.mongoback.model.Game;
import com.example.mongoback.service.GameServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameServiceImpl gameService;

    @MockBean
    private ModelMapper mapper;

    @Test
    public void whenGetGames_thenReturnOk() throws Exception {
        // Mocks
        List<GameDTO> gameDTOs = new ArrayList<GameDTO>();
        GameDTO gameDto1 = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
        GameDTO gameDto2 = new GameDTO("Jump and Shout", "Dev_1", "Publisher_1", "1991", List.of("Platform"));
        gameDTOs.add(gameDto1);
        gameDTOs.add(gameDto2);

        when(gameService.getAllGames()).thenReturn(gameDTOs);

        // When
        ResultActions response = mockMvc.perform(get("/games").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.size()", is(gameDTOs.size())));
    }

    @Test
    public void whenGetGames_thenReturnNoContent() throws Exception {
        // Mocks
        List<GameDTO> gameDTOs = new ArrayList<GameDTO>();

        when(gameService.getAllGames()).thenReturn(gameDTOs);

        // When
        ResultActions response = mockMvc.perform(get("/games").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        // Then
        response.andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void givenId_whenGetGameById_thenReturnOk() throws Exception {
        // Given
        String id = "1";
        Game game = new Game("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
        game.setId(id);
        GameDTO gameDto = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));

        when(gameService.getGameById("1")).thenReturn(gameDto);

        // When
        ResultActions response = mockMvc.perform(get("/game/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(gameDto.getTitle())))
                .andExpect(jsonPath("$.developer", is(gameDto.getDeveloper())))
                .andExpect(jsonPath("$.publisher", is(gameDto.getPublisher())))
                .andExpect(jsonPath("$.year", is(gameDto.getYear())))
                .andExpect(jsonPath("$.platforms.size()", is(gameDto.getPlatforms().size())));

    }

}
