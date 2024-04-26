package com.example.mongoback.service;

import java.util.List;

import com.example.mongoback.dto.GameDTO;
import com.example.mongoback.model.Game;

public interface GameService {

    public List<GameDTO> getAllGames();

    public GameDTO getGameById(String id);

    public List<GameDTO> getGamesByTitle(String title);

    public Game saveGame(GameDTO game);

    public void deleteGame(GameDTO game);

}
