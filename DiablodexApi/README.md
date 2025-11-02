# DiablodexApi - REST API para Diablo IV Characters

API REST que consume el servicio SOAP DiabloApi para gestionar personajes de Diablo IV. Implementa autenticación OAuth2 con Hydra, caché distribuido con Redis, y paginación HATEOAS.

## 📋 Características

- ✅ **6 Endpoints REST**: GET (con filtro y paginación), GET/{id}, POST, PUT, PATCH, DELETE
- ✅ **Autenticación OAuth2**: Hydra (ORY) con scopes `read` y `write`
- ✅ **Caché Distribuido**: Redis con invalidación automática
- ✅ **Cliente SOAP**: Consumo del servicio DiabloApi mediante JAXB
- ✅ **HATEOAS**: Enlaces hipermedia en respuestas
- ✅ **Paginación**: Soporte completo con query parameters
- ✅ **Validaciones**: Bean Validation con `@Valid`
- ✅ **Manejo de Errores**: `@ControllerAdvice` con respuestas estandarizadas
- ✅ **Documentación API**: Postman Collection con auto-autenticación OAuth2

## 🏗️ Arquitectura

```
┌─────────────┐        ┌──────────────────┐        ┌─────────────┐
│   Cliente   │───────▶│  DiablodexApi    │───────▶│  DiabloApi  │
│  (Postman)  │  HTTP  │  (REST + OAuth2) │  SOAP  │  (Backend)  │
└─────────────┘        └──────────────────┘        └─────────────┘
                              │      │
                              │      └──────▶ Redis (Cache)
                              │
                              └─────────────▶ Hydra (OAuth2)
                                                   │
                                                   └─▶ PostgreSQL
```

## 🚀 Inicio Rápido

### Prerrequisitos

- Java 21
- Maven 3.9+
- Docker/Podman
- DiabloApi SOAP ejecutándose en `http://localhost:8055/ws`

### 1. Levantar servicios con Docker Compose

```bash
# Construir y levantar todos los contenedores
docker compose up --build -d

# Verificar que los servicios estén corriendo
docker compose ps
```

Esto iniciará:
- **diablodexapi**: API REST en `http://localhost:8080`
- **redis**: Cache en `localhost:6379`
- **db-hydra**: PostgreSQL para Hydra
- **hydra-migrate**: Migración de base de datos
- **hydra-admin**: Consola administrativa en `http://localhost:4445`
- **hydra-public**: Endpoints públicos OAuth2 en `http://localhost:4444`

### 2. Configurar Cliente OAuth2 en Hydra

```bash
# Crear cliente con scopes read y write
docker exec diablodexapi-hydra-admin-1 hydra create client \
  --endpoint http://localhost:4445 \
  --grant-type client_credentials \
  --scope read,write \
  --id diablo-client \
  --secret diablo-secret
```

**Salida esperada:**
```
CLIENT ID       diablo-client
CLIENT SECRET   diablo-secret
GRANT TYPES     client_credentials
SCOPE           read write
```

### 3. Obtener Token de Acceso

```bash
# Solicitar token con ambos scopes
curl -X POST "http://localhost:4444/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=diablo-client" \
  -d "client_secret=diablo-secret" \
  -d "scope=read write"
```

**Respuesta:**
```json
{
  "access_token": "ory_at_xxxxxxxxxxxxxxxxxxxx",
  "expires_in": 3599,
  "scope": "read write",
  "token_type": "bearer"
}
```

## 📡 Endpoints

### Base URL
```
http://localhost:8080/characters
```

### 1. GET /characters (Paginado con Filtro)

Obtiene personajes filtrados por clase con soporte de paginación.

**Headers:**
```
Authorization: Bearer <access_token>
```

**Query Parameters:**
- `class` (obligatorio): Clase del personaje (`BARBARIAN`, `DRUID`, `NECROMANCER`, `ROGUE`, `SORCERER`)
- `page` (opcional, default: 0): Número de página
- `pageSize` (opcional, default: 10): Tamaño de página
- `sort` (opcional, default: name): Campo de ordenamiento

**Ejemplo curl:**
```bash
curl -X GET "http://localhost:8080/characters?class=BARBARIAN&page=0&pageSize=5&sort=name" \
  -H "Authorization: Bearer ory_at_xxxxxxxxxxxxxxxxxxxx"
```

**Respuesta (200 OK):**
```json
{
  "content": [
    {
      "id": "char-001",
      "name": "Bjorn the Brave",
      "characterClass": "BARBARIAN",
      "level": 75,
      "stats": {
        "strength": 950,
        "dexterity": 450,
        "intelligence": 250,
        "vitality": 800
      },
      "_links": {
        "self": {
          "href": "http://localhost:8080/characters/char-001"
        }
      }
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalPages": 3,
  "totalElements": 15,
  "last": false,
  "first": true
}
```

