package ru.clevertec.auth.server.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.auth.server.integration.BaseIntegrationTest;
import ru.clevertec.auth.server.web.model.AuthRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class AuthControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void authenticateShouldReturnAccessTokenAndOkStatus() {
        AuthRequest request = AuthRequest.builder()
                .username("john_doe")
                .password("password123")
                .build();

        mockMvc.perform(post("/auth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void registerShouldReturnAuthResponseWithNotNullIdAndCorrectUsername() {
        AuthRequest request = AuthRequest.builder()
                .username("user123")
                .password("password123")
                .build();

        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value(request.getUsername()));
    }

    @Test
    @SneakyThrows
    void validateShouldReturnUnauthorizedStatus() {
        String token = "invalid token";

        mockMvc.perform(get("/auth/validate")
                    .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }
}