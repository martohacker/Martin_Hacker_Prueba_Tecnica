package com.martinhacker.jsonplaceholder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Modelo de Post con información completa del usuario y comentarios")
public class Post {
    
    @Schema(description = "ID único del post", example = "1")
    private Long id;
    
    @Schema(description = "Título del post", example = "Título del post")
    private String title;
    
    @Schema(description = "Contenido del post", example = "Contenido del post")
    private String body;
    
    @Schema(description = "ID del usuario autor")
    private Long userId;
    
    @Schema(description = "Información del usuario autor")
    private User user;
    
    @Schema(description = "Lista de comentarios del post")
    private List<Comment> comments;
    
    // Constructores
    public Post() {}
    
    public Post(Long id, String title, String body, Long userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", userId=" + userId +
                ", user=" + user +
                ", comments=" + (comments != null ? comments.size() : 0) + " comentarios" +
                '}';
    }
}
