package com.example.mongoback.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mongoback.dto.GameDTO;
import com.example.mongoback.model.Game;
import com.example.mongoback.repository.GameRepository;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ModelMapper mapper;

    // GET methods
    @Override
    public List<GameDTO> getAllGames() {
        List<Game> games = gameRepository.findAll();
        return games.stream().map(game -> mapper.map(game, GameDTO.class)).collect(Collectors.toList());
    }

    @Override
    public GameDTO getGameById(String id) {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            return mapper.map(game.get(), GameDTO.class);
        }
        return null;
    }

    @Override
    public List<GameDTO> getGamesByTitle(String title) {
        List<Game> games = gameRepository.findByTitleContains(title);
        return games.stream().map(game -> mapper.map(game, GameDTO.class)).collect(Collectors.toList());
    }

    // POST/UPDATE methods
    @Override
    public Game saveGame(GameDTO gameDetails) {
        Game game = mapper.map(gameDetails, Game.class);
        return gameRepository.save(game);
    }

    // DELETE methods
    @Override
    public void deleteGame(GameDTO game) {
        gameRepository.delete(mapper.map(game, Game.class));
    }
}
