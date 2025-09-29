package com.martinhacker.jsonplaceholder.controller;

import com.martinhacker.jsonplaceholder.exception.BusinessException;
import com.martinhacker.jsonplaceholder.model.ApiResponse;
import com.martinhacker.jsonplaceholder.model.Post;
import com.martinhacker.jsonplaceholder.service.JsonPlaceholderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Validated
@Tag(name = "Posts", description = "Endpoints para gestión de posts con información completa")
public class PostController {
    
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    
    private final JsonPlaceholderService jsonPlaceholderService;
    
    public PostController(JsonPlaceholderService jsonPlaceholderService) {
        this.jsonPlaceholderService = jsonPlaceholderService;
    }
    
    /**
     * Endpoint principal: GET /posts
     * Obtiene todos los posts con información completa de usuarios y comentarios
     */
    @GetMapping
    @Operation(
            summary = "Obtener todos los posts con detalles completos",
            description = "Obtiene la lista completa de posts con información del usuario autor y comentarios. " +
                         "Realiza múltiples llamadas a la API externa JSONPlaceholder para mergear toda la información."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Posts obtenidos exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "503",
                    description = "Servicio no disponible (API externa)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    public ResponseEntity<ApiResponse<List<Post>>> getAllPosts() {
        logger.info("Solicitud recibida para obtener todos los posts con detalles");
        
        try {
            long startTime = System.currentTimeMillis();
            
            List<Post> posts = jsonPlaceholderService.getAllPostsWithDetails();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            logger.info("Posts obtenidos exitosamente: {} posts en {} ms", posts.size(), duration);
            
            ApiResponse<List<Post>> response = ApiResponse.success(
                    String.format("Se obtuvieron %d posts con detalles completos en %d ms", posts.size(), duration),
                    posts
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error obteniendo posts: {}", e.getMessage(), e);
            throw new BusinessException("Error obteniendo posts: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Endpoint secundario: DELETE /posts/{id}
     * Elimina un post por ID
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un post por ID",
            description = "Elimina un post específico por su ID. Realiza una llamada DELETE a la API externa JSONPlaceholder. " +
                         "Nota: La API no persiste cambios realmente, solo simula la operación."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post eliminado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "ID de post inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    public ResponseEntity<ApiResponse<String>> deletePost(
            @Parameter(description = "ID del post a eliminar", required = true, example = "1")
            @PathVariable @Positive(message = "El ID del post debe ser un número positivo") Long id) {
        
        logger.info("Solicitud recibida para eliminar post con ID: {}", id);
        
        try {
            boolean deleted = jsonPlaceholderService.deletePost(id);
            
            if (deleted) {
                logger.info("Post {} eliminado exitosamente", id);
                ApiResponse<String> response = ApiResponse.success(
                        String.format("Post con ID %d eliminado exitosamente", id),
                        "Operación completada"
                );
                return ResponseEntity.ok(response);
            } else {
                logger.warn("No se pudo eliminar el post con ID: {}", id);
                throw new BusinessException("No se pudo eliminar el post", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando post con ID {}: {}", id, e.getMessage(), e);
            throw new BusinessException("Error eliminando post: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Endpoint adicional: GET /posts/{id}
     * Obtiene un post específico por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un post por ID",
            description = "Obtiene un post específico por su ID con información completa del usuario y comentarios."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post obtenido exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "ID de post inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    public ResponseEntity<ApiResponse<Post>> getPostById(
            @Parameter(description = "ID del post a obtener", required = true, example = "1")
            @PathVariable @Positive(message = "El ID del post debe ser un número positivo") Long id) {
        
        logger.info("Solicitud recibida para obtener post con ID: {}", id);
        
        try {
            Post post = jsonPlaceholderService.getPostById(id);
            
            if (post == null) {
                logger.warn("Post con ID {} no encontrado", id);
                throw new BusinessException("Post no encontrado", HttpStatus.NOT_FOUND);
            }
            
            logger.info("Post {} obtenido exitosamente", id);
            ApiResponse<Post> response = ApiResponse.success("Post obtenido exitosamente", post);
            
            return ResponseEntity.ok(response);
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo post con ID {}: {}", id, e.getMessage(), e);
            throw new BusinessException("Error obteniendo post: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
