package com.martinhacker.jsonplaceholder.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción personalizada para errores de negocio
 */
public class BusinessException extends RuntimeException {
    
    private final HttpStatus statusCode;
    
    public BusinessException(String message) {
        super(message);
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String message, Throwable cause, HttpStatus statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
