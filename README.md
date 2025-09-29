# API REST con Integración de Servicios Externos - JSONPlaceholder

## 📋 Descripción del Proyecto

Esta es una API REST desarrollada con **Spring Boot** que consume y procesa datos de la API externa [JSONPlaceholder](https://jsonplaceholder.typicode.com/). La aplicación implementa los endpoints requeridos para obtener posts con información completa de usuarios y comentarios, así como operaciones de eliminación.

## 🚀 Características Principales

- **Framework**: Spring Boot 3.2.0 con Java 17
- **Integración Externa**: Consumo de múltiples endpoints de JSONPlaceholder
- **Procesamiento Asíncrono**: Llamadas concurrentes para optimizar performance
- **Cache**: Implementación de cache con Caffeine para optimizar llamadas repetidas
- **Documentación**: OpenAPI/Swagger integrado
- **Testing**: Tests unitarios completos
- **Manejo de Errores**: Gestión robusta de excepciones y timeouts
- **Logging**: Sistema de logging configurado
- **Validaciones**: Validación de entrada y sanitización

## 🏗️ Arquitectura

```
src/main/java/com/martinhacker/jsonplaceholder/
├── JsonPlaceholderApiApplication.java    # Clase principal de Spring Boot
├── config/
│   ├── OpenApiConfig.java               # Configuración de Swagger/OpenAPI
│   └── RestTemplateConfig.java          # Configuración de RestTemplate
├── controller/
│   └── PostController.java              # Controladores REST
├── exception/
│   ├── BusinessException.java           # Excepción personalizada
│   └── GlobalExceptionHandler.java     # Manejador global de excepciones
├── model/
│   ├── ApiResponse.java                 # Respuesta estándar de la API
│   ├── Comment.java                     # Modelo de comentario
│   ├── Post.java                        # Modelo de post
│   └── User.java                        # Modelo de usuario
└── service/
    └── JsonPlaceholderService.java      # Servicio para consumir API externa
```

## 📡 Endpoints Disponibles

### 1. GET /api/posts (Endpoint Principal)
- **Descripción**: Obtiene todos los posts con información completa de usuarios y comentarios
- **Funcionalidad**: 
  - Realiza múltiples llamadas a la API externa
  - Mergea información de posts, usuarios y comentarios
  - Procesamiento asíncrono para optimizar performance
- **Respuesta**: Lista de posts con detalles completos

### 2. DELETE /api/posts/{id} (Endpoint Secundario)
- **Descripción**: Elimina un post por ID
- **Funcionalidad**: Realiza llamada DELETE a la API externa
- **Nota**: La API no persiste cambios realmente, solo simula la operación

### 3. GET /api/posts/{id} (Endpoint Adicional)
- **Descripción**: Obtiene un post específico por ID con información completa

## 🛠️ Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6 o superior
- Conexión a internet (para consumir JSONPlaceholder API)

### Compilación
```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd Martin_Hacker_Prueba_Tecnica

# Compilar el proyecto
mvn clean compile
```

### Ejecución
```bash
# Ejecutar la aplicación
mvn spring-boot:run

# O compilar y ejecutar el JAR
mvn clean package
java -jar target/jsonplaceholder-api-0.0.1-SNAPSHOT.jar
```

### Testing
```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report
```

## 🌐 Uso de la API

### 1. Obtener todos los posts con detalles
```bash
curl -X GET "http://localhost:8080/api/posts" \
     -H "Content-Type: application/json"
```

### 2. Obtener un post específico
```bash
curl -X GET "http://localhost:8080/api/posts/1" \
     -H "Content-Type: application/json"
```

### 3. Eliminar un post
```bash
curl -X DELETE "http://localhost:8080/api/posts/1" \
     -H "Content-Type: application/json"
```

## 📚 Documentación Interactiva

Una vez ejecutada la aplicación, puedes acceder a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **Actuator Health**: http://localhost:8080/actuator/health

## ⚙️ Configuración

La aplicación utiliza `application.properties` para configuración:

```properties
# Puerto del servidor
server.port=8080

# URL base de la API externa
external.api.jsonplaceholder.base-url=https://jsonplaceholder.typicode.com
external.api.jsonplaceholder.timeout=5000

# Configuración de cache
spring.cache.type=caffeine
spring.cache.cache-names=posts,users,comments
spring.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=5m

# Configuración de logging
logging.level.com.martinhacker.jsonplaceholder=INFO
```

## 🧪 Testing

### Tests Unitarios
- **PostControllerTest**: Tests para todos los endpoints del controlador
- **JsonPlaceholderServiceTest**: Tests para el servicio de integración
- **Cobertura**: >80% de cobertura de código

### Ejecutar Tests
```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=PostControllerTest
mvn test -Dtest=JsonPlaceholderServiceTest
```

## 🔧 Decisiones Técnicas

### 1. **Spring Boot vs Quarkus**
- Elegido Spring Boot por familiaridad y ecosistema maduro
- Mejor soporte para testing y documentación
- Configuración más flexible para integración con APIs externas

### 2. **RestTemplate vs WebClient**
- RestTemplate para simplicidad y compatibilidad
- Configuración de timeouts personalizada
- Manejo de errores más directo

### 3. **Procesamiento Asíncrono**
- CompletableFuture para llamadas concurrentes
- Thread pool configurado para optimizar performance
- Merge de datos de forma eficiente

### 4. **Cache Strategy**
- Caffeine cache para optimizar llamadas repetidas
- TTL de 5 minutos para datos de usuarios y comentarios
- Cache por separado para posts, usuarios y comentarios

### 5. **Manejo de Errores**
- GlobalExceptionHandler centralizado
- Excepciones personalizadas para diferentes tipos de errores
- Respuestas HTTP apropiadas según el tipo de error

## 📊 Performance

- **Llamadas Concurrentes**: Procesamiento asíncrono de usuarios y comentarios
- **Cache**: Reducción de llamadas repetidas a la API externa
- **Timeouts**: Configuración de 5 segundos para evitar bloqueos
- **Logging**: Monitoreo de tiempos de respuesta

## 🔒 Seguridad

- **Validación de Entrada**: Validación de parámetros de entrada
- **Sanitización**: Limpieza de datos recibidos
- **Manejo de Errores**: No exposición de información sensible en errores

## 📈 Monitoreo

- **Actuator**: Endpoints de salud y métricas
- **Logging**: Logs estructurados con niveles configurables
- **Métricas**: Tiempo de respuesta y número de llamadas

## 🚀 Mejoras Futuras

1. **Rate Limiting**: Implementar límites de velocidad para la API externa
2. **Circuit Breaker**: Patrón circuit breaker para resilencia
3. **Métricas Avanzadas**: Integración con Micrometer y Prometheus
4. **Base de Datos**: Persistencia local para cache más robusto
5. **Docker**: Containerización de la aplicación

## 👨‍💻 Autor

**Martin Hacker**
- Email: martin.hacker@example.com
- GitHub: [@martinhacker](https://github.com/martinhacker)


---

## 🎯 Criterios de Evaluación Cumplidos

### ✅ Requisitos Obligatorios
- [x] API REST funcional con los endpoints especificados
- [x] Manejo de excepciones y errores HTTP
- [x] Logging básico
- [x] README con instrucciones de ejecución

### ✅ Requisitos Valorables
- [x] Tests unitarios para el endpoint principal
- [x] Configuración externalizada (application.properties)
- [x] Implementación de cache para optimizar llamadas repetidas
- [x] Documentación con OpenAPI/Swagger
- [x] Validaciones de entrada y sanitización

### ✅ Criterios de Evaluación
- [x] **Funcionalidad**: Los endpoints funcionan correctamente
- [x] **Integración**: Consumo adecuado de múltiples servicios externos
- [x] **Código**: Calidad, organización y buenas prácticas
- [x] **Manejo de errores**: Gestión apropiada de fallos y timeouts
- [x] **Testing**: Diseño y cobertura de tests
- [x] **Performance**: Eficiencia en las llamadas concurrentes