### 2. GET /characters/{id}

Obtiene un personaje específico por su ID.

**Ejemplo curl:**
```bash
curl -X GET "http://localhost:8080/characters/char-001" \
  -H "Authorization: Bearer ory_at_xxxxxxxxxxxxxxxxxxxx"
```

**Respuesta (200 OK):**
```json
{
  "id": "char-001",
  "name": "Bjorn the Brave",
  "characterClass": "BARBARIAN",
  "level": 75,
  "stats": {
    "strength": 950,
    "dexterity": 450,
    "intelligence": 250,
    "vitality": 800
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/characters/char-001"
    },
    "collection-by-class": {
      "href": "http://localhost:8080/characters?class=BARBARIAN"
    }
  }
}
```

### 3. POST /characters

Crea un nuevo personaje.

**Validaciones:**
- `name`: No vacío
- `characterClass`: Enum válido
- `level`: Entre 1 y 100
- `stats`: Todos los valores > 0

**Ejemplo curl:**
```bash
curl -X POST "http://localhost:8080/characters" \
  -H "Authorization: Bearer ory_at_xxxxxxxxxxxxxxxxxxxx" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Lyra Shadowblade",
    "characterClass": "ROGUE",
    "level": 60,
    "stats": {
      "strength": 400,
      "dexterity": 950,
      "intelligence": 500,
      "vitality": 650
    }
  }'
```

**Respuesta (201 Created):**
```json
{
  "id": "char-new-123",
  "name": "Lyra Shadowblade",
  "characterClass": "ROGUE",
  "level": 60,
  "stats": {
    "strength": 400,
    "dexterity": 950,
    "intelligence": 500,
    "vitality": 650
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/characters/char-new-123"
    }
  }
}
```

### 4. PUT /characters/{id}

Reemplaza completamente un personaje existente.

**Ejemplo curl:**
```bash
curl -X PUT "http://localhost:8080/characters/char-001" \
  -H "Authorization: Bearer ory_at_xxxxxxxxxxxxxxxxxxxx" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bjorn the Legendary",
    "characterClass": "BARBARIAN",
    "level": 80,
    "stats": {
      "strength": 1000,
      "dexterity": 500,
      "intelligence": 300,
      "vitality": 850
    }
  }'
```

**Respuesta (200 OK):** Similar a POST

### 5. PATCH /characters/{id}

Actualiza parcialmente un personaje (solo campos enviados).

**Ejemplo curl:**
```bash
curl -X PATCH "http://localhost:8080/characters/char-001" \
  -H "Authorization: Bearer ory_at_xxxxxxxxxxxxxxxxxxxx" \
  -H "Content-Type: application/json" \
  -d '{
    "level": 85
  }'
```

### 6. DELETE /characters/{id}

Elimina un personaje.

**Ejemplo curl:**
```bash
curl -X DELETE "http://localhost:8080/characters/char-001" \
  -H "Authorization: Bearer ory_at_xxxxxxxxxxxxxxxxxxxx"
```

**Respuesta (204 No Content)**

## 🔒 Manejo de Errores

### 400 Bad Request (Validación)
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "El campo 'name' no puede estar vacío",
  "timestamp": "2025-11-01T10:30:00.000+00:00"
}
```

### 401 Unauthorized (Token inválido)
```json
{
  "error": "invalid_token",
  "error_description": "Token expirado o inválido"
}
```

### 403 Forbidden (Scope insuficiente)
```json
{
  "error": "insufficient_scope",
  "error_description": "El token no tiene el scope 'write' requerido"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Character not found with id: char-999",
  "timestamp": "2025-11-01T10:30:00.000+00:00"
}
```

### 409 Conflict
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Character with name 'Bjorn the Brave' already exists",
  "timestamp": "2025-11-01T10:30:00.000+00:00"
}
```

### 502 Bad Gateway (Error SOAP)
```json
{
  "status": 502,
  "error": "Bad Gateway",
  "message": "Error communicating with SOAP service: Connection refused",
  "timestamp": "2025-11-01T10:30:00.000+00:00"
}
```

## 🗂️ Caché con Redis

### Estrategia de Caché

- **GET /characters?class={class}**: Cachea resultados por clase con TTL de 10 minutos
- **GET /characters/{id}**: Cachea personajes individuales
- **POST, PUT, PATCH, DELETE**: Invalida caché automáticamente con `@CacheEvict`

