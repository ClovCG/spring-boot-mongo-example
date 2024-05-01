package com.example.mongoback.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @Mock
    private ModelMapper mapper;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenGameDTO_whenSaveGame_thenReturnGame() {
        // Given
        GameDTO gameDetails = new GameDTO("Tetrimino", "Tetrimino Guy", "Tots", "1984", List.of("Puzzle"));
        Game game = new Game("Tetrimino", "Tetrimino Guy", "Tots", "1984", List.of("Puzzle"));
        Game savedGame = new Game("Tetrimino", "Tetrimino Guy", "Tots", "1984", List.of("Puzzle"));

        // Mocks
        when(mapper.map(gameDetails, Game.class)).thenReturn(game);
        when(gameRepository.save(game)).thenReturn(savedGame);

        // When
        Game responseGame = gameService.saveGame(gameDetails);

        // Then
        assertEquals(responseGame.getTitle(), savedGame.getTitle());
        assertEquals(responseGame.getDeveloper(), savedGame.getDeveloper());
        assertEquals(responseGame.getPublisher(), savedGame.getPublisher());
        assertEquals(responseGame.getYear(), savedGame.getYear());
        assertIterableEquals(responseGame.getPlatforms(), savedGame.getPlatforms());
    }

}
