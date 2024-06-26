package com.example.mongoback.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mongoback.model.Game;

public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByTitleContains(String title);
}
