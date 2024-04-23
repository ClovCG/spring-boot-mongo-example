package com.example.mongoback.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mongoback.model.Game;
import com.example.mongoback.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    // GET methods
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(String id) {
        return gameRepository.findById(id);
    }

    public List<Game> getGamesbyTitle(String title) {
        return gameRepository.findByTitleContains(title);
    }

    // POST/UPDATE methods
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    // DELETE methods
    public void deleteGame(Game game) {
        gameRepository.delete(game);
    }
}
