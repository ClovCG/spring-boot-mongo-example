package com.example.mongoback.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.mongoback.dto.GameDTO;
import com.example.mongoback.handler.InternalException;
import com.example.mongoback.handler.InternalExceptionHandler;
import com.example.mongoback.model.Game;
import com.example.mongoback.service.GameServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(value = SpringExtension.class)
@WebMvcTest(controllers = GameController.class)
@Import(InternalExceptionHandler.class)
public class GameControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private GameServiceImpl gameService;

        @MockBean
        private ModelMapper modelMapper;

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
                verify(gameService, times(1)).getAllGames();
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
                verify(gameService, times(1)).getGamesByTitle(title);
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
                verify(gameService, times(1)).getAllGames();
        }

        @Test
        public void whenGetGames_thenReturnServerError() throws Exception {
                String errorMsg = "There was an error while processing the request.";
                doThrow(new InternalException(errorMsg)).when(gameService).getAllGames();

                // When
                ResultActions response = mockMvc.perform(get("/games").contentType(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print());

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
                verify(gameService, times(1)).getGameById(id);

        }

        @Test
        public void givenGameDTO_whenCreateGame_thenReturnCreated() throws Exception {
                // Given
                String id = "1";
                GameDTO gameDetails = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
                Game savedGame = new Game("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
                savedGame.setId(id);

                when(gameService.saveGame(ArgumentMatchers.any())).thenReturn(savedGame);

                // When
                ResultActions response = mockMvc.perform(post("/game")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gameDetails))
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print());

                // Then
                response.andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id", is(savedGame.getId())))
                                .andExpect(jsonPath("$.title", is(savedGame.getTitle())))
                                .andExpect(jsonPath("$.developer", is(savedGame.getDeveloper())))
                                .andExpect(jsonPath("$.publisher", is(savedGame.getPublisher())))
                                .andExpect(jsonPath("$.year", is(savedGame.getYear())))
                                .andExpect(jsonPath("$.platforms.size()", is(savedGame.getPlatforms().size())));
                verify(gameService, times(1)).saveGame(ArgumentMatchers.any());
        }

        @Test
        public void givenGameDTO_whenCreateGame_thenReturnServerError() throws JsonProcessingException, Exception {
                // Given
                String errorMsg = "There was an error while processing the request.";
                GameDTO gameDetails = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));

                doThrow(new InternalException(errorMsg)).when(gameService).saveGame(gameDetails);

                // When
                ResultActions response = mockMvc.perform(post("/game")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gameDetails))
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print());

                // Then
                response.andExpect(result -> assertTrue(result.getResolvedException() instanceof InternalException))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        public void givenIdAndGameDTO_whenUpdateGame_thenReturnOk() throws Exception {
                // Given
                String id = "1";
                GameDTO gameGetDTO = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
                GameDTO gameDetails = new GameDTO("Jump, Go and Shout", "Dev_1", "Publisher_1", "1990",
                                List.of("Platform"));
                Game savedGame = new Game("Jump, Go and Shout", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
                GameDTO savedGameDTO = new GameDTO("Jump, Go and Shout", "Dev_1", "Publisher_1", "1990",
                                List.of("Platform"));
                savedGame.setId(id);

                when(gameService.getGameById(id)).thenReturn(gameGetDTO);
                when(gameService.saveGame(ArgumentMatchers.any())).thenReturn(savedGame);
                when(modelMapper.map(savedGame, GameDTO.class)).thenReturn(savedGameDTO);

                // When
                ResultActions response = mockMvc.perform(put("/game/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gameDetails))
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print());

                // Then
                response.andExpect(status().isOk())
                                .andExpect(jsonPath("$.title", is(savedGame.getTitle())))
                                .andExpect(jsonPath("$.developer", is(savedGame.getDeveloper())))
                                .andExpect(jsonPath("$.publisher", is(savedGame.getPublisher())))
                                .andExpect(jsonPath("$.year", is(savedGame.getYear())))
                                .andExpect(jsonPath("$.platforms.size()", is(savedGame.getPlatforms().size())));
                verify(gameService, times(1)).getGameById(id);
                verify(gameService, times(1)).saveGame(ArgumentMatchers.any());
        }

        @Test
        public void givenIdAndGameDTO_whenUpdateGame_thenReturnNotFound() throws Exception {
                // Given
                String id = "1";
                GameDTO gameDetails = new GameDTO("Jump, Go and Shout", "Dev_1", "Publisher_1", "1990",
                                List.of("Platform"));

                when(gameService.getGameById(id)).thenReturn(null);

                // When
                ResultActions response = mockMvc.perform(put("/game/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gameDetails))
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print());

                // Then
                response.andExpect(status().isNotFound());
                verify(gameService, times(1)).getGameById(id);

        }

        @Test
        public void givenIdAndGameDTO_whenUpdateGame_thenReturnServerError() throws Exception {
                // Given
                String id = "1";
                String errorMsg = "There was an error while processing the request.";
                GameDTO gameDetails = new GameDTO("Jump, Go and Shout", "Dev_1", "Publisher_1", "1990",
                                List.of("Platform"));

                doThrow(new InternalException(errorMsg)).when(gameService).getGameById(id);

                // When
                ResultActions response = mockMvc.perform(put("/game/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gameDetails))
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print());

                // Then
                response.andExpect(result -> assertTrue(result.getResolvedException() instanceof InternalException))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        public void givenId_whenDeleteGame_thenReturnNoContent() throws Exception {
                // Given
                String id = "1";
                GameDTO gameGetDTO = new GameDTO("Jump and Go", "Dev_1", "Publisher_1", "1990", List.of("Platform"));

                when(gameService.getGameById(id)).thenReturn(gameGetDTO);

                // When
                ResultActions response = mockMvc.perform(delete("/game/{id}", id))
                                .andDo(MockMvcResultHandlers.print());

                // Then
                response.andExpect(status().isNoContent());
                verify(gameService, times(1)).getGameById(id);
                verify(gameService, times(1)).deleteGame(gameGetDTO);
        }

        @Test
        public void givenId_whenDeleteGame_thenReturnNotFound() throws Exception {
                // Given
                String id = "1";

                when(gameService.getGameById(id)).thenReturn(null);

                // When
                ResultActions response = mockMvc.perform(delete("/game/{id}", id))
                                .andDo(MockMvcResultHandlers.print());

                // Then
                response.andExpect(status().isNotFound());
                verify(gameService, times(1)).getGameById(id);
        }

        @Test
        public void givenId_whenDeleteGame_thenReturnServerError() throws Exception {
                // Given
                String id = "1";
                String errorMsg = "There was an error while processing the request.";

                doThrow(new InternalException(errorMsg)).when(gameService).getGameById(id);

                // When
                ResultActions response = mockMvc.perform(delete("/game/{id}", id))
                                .andDo(MockMvcResultHandlers.print());

                // Then
                response.andExpect(result -> assertTrue(result.getResolvedException() instanceof InternalException))
                                .andExpect(status().isInternalServerError());
        }

}
