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
git fetch origin pull/28/head:pr-28
git checkout pr-28

# Docker
docker-compose up -d --build

# Podman
python -m podman_compose up -d --build
podman-compose up -d --build
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

## Weapons gRPC Service (Python)

- Servicio gRPC escrito en **Python 3.11** (carpeta `../WeaponsApi`) que gestiona loot en MongoDB 7; se levanta automáticamente con `docker-compose up --build` desde esta carpeta.
- Reúne los tres tipos de RPC requeridos del archivo `src/main/proto/diablo_inventory.proto` y usa una base de datos distinta al SOAP (`MySQL` para `DiabloApi`, `MongoDB` para `WeaponsApi`).
- Incluye Dockerfile, docker-compose y script `scripts/init-db.js` (ejecutar con `mongosh mongodb://localhost:27017 < scripts/init-db.js`) para crear colección `loot_items`, validaciones JSON Schema e índices (`tipo`).
- Validaciones principales devuelven `StatusCode` gRPC: `INVALID_ARGUMENT` (campos obligatorios, rango poder 1-1000, máx. 2 gemas, tipo específico requiere daño/armadura), `ALREADY_EXISTS` (IDs duplicados), `NOT_FOUND` (item inexistente), `OUT_OF_RANGE` (poder fuera de rango).

### RPC expuestos

| Tipo | Método | Descripción |
| --- | --- | --- |
| Unary | `GetItem` | Obtiene un item por ID. |
| Server streaming | `ListItemsByType` | Devuelve flujo de items filtrado por tipo `WEAPON`/`ARMOR`. |
| Client streaming | `CreateBulkLoot` | Inserta múltiples items en una sola conexión, reutilizada por la REST API para creaciones masivas. |

Fragmento del `.proto` (mismos mensajes/enums que usa el cliente Java):

```proto
service InventoryService {
  rpc GetItem (ItemRequest) returns (ItemResponse);
  rpc ListItemsByType (ItemTypeRequest) returns (stream ItemResponse);
  rpc CreateBulkLoot (stream CreateItemRequest) returns (BulkCreateResponse);
}
```

### REST → gRPC

`ItemService.java` declara `@GrpcClient("weaponsApi")` con `InventoryServiceGrpc` para invocar el servicio:

```java
ItemResponse response = blockingStub.getItem(ItemRequest.newBuilder().setId(id).build());
StreamObserver<CreateItemRequest> observer = asyncStub.createBulkLoot(responseObserver);
```

La endpoint REST `/api/v1/items` reutiliza estas llamadas para dar visibilidad a los flujos Unary/Streaming y propagar los códigos de error.

### Pruebas rápidas

1. Arranca todo con `docker-compose up --build`.
2. Crea cliente OAuth2 en Hydra y usa el token para invocar REST:
  - `POST /api/v1/items/bulk` → dispara client streaming.
  - `GET /api/v1/items/type/{tipo}` → server streaming.
  - `GET /api/v1/items/{id}` → unary.
3. Prueba el gRPC directamente (sin REST) con `grpcurl`:
  ```bash
  grpcurl -plaintext -d '{"id":"sample-weapon-001"}' localhost:50051 diablopb.InventoryService/GetItem
  grpcurl -plaintext -d '{"tipo":1}' localhost:50051 diablopb.InventoryService/ListItemsByType
  ```
4. Para poblar Mongo manualmente o resetear validaciones: `mongosh mongodb://localhost:27017 < scripts/init-db.js` dentro de `../WeaponsApi`.
