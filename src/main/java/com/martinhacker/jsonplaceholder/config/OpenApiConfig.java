package com.martinhacker.jsonplaceholder.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JSONPlaceholder API Integration")
                        .description("API REST que consume y procesa datos de servicios externos JSONPlaceholder. " +
                                   "Esta API proporciona endpoints para obtener posts con información completa " +
                                   "de usuarios y comentarios, así como operaciones de eliminación.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Martin Hacker")
                                .email("martin.hacker@example.com")
                                .url("https://github.com/martinhacker"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("https://api.example.com")
                                .description("Servidor de producción")
                ));
    }
}
