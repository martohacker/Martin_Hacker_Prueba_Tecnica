package com.martinhacker.jsonplaceholder.exception;

import com.martinhacker.jsonplaceholder.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Maneja excepciones de validación de argumentos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        logger.warn("Error de validación: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse<Map<String, String>> response = ApiResponse.error(
                "Error de validación en los datos de entrada", 
                HttpStatus.BAD_REQUEST.value()
        );
        response.setData(errors);
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja excepciones de violación de restricciones (validación de parámetros de método)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        
        logger.warn("Error de validación de restricciones: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        
        ApiResponse<Map<String, String>> response = ApiResponse.error(
                "Error de validación en los parámetros de entrada", 
                HttpStatus.BAD_REQUEST.value()
        );
        response.setData(errors);
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja excepciones de tipo de argumento incorrecto
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.warn("Error de tipo de argumento: {}", ex.getMessage());
        
        String message = String.format("El parámetro '%s' debe ser de tipo %s", 
                ex.getName(), ex.getRequiredType().getSimpleName());
        
        ApiResponse<String> response = ApiResponse.error(message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja excepciones de método HTTP no soportado
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        logger.warn("Método HTTP no soportado: {}", ex.getMessage());
        
        String message = String.format("Método '%s' no soportado para esta URL", ex.getMethod());
        ApiResponse<String> response = ApiResponse.error(message, HttpStatus.METHOD_NOT_ALLOWED.value());
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }
    
    /**
     * Maneja excepciones de JSON malformado
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.warn("Error de lectura de JSON: {}", ex.getMessage());
        
        ApiResponse<String> response = ApiResponse.error(
                "Error en el formato JSON de la petición", 
                HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja errores de cliente HTTP (4xx) de la API externa
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpClientError(HttpClientErrorException ex) {
        logger.error("Error de cliente HTTP en API externa: {} - {}", ex.getStatusCode(), ex.getMessage());
        
        String message = "Error en la API externa: " + ex.getStatusCode().value() + " " + ex.getStatusText();
        ApiResponse<String> response = ApiResponse.error(message, ex.getStatusCode().value());
        
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    
    /**
     * Maneja errores de servidor HTTP (5xx) de la API externa
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpServerError(HttpServerErrorException ex) {
        logger.error("Error de servidor HTTP en API externa: {} - {}", ex.getStatusCode(), ex.getMessage());
        
        String message = "Error interno en la API externa: " + ex.getStatusCode().value() + " " + ex.getStatusText();
        ApiResponse<String> response = ApiResponse.error(message, HttpStatus.SERVICE_UNAVAILABLE.value());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    /**
     * Maneja errores de timeout o conectividad
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceAccessException(ResourceAccessException ex) {
        logger.error("Error de conectividad con API externa: {}", ex.getMessage());
        
        ApiResponse<String> response = ApiResponse.error(
                "Error de conectividad con la API externa. Intente nuevamente más tarde.", 
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    /**
     * Maneja excepciones de negocio personalizadas
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<String>> handleBusinessException(BusinessException ex) {
        logger.warn("Excepción de negocio: {}", ex.getMessage());
        
        ApiResponse<String> response = ApiResponse.error(ex.getMessage(), ex.getStatusCode().value());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    
    /**
     * Maneja excepciones no controladas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        logger.error("Error interno no controlado: {}", ex.getMessage(), ex);
        
        ApiResponse<String> response = ApiResponse.error(
                "Error interno del servidor. Contacte al administrador.", 
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
