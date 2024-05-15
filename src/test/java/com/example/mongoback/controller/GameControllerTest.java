package com.example.mongoback.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.mongoback.dto.GameDTO;
import com.example.mongoback.handler.InternalException;
import com.example.mongoback.handler.InternalExceptionHandler;
import com.example.mongoback.model.Game;
import com.example.mongoback.service.GameServiceImpl;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = GameController.class)
@Import(InternalExceptionHandler.class)
public class GameControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private GameServiceImpl gameService;

        @MockBean
        private ModelMapper mapper;

        @Test
        public void whenGetGames_thenReturnOk() throws Exception {
                // Mocks
                List<GameDTO> gameDTOs = new ArrayList<GameDTO>();
                GameDTO gameDto1 = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
                GameDTO gameDto2 = new GameDTO("Shout", "Dev_1", "Publisher_1", "1991", List.of("Platform"));
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
        public void givenTitle_whenGetGames_thenReturnOk() throws Exception {
                // Given
                String title = "Jump";

                // Mocks
                List<GameDTO> gameDTOs = new ArrayList<GameDTO>();
                GameDTO gameDto1 = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
                GameDTO gameDto2 = new GameDTO("Jump and Shout", "Dev_1", "Publisher_1", "1991", List.of("Platform"));
                gameDTOs.add(gameDto1);
                gameDTOs.add(gameDto2);

                when(gameService.getGamesByTitle(title)).thenReturn(gameDTOs);

                // When
                ResultActions response = mockMvc.perform(get("/games", title)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("title", title))
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
        public void whenGetGames_thenReturnServerError() throws Exception {
                // Mocks (set mockMvc so it acts as if an exception is being thrown)
                mockMvc = MockMvcBuilders.standaloneSetup(GameController.class)
                                .setControllerAdvice(new InternalExceptionHandler()).build();

                // When
                ResultActions response = mockMvc.perform(get("/games").contentType(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print());

                Exception ex = response.andReturn().getResolvedException();
                // Then
                response.andExpect(result -> assertTrue(result.getResolvedException() instanceof InternalException))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        public void givenId_whenGetGameById_thenReturnOk() throws Exception {
                // Given
                String id = "1";
                Game game = new Game("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
                game.setId(id);
                GameDTO gameDto = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));

                when(gameService.getGameById(id)).thenReturn(gameDto);

                // When
                ResultActions response = mockMvc.perform(get("/game/{id}", id)
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

        @Test
        public void givenGameDTO_whenCreateGame_thenReturnOk() {

        }

        @Test
        public void givenGameDTO_whenCreateGame_thenReturnServerError() {

        }

        @Test
        public void givenId_whenUpdateGame_thenReturnOk() {

        }

        @Test
        public void givenId_whenUpdateGame_thenReturnNotFound() {

        }

        @Test
        public void givenId_whenUpdateGame_thenReturnServerError() {

        }

        @Test
        public void givenId_whenDeleteGame_thenReturnNoContent() {

        }

        @Test
        public void givenId_whenDeleteGame_thenReturnNotFound() {

        }

        @Test
        public void givenId_whenDeleteGame_thenReturnServerError() {

        }

}
