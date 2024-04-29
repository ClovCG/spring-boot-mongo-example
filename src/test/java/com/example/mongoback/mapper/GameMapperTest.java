package com.example.mongoback.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.mongoback.dto.GameDTO;
import com.example.mongoback.model.Game;

@ExtendWith(MockitoExtension.class)
public class GameMapperTest {

    @Spy
    private ModelMapper mapper;

    @Test
    public void givenGame_whenMapToGameDTO_thenEquals() {
        // Given
        Game game = new Game("Tetrimino", "Tetrimino Guy", "Tots", "1984", List.of("Puzzle"));

        // When
        GameDTO gameDto = mapper.map(game, GameDTO.class);

        // Then
        assertEquals(game.getTitle(), gameDto.getTitle());
        assertEquals(game.getDeveloper(), gameDto.getDeveloper());
        assertEquals(game.getPublisher(), gameDto.getPublisher());
        assertEquals(game.getYear(), gameDto.getYear());
        assertIterableEquals(game.getPlatforms(), gameDto.getPlatforms());

    }

    @Test
    public void givenGameDTO_whenMapToGame_thenEquals() {
        // Given
        GameDTO gameDto = new GameDTO("Tetrimino", "Tetrimino Guy", "Tots", "1984", List.of("Puzzle"));

        // When
        Game game = mapper.map(gameDto, Game.class);

        // Then
        assertEquals(gameDto.getTitle(), game.getTitle());
        assertEquals(gameDto.getDeveloper(), game.getDeveloper());
        assertEquals(gameDto.getPublisher(), game.getPublisher());
        assertEquals(gameDto.getYear(), game.getYear());
        assertIterableEquals(gameDto.getPlatforms(), game.getPlatforms());
    }

}
