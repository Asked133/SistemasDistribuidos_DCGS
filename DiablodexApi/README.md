# Diablodex API - REST Web Service

API REST para gestionar personajes de Diablo IV. Consume el servicio SOAP **DiabloApi** (Parcial 1), con caché Redis y autenticación OAuth2 (Hydra).

## Operaciones

- **GET /api/v1/characters/{id}** - Obtener por ID
- **GET /api/v1/characters** - Listar con paginación
- **POST /api/v1/characters** - Crear
- **PUT /api/v1/characters/{id}** - Actualizar completo
- **PATCH /api/v1/characters/{id}** - Actualizar parcial
- **DELETE /api/v1/characters/{id}** - Eliminar

## Tecnologías

- Java 21, Spring Boot 3.5.5, Spring Web Services (SOAP Client)
- Redis 7, Ory Hydra v2.2, MySQL 8.0, PostgreSQL 15
- Docker Compose, Bean Validation, Swagger UI

## Prerequisitos

- Docker y Docker Compose (o Podman)
- Puertos: 8081, 8055, 6379, 3366, 5433, 4444-4445

## Ejecución

```bash
git clone https://github.com/Asked133/SistemasDistribuidos_DCGS.git
cd SistemasDistribuidos_DCGS/DiablodexApi

# Docker
podman-compose up -d --build

# Podman
python -m podman_compose up -d --build
```

**URLs:**
- REST API: http://localhost:8081
- Swagger: http://localhost:8081/swagger-ui.html
- SOAP WSDL: http://localhost:8055/ws/characters.wsdl

## OAuth2 - Hydra

### 1. Crear cliente
```bash
# Docker
podman exec hydra hydra create oauth2-client \
  --endpoint http://localhost:4445 \
  --grant-type client_credentials \
  --scope read,write --format json

# Podman
podman exec hydra hydra create oauth2-client \
  --endpoint http://localhost:4445 \
  --grant-type client_credentials \
  --scope read,write --format json
```

### 2. Obtener token
```bash
curl -X POST http://localhost:4444/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=TU_CLIENT_ID" \
  -d "client_secret=TU_CLIENT_SECRET" \
  -d "scope=read write"
```

### 3. Usar en Postman
Headers → `Authorization: Bearer TU_ACCESS_TOKEN`

Scopes: `read` (GET) | `write` (POST/PUT/PATCH/DELETE)

## Ejemplos

### POST - Crear Personaje
```bash
POST http://localhost:8081/api/v1/characters
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Conan",
  "characterClass": "BARBARIAN",
  "level": 50,
  "power": 1500,
  "armor": 2000,
  "life": 5000,
  "strength": 150,
  "intelligence": 80,
  "willpower": 100,
  "dexterity": 120
}
```

### GET - Listar con Filtros
```bash
GET http://localhost:8081/api/v1/characters?page=0&size=10&sort=name,asc&characterClass=BARBARIAN
Authorization: Bearer <token>
```

### PATCH - Actualizar Parcial
```bash
PATCH http://localhost:8081/api/v1/characters/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "level": 70,
  "power": 2500
}
```

### DELETE - Eliminar
```bash
DELETE http://localhost:8081/api/v1/characters/{id}
Authorization: Bearer <token>
```

## Validaciones

- **name**: 3-50 caracteres (requerido)
- **characterClass**: BARBARIAN, SORCERER, NECROMANCER, ROGUE, DRUID, SPIRITBORN
- **level**: 1-100
- **stats**: no negativos

**Ejemplo error 400:**
```json
{
  "status": 400,
  "message": "Validation failed",
  "details": "Strength cannot be negative"
}
```

## Códigos HTTP

- 200 (OK), 201 (Created), 204 (No Content)
- 400 (Bad Request), 401 (Unauthorized), 403 (Forbidden)
- 404 (Not Found), 409 (Conflict), 502 (Bad Gateway)

## Comandos Útiles

```bash
# Logs
podman logs diablodex-api -f

# Reiniciar
podman-compose restart diablodex-api

# Redis
podman exec -it redis redis-cli
> KEYS *

# Limpiar
podman-compose down -v
podman-compose up -d --build
```

## Notas

- Caché Redis (TTL 5 min) en GETs
- OAuth2 obligatorio
- Swagger UI disponible
- DiabloApi (SOAP) requerido

---
**Autor:** Diego Castellanos | UPIICSA-IPN | Nov 2025
