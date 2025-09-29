package com.martinhacker.jsonplaceholder.service;

import com.martinhacker.jsonplaceholder.model.Comment;
import com.martinhacker.jsonplaceholder.model.Post;
import com.martinhacker.jsonplaceholder.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonPlaceholderServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JsonPlaceholderService jsonPlaceholderService;

    private Post samplePost;
    private User sampleUser;
    private Comment sampleComment;

    @BeforeEach
    void setUp() {
        // Configurar URL base para testing
        jsonPlaceholderService = new JsonPlaceholderService(restTemplate, "https://jsonplaceholder.typicode.com");
        
        // Crear datos de muestra
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Leanne Graham");
        sampleUser.setUsername("Bret");
        sampleUser.setEmail("Sincere@april.biz");

        sampleComment = new Comment();
        sampleComment.setId(1L);
        sampleComment.setPostId(1L);
        sampleComment.setName("id labore ex et quam laborum");
        sampleComment.setEmail("Eliseo@gardner.biz");
        sampleComment.setBody("laudantium enim quasi est quidem magnam voluptate ipsam eos...");

        samplePost = new Post();
        samplePost.setId(1L);
        samplePost.setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
        samplePost.setBody("quia et suscipit...");
        samplePost.setUserId(1L);
    }

    @Test
    void getPosts_ShouldReturnListOfPosts() {
        // Arrange
        List<Post> expectedPosts = Arrays.asList(samplePost);
        ResponseEntity<List<Post>> responseEntity = new ResponseEntity<>(expectedPosts, HttpStatus.OK);
        
        when(restTemplate.exchange(
                eq("https://jsonplaceholder.typicode.com/posts"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<Post> result = jsonPlaceholderService.getPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(samplePost.getId(), result.get(0).getId());
        assertEquals(samplePost.getTitle(), result.get(0).getTitle());
    }

    @Test
    void getPostById_ShouldReturnPost() {
        // Arrange
        when(restTemplate.getForObject(
                eq("https://jsonplaceholder.typicode.com/posts/1"),
                eq(Post.class)
        )).thenReturn(samplePost);

        // Act
        Post result = jsonPlaceholderService.getPostById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(samplePost.getId(), result.getId());
        assertEquals(samplePost.getTitle(), result.getTitle());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Arrange
        when(restTemplate.getForObject(
                eq("https://jsonplaceholder.typicode.com/users/1"),
                eq(User.class)
        )).thenReturn(sampleUser);

        // Act
        User result = jsonPlaceholderService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(sampleUser.getId(), result.getId());
        assertEquals(sampleUser.getName(), result.getName());
    }

    @Test
    void getCommentsByPostId_ShouldReturnComments() {
        // Arrange
        List<Comment> expectedComments = Arrays.asList(sampleComment);
        ResponseEntity<List<Comment>> responseEntity = new ResponseEntity<>(expectedComments, HttpStatus.OK);
        
        when(restTemplate.exchange(
                eq("https://jsonplaceholder.typicode.com/posts/1/comments"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<Comment> result = jsonPlaceholderService.getCommentsByPostId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleComment.getId(), result.get(0).getId());
        assertEquals(sampleComment.getPostId(), result.get(0).getPostId());
    }

    @Test
    void deletePost_ShouldReturnTrue() {
        // Arrange
        doNothing().when(restTemplate).delete("https://jsonplaceholder.typicode.com/posts/1");

        // Act
        boolean result = jsonPlaceholderService.deletePost(1L);

        // Assert
        assertTrue(result);
        verify(restTemplate).delete("https://jsonplaceholder.typicode.com/posts/1");
    }

    @Test
    void getPosts_WhenApiThrowsException_ShouldThrowRuntimeException() {
        // Arrange
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            jsonPlaceholderService.getPosts();
        });
    }

    @Test
    void getPostById_WhenPostNotFound_ShouldReturnNull() {
        // Arrange
        when(restTemplate.getForObject(
                eq("https://jsonplaceholder.typicode.com/posts/999"),
                eq(Post.class)
        )).thenReturn(null);

        // Act
        Post result = jsonPlaceholderService.getPostById(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void getAllPostsWithDetails_ShouldReturnPostsWithUserAndComments() {
        // Arrange
        List<Post> posts = Arrays.asList(samplePost);
        ResponseEntity<List<Post>> postsResponse = new ResponseEntity<>(posts, HttpStatus.OK);
        ResponseEntity<List<Comment>> commentsResponse = new ResponseEntity<>(Arrays.asList(sampleComment), HttpStatus.OK);
        
        when(restTemplate.exchange(
                eq("https://jsonplaceholder.typicode.com/posts"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(postsResponse);
        
        when(restTemplate.getForObject(
                eq("https://jsonplaceholder.typicode.com/users/1"),
                eq(User.class)
        )).thenReturn(sampleUser);
        
        when(restTemplate.exchange(
                eq("https://jsonplaceholder.typicode.com/posts/1/comments"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(commentsResponse);

        // Act
        List<Post> result = jsonPlaceholderService.getAllPostsWithDetails();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getUser());
        assertNotNull(result.get(0).getComments());
        assertEquals(sampleUser.getId(), result.get(0).getUser().getId());
        assertEquals(1, result.get(0).getComments().size());
    }
}
