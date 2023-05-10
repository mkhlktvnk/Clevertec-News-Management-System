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
import ru.clevertec.newsresource.builder.impl.CommentDtoTestBuilder;
import ru.clevertec.newsresource.builder.impl.UserTestBuilder;
import ru.clevertec.newsresource.integration.BaseIntegrationTest;
import ru.clevertec.newsresource.integration.WireMockExtension;
import ru.clevertec.newsresource.security.constant.AuthConstant;
import ru.clevertec.newsresource.jwt.JwtParser;
import ru.clevertec.newsresource.web.dto.CommentDto;

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
class CommentControllerTest extends BaseIntegrationTest {

    private static final Long NEWS_ID = 1L;
    private static final Long COMMENT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtParser jwtParser;

    @Test
    @SneakyThrows
    void findAllByNewsIdAndPageableAndMatchWithQueryShouldReturnCorrectCountOfComments() {
        String query = "i totally agree";
        int expectedLength = 1;

        mockMvc.perform(get("/api/v0/news/" + NEWS_ID + "/comments?query=" + query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedLength));
    }

    @Test
    @SneakyThrows
    void findCommentByIdShouldReturnCommentWithCorrectId() {
        mockMvc.perform(get("/api/v0/comments/" + COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(COMMENT_ID));
    }

    @Test
    @SneakyThrows
    void addCommentToNewsShouldReturnCommentWithNotNullId() {
        String token = "token";
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withPassword("password")
                .withRoles(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();
        CommentDto commentDto = CommentDtoTestBuilder.aCommentDto()
                .withNewsId(1L)
                .withText("New comment")
                .build();
        stubFor(WireMock.get(urlEqualTo("/auth/validate"))
                .withHeader(AuthConstant.AUTHORIZATION, equalTo(token))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())));
        doReturn(user).when(jwtParser).getUserInfoFromToken(token);

        mockMvc.perform(post("/api/v0/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header(AuthConstant.AUTHORIZATION, AuthConstant.BEARER + token))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void updateCommentPartiallyByIdShouldReturnNoContentStatus() {
        String token = "token";
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();
        CommentDto commentDto = CommentDtoTestBuilder.aCommentDto()
                .withNewsId(1L)
                .withText("New comment name")
                .build();
        stubFor(WireMock.get(urlEqualTo("/auth/validate"))
                .withHeader(AuthConstant.AUTHORIZATION, equalTo(token))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())));
        doReturn(user).when(jwtParser).getUserInfoFromToken(token);

        mockMvc.perform(patch("/api/v0/comments/" + COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header(AuthConstant.AUTHORIZATION, AuthConstant.BEARER + token))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void deleteCommentByIdShouldReturnNoContentStatus() {
        String token = "token";
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();

        stubFor(WireMock.get(urlEqualTo("/auth/validate"))
                .withHeader(AuthConstant.AUTHORIZATION, equalTo(token))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())));
        doReturn(user).when(jwtParser).getUserInfoFromToken(token);

        mockMvc.perform(delete("/api/v0/comments/" + COMMENT_ID)
                        .header(AuthConstant.AUTHORIZATION, AuthConstant.BEARER + token))
                .andExpect(status().isNoContent());
    }
}