package com.example.mongoback.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.mongoback.model.Game;
import com.example.mongoback.service.GameService;

@RestController
@RequestMapping("/")
public class GameController {

    @Autowired
    private GameService gameService;

    /*
     * GET requests
     */

    // Get all games
    @GetMapping("/games")
    public ResponseEntity<List<Game>> getGames() {
        try {
            List<Game> games = gameService.getAllGames();
            if (games.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(games);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get game by id
    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Optional<Game> game = gameService.getGameById(id);

        if (!game.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(game.get());
    }

    // Get games containing the title provided
    @GetMapping("/games/{title}")
    public ResponseEntity<List<Game>> getGamesByTitle(@PathVariable String title) {
        try {
            List<Game> games = gameService.getGamesbyTitle(title);
            if (games.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(games);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
     * POST requests
     */

    // Create new game
    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        try {
            Game newGame = gameService.saveGame(game);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newGame.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
     * PUT requests
     */

    // Update game by id
    @PutMapping("/games/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable String id, @RequestBody Game gameDetails) {
        Optional<Game> game = gameService.getGameById(id);

        if (!game.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Game updatedGame = gameService.saveGame(gameDetails);
            return ResponseEntity.ok(updatedGame);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
     * DELETE requests
     */

    // Delete game by id
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteGame(@PathVariable String id) {
        Optional<Game> game = gameService.getGameById(id);

        if (!game.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            gameService.deleteGame(game.get());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
