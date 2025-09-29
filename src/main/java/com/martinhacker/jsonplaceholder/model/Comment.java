package com.martinhacker.jsonplaceholder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de Comentario")
public class Comment {
    
    @Schema(description = "ID único del comentario", example = "1")
    private Long id;
    
    @Schema(description = "ID del post al que pertenece el comentario", example = "1")
    @JsonProperty("postId")
    private Long postId;
    
    @Schema(description = "Nombre del autor del comentario", example = "id labore ex et quam laborum")
    private String name;
    
    @Schema(description = "Email del autor del comentario", example = "Eliseo@gardner.biz")
    private String email;
    
    @Schema(description = "Contenido del comentario", example = "laudantium enim quasi est quidem magnam voluptate ipsam eos...")
    private String body;
    
    // Constructores
    public Comment() {}
    
    public Comment(Long id, Long postId, String name, String email, String body) {
        this.id = id;
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.body = body;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getPostId() {
        return postId;
    }
    
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + (body != null && body.length() > 50 ? body.substring(0, 50) + "..." : body) + '\'' +
                '}';
    }
}
