package com.martinhacker.jsonplaceholder.service;

import com.martinhacker.jsonplaceholder.model.Comment;
import com.martinhacker.jsonplaceholder.model.Post;
import com.martinhacker.jsonplaceholder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class JsonPlaceholderService {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonPlaceholderService.class);
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ExecutorService executorService;
    
    public JsonPlaceholderService(RestTemplate restTemplate, 
                                  @Value("${external.api.jsonplaceholder.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.executorService = Executors.newFixedThreadPool(10);
    }
    
    /**
     * Obtiene todos los posts con información completa de usuarios y comentarios
     */
    public List<Post> getAllPostsWithDetails() {
        logger.info("Obteniendo todos los posts con detalles completos");
        
        try {
            // Obtener posts
            List<Post> posts = getPosts();
            logger.info("Se obtuvieron {} posts", posts.size());
            
            // Procesar cada post de forma asíncrona para obtener usuarios y comentarios
            List<CompletableFuture<Void>> futures = posts.stream()
                    .map(post -> CompletableFuture.runAsync(() -> {
                        try {
                            // Obtener usuario y comentarios en paralelo
                            CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> getUserById(post.getUserId()));
                            CompletableFuture<List<Comment>> commentsFuture = CompletableFuture.supplyAsync(() -> getCommentsByPostId(post.getId()));
                            
                            // Esperar a que ambas operaciones terminen
                            CompletableFuture.allOf(userFuture, commentsFuture).join();
                            
                            // Asignar los datos al post
                            post.setUser(userFuture.get());
                            post.setComments(commentsFuture.get());
                            
                            logger.debug("Post {} procesado exitosamente", post.getId());
                        } catch (Exception e) {
                            logger.error("Error procesando post {}: {}", post.getId(), e.getMessage());
                        }
                    }, executorService))
                    .toList();
            
            // Esperar a que todos los posts sean procesados
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            
            logger.info("Procesamiento de posts completado exitosamente");
            return posts;
            
        } catch (Exception e) {
            logger.error("Error obteniendo posts con detalles: {}", e.getMessage());
            throw new RuntimeException("Error obteniendo posts con detalles", e);
        }
    }
    
    /**
     * Obtiene todos los posts
     */
    @Cacheable(value = "posts", key = "'all'")
    public List<Post> getPosts() {
        logger.info("Obteniendo lista de posts desde API externa");
        
        try {
            String url = baseUrl + "/posts";
            ResponseEntity<List<Post>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Post>>() {}
            );
            
            logger.info("Posts obtenidos exitosamente: {}", response.getBody().size());
            return response.getBody();
            
        } catch (RestClientException e) {
            logger.error("Error obteniendo posts: {}", e.getMessage());
            throw new RuntimeException("Error obteniendo posts desde API externa", e);
        }
    }
    
    /**
     * Obtiene un post por ID
     */
    @Cacheable(value = "posts", key = "#id")
    public Post getPostById(Long id) {
        logger.info("Obteniendo post con ID: {}", id);
        
        try {
            String url = baseUrl + "/posts/" + id;
            Post post = restTemplate.getForObject(url, Post.class);
            
            if (post != null) {
                logger.info("Post obtenido exitosamente: {}", post.getId());
            } else {
                logger.warn("No se encontró post con ID: {}", id);
            }
            
            return post;
            
        } catch (RestClientException e) {
            logger.error("Error obteniendo post con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error obteniendo post desde API externa", e);
        }
    }
    
    /**
     * Obtiene un usuario por ID
     */
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        logger.debug("Obteniendo usuario con ID: {}", id);
        
        try {
            String url = baseUrl + "/users/" + id;
            User user = restTemplate.getForObject(url, User.class);
            
            if (user != null) {
                logger.debug("Usuario obtenido exitosamente: {}", user.getId());
            } else {
                logger.warn("No se encontró usuario con ID: {}", id);
            }
            
            return user;
            
        } catch (RestClientException e) {
            logger.error("Error obteniendo usuario con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error obteniendo usuario desde API externa", e);
        }
    }
    
    /**
     * Obtiene comentarios por ID de post
     */
    @Cacheable(value = "comments", key = "#postId")
    public List<Comment> getCommentsByPostId(Long postId) {
        logger.debug("Obteniendo comentarios para post ID: {}", postId);
        
        try {
            String url = baseUrl + "/posts/" + postId + "/comments";
            ResponseEntity<List<Comment>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Comment>>() {}
            );
            
            List<Comment> comments = response.getBody();
            logger.debug("Comentarios obtenidos para post {}: {}", postId, comments != null ? comments.size() : 0);
            
            return comments;
            
        } catch (RestClientException e) {
            logger.error("Error obteniendo comentarios para post ID {}: {}", postId, e.getMessage());
            throw new RuntimeException("Error obteniendo comentarios desde API externa", e);
        }
    }
    
    /**
     * Elimina un post por ID
     */
    public boolean deletePost(Long id) {
        logger.info("Eliminando post con ID: {}", id);
        
        try {
            String url = baseUrl + "/posts/" + id;
            restTemplate.delete(url);
            
            logger.info("Post {} eliminado exitosamente", id);
            return true;
            
        } catch (RestClientException e) {
            logger.error("Error eliminando post con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error eliminando post desde API externa", e);
        }
    }
}