### Verificar Caché

```bash
# Conectarse a Redis
docker exec -it diablodexapi-redis-1 redis-cli

# Ver todas las claves
KEYS *

# Ver valor de una clave
GET "characters::BARBARIAN::0::10"

# Limpiar toda la caché
FLUSHALL
```

## 📚 Documentación de la API con Postman

### Importar Colección y Environment

La API está completamente documentada con Postman Collection que incluye **auto-autenticación OAuth2**.

#### Archivos incluidos:

1. **`DiablodexApi.postman_collection.json`** - Todos los endpoints documentados
2. **`DiablodexApi.postman_environment.json`** - Variables de entorno pre-configuradas

#### Pasos para importar:

1. Abre Postman
2. Click en **"Import"** (esquina superior izquierda)
3. Arrastra ambos archivos JSON o selecciónalos con **"Upload Files"**
4. Los archivos se importarán automáticamente

### Configuración Automática

El **Environment** ya incluye todas las variables necesarias:

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `base_url` | `http://localhost:8081` | URL base de la API REST |
| `hydra_url` | `http://localhost:4444` | URL de Hydra OAuth2 |
| `client_id` | `diablo-client` | Client ID de OAuth2 |
| `client_secret` | `diablo-secret` | Client Secret de OAuth2 |
| `access_token` | (auto-generado) | Token JWT (se renueva automáticamente) |

### Auto-Autenticación OAuth2

La colección incluye un **Pre-request Script** que:

✅ Obtiene automáticamente un nuevo token antes de cada petición  
✅ Actualiza la variable `access_token` en el environment  
✅ Aplica el token Bearer a todos los requests  

**No necesitas hacer nada manualmente** - solo ejecuta los requests y funcionarán.

### Endpoints incluidos:

#### 📁 Characters
- **GET All Characters (Paginated)** - Lista paginada con filtro por clase
- **GET Character by ID** - Obtener personaje específico
- **POST Create Character** - Crear nuevo personaje
- **PUT Update Character (Full)** - Actualización completa
- **PATCH Update Character (Partial)** - Actualización parcial
- **DELETE Character** - Eliminar personaje

#### 🔐 OAuth2
- **Get Access Token** - Obtener token manualmente (opcional)

### Probar la API

1. **Selecciona el Environment**: Click en el dropdown de environments y selecciona "DiablodexApi Environment"
2. **Ejecuta cualquier request**: El token se obtendrá automáticamente
3. **Ver Console**: Abre Postman Console (View → Show Postman Console) para ver mensajes como:
   ```
   ✅ Token refreshed successfully
   ```

### Ejemplo: Crear un Personaje

1. Abre el request **"POST Create Character"**
2. El body ya está pre-configurado:
   ```json
   {
     "name": "Lyra Shadowblade",
     "characterClass": "ROGUE",
     "level": 60,
     "stats": {
       "strength": 400,
       "dexterity": 950,
       "intelligence": 500,
       "vitality": 650
     }
   }
   ```
3. Click en **"Send"**
4. Recibirás respuesta **201 Created** con el personaje creado

## 🧪 Probar con cURL (Alternativa)

Si prefieres usar cURL en lugar de Postman:

### 1. Obtener Token

```bash
curl -X POST "http://localhost:4444/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=diablo-client" \
  -d "client_secret=diablo-secret" \
  -d "scope=read write"
```

### 2. Usar Token en Requests

```bash
# Guarda el token en una variable
TOKEN="ory_at_xxxxxxxxxxxx"

# GET All Characters
curl -X GET "http://localhost:8081/characters?class=BARBARIAN&page=0&pageSize=10" \
  -H "Authorization: Bearer $TOKEN"

# POST Create Character
curl -X POST "http://localhost:8081/characters" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Character",
    "characterClass": "SORCERER",
    "level": 50,
    "stats": {
      "strength": 300,
      "dexterity": 400,
      "intelligence": 1000,
      "vitality": 600
    }
  }'
```

## 🛠️ Desarrollo

### Compilar localmente

```bash
# Limpiar y compilar
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Empaquetar JAR
./mvnw package

# Ejecutar localmente (sin Docker)
./mvnw spring-boot:run
```

### Variables de Entorno (application.properties)

```properties
# SOAP Client
soap.client.url=http://localhost:8055/ws

# Redis Cache
spring.data.redis.host=localhost
spring.data.redis.port=6379

# OAuth2 Resource Server
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:4444

# Logging
logging.level.com.example.DiablodexApi=DEBUG
```

## 🐳 Docker

### Construir Imagen

```bash
docker build -t diablodexapi:latest .
```

