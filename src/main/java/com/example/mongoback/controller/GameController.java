package com.example.mongoback.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.mongoback.dto.GameDTO;
import com.example.mongoback.handler.InternalException;
import com.example.mongoback.model.Game;
import com.example.mongoback.service.GameServiceImpl;

@RestController
public class GameController {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private ModelMapper mapper;

    /*
     * GET requests
     */

    // Get all games or get games containing the title provided
    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getGames(@RequestParam(required = false) String title) {
        try {
            List<GameDTO> games;
            if (title == null || title == "") {
                games = gameService.getAllGames();
            } else {
                games = gameService.getGamesByTitle(title);
            }

            if (games.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(games);
        } catch (Exception e) {
            throw new InternalException("There was an error while processing the request.");
        }
    }

    // Get game by id
    @GetMapping("/game/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable String id) {
        GameDTO game = gameService.getGameById(id);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(game);
    }

    /*
     * POST requests
     */

    // Create new game
    @PostMapping("/game")
    public ResponseEntity<Game> createGame(@RequestBody GameDTO game) {
        try {
            Game newGame = gameService.saveGame(game);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newGame.getId()).toUri();

            return ResponseEntity.created(location).body(newGame);
        } catch (Exception e) {
            throw new InternalException("There was an error while processing the request.");
        }
    }

    /*
     * PUT requests
     */

    // Update game by id
    @PutMapping("/game/{id}")
    public ResponseEntity<GameDTO> updateGame(@PathVariable String id, @RequestBody GameDTO gameDetails) {
        GameDTO game = gameService.getGameById(id);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Game updatedGame = gameService.saveGame(gameDetails);
            return ResponseEntity.ok(mapper.map(updatedGame, GameDTO.class));
        } catch (Exception e) {
            throw new InternalException("There was an error while processing the request.");
        }
    }

    /*
     * DELETE requests
     */

    // Delete game by id
    @DeleteMapping("/game/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteGame(@PathVariable String id) {
        GameDTO game = gameService.getGameById(id);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            gameService.deleteGame(game);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new InternalException("There was an error while processing the request.");
        }
    }
}
