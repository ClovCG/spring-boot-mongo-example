package com.example.mongoback.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Game {

    @Id
    private String id;
    private String title;
    private String developer;
    private String publisher;
    private String year;
    private String[] platforms;
}
