package com.martinhacker.jsonplaceholder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinhacker.jsonplaceholder.model.Comment;
import com.martinhacker.jsonplaceholder.model.Post;
import com.martinhacker.jsonplaceholder.model.User;
import com.martinhacker.jsonplaceholder.service.JsonPlaceholderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonPlaceholderService jsonPlaceholderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Post samplePost;
    private User sampleUser;
    private Comment sampleComment;

    @BeforeEach
    void setUp() {
        // Crear usuario de muestra
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Leanne Graham");
        sampleUser.setUsername("Bret");
        sampleUser.setEmail("Sincere@april.biz");

        // Crear comentario de muestra
        sampleComment = new Comment();
        sampleComment.setId(1L);
        sampleComment.setPostId(1L);
        sampleComment.setName("id labore ex et quam laborum");
        sampleComment.setEmail("Eliseo@gardner.biz");
        sampleComment.setBody("laudantium enim quasi est quidem magnam voluptate ipsam eos...");

        // Crear post de muestra
        samplePost = new Post();
        samplePost.setId(1L);
        samplePost.setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
        samplePost.setBody("quia et suscipit...");
        samplePost.setUserId(1L);
        samplePost.setUser(sampleUser);
        samplePost.setComments(Arrays.asList(sampleComment));
    }

    @Test
    void getAllPosts_ShouldReturnPostsWithDetails() throws Exception {
        // Arrange
        List<Post> posts = Arrays.asList(samplePost);
        when(jsonPlaceholderService.getAllPostsWithDetails()).thenReturn(posts);

        // Act & Assert
        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"))
                .andExpect(jsonPath("$.data[0].user.id").value(1))
                .andExpect(jsonPath("$.data[0].user.name").value("Leanne Graham"))
                .andExpect(jsonPath("$.data[0].comments").isArray())
                .andExpect(jsonPath("$.data[0].comments[0].id").value(1));
    }

    @Test
    void getPostById_ShouldReturnPost() throws Exception {
        // Arrange
        when(jsonPlaceholderService.getPostById(1L)).thenReturn(samplePost);

        // Act & Assert
        mockMvc.perform(get("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"))
                .andExpect(jsonPath("$.data.user.id").value(1));
    }

    @Test
    void getPostById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/posts/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPostById_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/posts/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePost_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(jsonPlaceholderService.deletePost(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Post con ID 1 eliminado exitosamente"));
    }

    @Test
    void deletePost_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/posts/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePost_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/posts/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