### Ejecutar Standalone

```bash
docker run -d -p 8080:8080 \
  -e SOAP_CLIENT_URL=http://host.docker.internal:8055/ws \
  -e SPRING_DATA_REDIS_HOST=redis \
  -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://hydra:4444 \
  diablodexapi:latest
```

## 📦 Estructura del Proyecto

```
src/main/java/com/example/DiablodexApi/
├── config/
│   ├── CacheConfig.java          # Configuración Redis
│   ├── OpenApiConfig.java        # Swagger/OpenAPI
│   ├── SecurityConfig.java       # OAuth2 Resource Server
│   └── SoapClientConfig.java     # Cliente SOAP JAXB
├── controller/
│   └── CharacterController.java  # 6 endpoints REST
├── dto/
│   ├── CharacterResponse.java    # HATEOAS + RepresentationModel
│   ├── CreateCharacterRequest.java
│   ├── UpdateCharacterRequest.java
│   ├── PatchCharacterRequest.java
│   └── StatsDto.java
├── exception/
│   ├── GlobalExceptionHandler.java  # @ControllerAdvice
│   ├── ResourceNotFoundException.java
│   └── ConflictException.java
├── gateway/
│   └── CharacterSoapGateway.java # Cliente SOAP con WebServiceTemplate
├── mapper/
│   └── CharacterMapper.java      # Conversión DTO ↔ SOAP
└── service/
    ├── ICharacterService.java
    └── CharacterService.java     # Lógica + @Cacheable/@CacheEvict
```

## 🔗 Enlaces Útiles

- **API REST**: http://localhost:8081
- **Hydra Admin**: http://localhost:4445
- **Hydra Public (OAuth2)**: http://localhost:4444
- **DiabloApi SOAP**: http://localhost:8055/ws
- **Redis**: localhost:6379
- **Postman Collection**: `DiablodexApi.postman_collection.json`
- **Environment**: `DiablodexApi.postman_environment.json`

## 📝 Notas de Implementación

### Clases Enum en SOAP

El servicio SOAP define:
```java
public enum CharacterClassEnum {
    BARBARIAN, DRUID, NECROMANCER, ROGUE, SORCERER
}
```

Usa estos valores en el query parameter `class`.

### Paginación In-Memory

Actualmente se implementa paginación en memoria usando `subList`:
```java
int start = (int) pageable.getOffset();
int end = Math.min(start + pageable.getPageSize(), total);
List<CharacterResponse> page = allCharacters.subList(start, end);
return new PageImpl<>(page, pageable, total);
```

**Limitación**: Si el servicio SOAP devuelve miles de personajes, se cargan todos en memoria. Para producción, el servicio SOAP debería soportar paginación nativa.

### Invalidación de Caché

Todas las operaciones de escritura invalidan la caché:
```java
@CacheEvict(value = "characters", allEntries = true)
public CharacterResponse create(CreateCharacterRequest request) { ... }
```

Esto asegura que `GET /characters` siempre devuelva datos frescos después de modificaciones.

## 🎯 Requisitos Cumplidos (Parcial 2)

- ✅ **GET /{resource}**: Con filtro por `class` y paginación
- ✅ **GET /{resource}/{id}**: Obtener personaje específico
- ✅ **POST /{resource}**: Crear personaje con validaciones
- ✅ **PUT /{resource}/{id}**: Reemplazo total
- ✅ **PATCH /{resource}/{id}**: Actualización parcial
- ✅ **DELETE /{resource}/{id}**: Eliminación
- ✅ **OAuth2**: Hydra con scopes `read` y `write`
- ✅ **Caché**: Redis con `@Cacheable` y `@CacheEvict`
- ✅ **HATEOAS**: Spring HATEOAS con `RepresentationModel`
- ✅ **Paginación**: `Page<>` con `Pageable`
- ✅ **Validaciones**: `@Valid` con Bean Validation
- ✅ **Manejo de Errores**: `@ControllerAdvice` con códigos HTTP estándar
- ✅ **Documentación**: Postman Collection con auto-autenticación OAuth2
- ✅ **Cliente SOAP**: Integración con DiabloApi mediante JAXB
- ✅ **Docker Compose**: 6 servicios orquestados (API REST, SOAP, Redis, MySQL, PostgreSQL, Hydra)

## 👨‍💻 Autor

**Diego Camilo Gomez Saenz**  
Universidad Industrial de Santander  
Sistemas Distribuidos - Parcial 2

---

**Versión**: 1.0.0  
**Fecha**: Noviembre 2025  
**Java**: 21  
**Spring Boot**: 3.5.7
