package ru.clevertec.newsresource.integration.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsresource.builder.impl.NewsDtoTestBuilder;
import ru.clevertec.newsresource.builder.impl.UserTestBuilder;
import ru.clevertec.newsresource.integration.BaseIntegrationTest;
import ru.clevertec.newsresource.integration.WireMockExtension;
import ru.clevertec.newsresource.service.TokenService;
import ru.clevertec.newsresource.web.dto.NewsDto;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith({WireMockExtension.class, MockitoExtension.class})
class NewsControllerTest extends BaseIntegrationTest {

    private static final Long NEWS_ID = 1L;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @Test
    @SneakyThrows
    void findAllByPageableAndMatchWithQueryShouldReturnCorrectCountOfNews() {
        String query = "new";
        int expectedLength = 9;

        mockMvc.perform(get("/api/v0/news?query=" + query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedLength));
    }

    @Test
    @SneakyThrows
    void findNewsById() {
        mockMvc.perform(get("/api/v0/news/" + NEWS_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(NEWS_ID));
    }

    @Test
    @SneakyThrows
    void saveNewsShouldReturnNewsWithNotNullId() {
        String token = "token";
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("ADMIN")))
                .build();
        NewsDto newsDto = NewsDtoTestBuilder.aNewsDto()
                .withTitle("New news")
                .withText("New text")
                .build();
        stubFor(WireMock.get(urlEqualTo("/auth/validate"))
                .withHeader("Authorization", equalTo("Bearer " + token))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())));
        doReturn(user).when(tokenService).getUserInfoFromToken(token);

        mockMvc.perform(post("/api/v0/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsDto))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void updateNewsPartiallyByIdShouldReturnNoContentStatus() {
        String token = "token";
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("ADMIN")))
                .build();
        NewsDto newsDto = NewsDtoTestBuilder.aNewsDto()
                .withTitle("New news")
                .withText("New text")
                .build();
        stubFor(WireMock.get(urlEqualTo("/auth/validate"))
                .withHeader("Authorization", equalTo("Bearer " + token))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())));
        doReturn(user).when(tokenService).getUserInfoFromToken(token);

        mockMvc.perform(patch("/api/v0/news/" + NEWS_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsDto))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    void deleteNewsByIdShouldReturnNoContentStatus() {
        String token = "token";
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("ADMIN")))
                .build();
        stubFor(WireMock.get(urlEqualTo("/auth/validate"))
                .withHeader("Authorization", equalTo("Bearer " + token))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())));
        doReturn(user).when(tokenService).getUserInfoFromToken(token);

        mockMvc.perform(delete("/api/v0/news/" + NEWS_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}