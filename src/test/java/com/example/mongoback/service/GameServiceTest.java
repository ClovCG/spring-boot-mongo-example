package com.example.mongoback.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.mongoback.dto.GameDTO;
import com.example.mongoback.model.Game;
import com.example.mongoback.repository.GameRepository;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private GameRepository gameRepository;

    @Spy
    private ModelMapper mapper;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAllGames_thenReturnList() {
        // Given
        List<Game> games = new ArrayList<Game>();
        List<GameDTO> gameDTOs = new ArrayList<GameDTO>();
        Game game1 = new Game("Game_1", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
        game1.setId("1");
        Game game2 = new Game("Game_2", "Dev_2", "Publisher_2", "1991", List.of("Platform"));
        game2.setId("2");
        GameDTO gameDto1 = new GameDTO("Game_1", "Dev_1", "Publisher_1", "1990", List.of("Platform"));
        GameDTO gameDto2 = new GameDTO("Game_2", "Dev_2", "Publisher_2", "1991", List.of("Platform"));
        games.add(game1);
        games.add(game2);
        gameDTOs.add(gameDto1);
        gameDTOs.add(gameDto2);

        // Mocks
        when(gameRepository.findAll()).thenReturn(games);
        doReturn(gameDto1).when(mapper).map(eq(game1), eq(GameDTO.class));
        doReturn(gameDto2).when(mapper).map(eq(game2), eq(GameDTO.class));

        // When
        List<GameDTO> responseGameDTOs = gameService.getAllGames();

        // Then
        assertNotNull(responseGameDTOs);
        assertEquals(gameDTOs.size(), responseGameDTOs.size());
        assertIterableEquals(gameDTOs, responseGameDTOs);
    }

    @Test
    public void givenGameDTO_whenSaveGame_thenReturnGame() {
        // Given
        GameDTO gameDetails = new GameDTO("Tetrimino", "Tetrimino Guy", "Tots", "1984", List.of("Puzzle"));
        Game game = new Game("Tetrimino", "Tetrimino Guy", "Tots", "1984", List.of("Puzzle"));
        game.setId("1");
        Game savedGame = new Game("Tetrimino", "Tetrimino Guy", "Tots", "1984", List.of("Puzzle"));
        game.setId("1");

        // Mocks
        when(mapper.map(gameDetails, Game.class)).thenReturn(game);
        when(gameRepository.save(game)).thenReturn(savedGame);

        // When
        Game responseGame = gameService.saveGame(gameDetails);

        // Then
        assertNotNull(responseGame);
        assertEquals(savedGame.getId(), responseGame.getId());
        assertEquals(savedGame.getTitle(), responseGame.getTitle());
        assertEquals(savedGame.getDeveloper(), responseGame.getDeveloper());
        assertEquals(savedGame.getPublisher(), responseGame.getPublisher());
        assertEquals(savedGame.getYear(), responseGame.getYear());
        assertIterableEquals(savedGame.getPlatforms(), responseGame.getPlatforms());
    }

}
