package com.example.mongoback.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    public void givenGameWithNullProperties_whenMapToGameDTO_thenEquals() {
        // Given
        Game game = new Game();

        // When
        GameDTO gameDto = mapper.map(game, GameDTO.class);

        // Then (assert null properties, then assert equals)
        assertNull(game.getTitle());
        assertEquals(game.getTitle(), gameDto.getTitle());
        assertNull(game.getDeveloper());
        assertEquals(game.getDeveloper(), gameDto.getDeveloper());
        assertNull(game.getPublisher());
        assertEquals(game.getPublisher(), gameDto.getPublisher());
        assertNull(game.getYear());
        assertEquals(game.getYear(), gameDto.getYear());
        assertNull(game.getPlatforms());
        assertIterableEquals(game.getPlatforms(), gameDto.getPlatforms());
    }

    @Test
    public void givenGameDTOWithEmptyParameters_whenMapToGame_thenEquals() {
        // Given
        GameDTO gameDto = new GameDTO();

        // When
        Game game = mapper.map(gameDto, Game.class);

        // Then (assert null properties, then assert equals)
        assertNull(gameDto.getTitle());
        assertEquals(gameDto.getTitle(), game.getTitle());
        assertNull(gameDto.getDeveloper());
        assertEquals(gameDto.getDeveloper(), game.getDeveloper());
        assertNull(gameDto.getPublisher());
        assertEquals(gameDto.getPublisher(), game.getPublisher());
        assertNull(gameDto.getYear());
        assertEquals(gameDto.getYear(), game.getYear());
        assertNull(gameDto.getPlatforms());
        assertIterableEquals(gameDto.getPlatforms(), game.getPlatforms());
    }

    @Test
    public void givenGameWithEmptyProperties_whenMapToGameDTO_thenEquals() {
        // Given
        Game game = new Game("", "", "", "", List.of(""));

        // When
        GameDTO gameDto = mapper.map(game, GameDTO.class);

        // Then (assert empty strings, then assert equals)
        assertEquals(game.getTitle(), "");
        assertEquals(game.getTitle(), gameDto.getTitle());
        assertEquals(game.getDeveloper(), "");
        assertEquals(game.getDeveloper(), gameDto.getDeveloper());
        assertEquals(game.getPublisher(), "");
        assertEquals(game.getPublisher(), gameDto.getPublisher());
        assertEquals(game.getYear(), "");
        assertEquals(game.getYear(), gameDto.getYear());
        assertArrayEquals(game.getPlatforms().toArray(), List.of("").toArray());
        assertIterableEquals(game.getPlatforms(), gameDto.getPlatforms());
    }

    @Test
    public void givenGameDTOWithEmptyProperties_whenMapToGame_thenEquals() {
        // Given
        GameDTO gameDto = new GameDTO("", "", "", "", List.of(""));

        // When
        Game game = mapper.map(gameDto, Game.class);

        // Then (assert empty strings, then assert equals)
        assertEquals(gameDto.getTitle(), "");
        assertEquals(gameDto.getTitle(), game.getTitle());
        assertEquals(gameDto.getDeveloper(), "");
        assertEquals(gameDto.getDeveloper(), game.getDeveloper());
        assertEquals(gameDto.getPublisher(), "");
        assertEquals(gameDto.getPublisher(), game.getPublisher());
        assertEquals(gameDto.getYear(), "");
        assertEquals(gameDto.getYear(), game.getYear());
        assertArrayEquals(gameDto.getPlatforms().toArray(), List.of("").toArray());
        assertIterableEquals(gameDto.getPlatforms(), game.getPlatforms());
    }

}
