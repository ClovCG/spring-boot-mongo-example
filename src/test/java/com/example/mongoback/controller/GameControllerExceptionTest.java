package com.example.mongoback.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.mongoback.handler.TestExceptionHandler;
import com.example.mongoback.service.GameServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
@ContextConfiguration(classes = TestExceptionHandler.class)
public class GameControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameServiceImpl gameService;

    @Test
    public void whenGetGames_thenReturnServerError() throws Exception {
        // When
        ResultActions response = mockMvc.perform(get("/games").contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(result -> assertTrue(result.getResolvedException() instanceof Exception))
                .andExpect(status().isInternalServerError());
    }

}
