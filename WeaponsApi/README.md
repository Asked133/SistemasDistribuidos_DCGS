docker-compose up --build
# DiablodexApi - Weapons gRPC Service

Servicio gRPC en Python que gestiona el loot (armas y armaduras) de Diablo IV consumido por la REST API `DiablodexApi`.

> **Nota:** Este servicio se levanta automáticamente cuando ejecutas el `docker-compose` principal de DiablodexApi. Solo necesitas iniciarlo manualmente si deseas desarrollarlo o depurarlo por separado.

## Descripción

- Exposición de inventario mediante gRPC usando los tres tipos de RPC requeridos (Unary, Client Streaming y Server Streaming).
- Persistencia en MongoDB independiente del backend SOAP (que usa MySQL) para cumplir el requerimiento “BD distinta”.
- Validaciones con `StatusCode` gRPC alineadas con los errores HTTP que devuelve la REST API.
- Scripts de inicialización, Dockerfile y docker-compose listos para entornos locales y CI.

### Operaciones RPC

| Tipo | RPC | Acción |
|------|-----|--------|
| Unary | `GetItem` | Obtiene un item por ID |
| Unary | `CreateItem`* | Se maneja vía `CreateBulkLoot` que acepta IDs personalizados |
| Unary | `UpdateItem`* | La API REST usa Mongo directamente para actualizaciones puntuales |
| Server Streaming | `ListItemsByType` | Devuelve flujo de items filtrado por tipo |
| Client Streaming | `CreateBulkLoot` | Inserta múltiples armas/armaduras en una sola conexión |

\*La funcionalidad individual de creación/actualización se canaliza a través del flujo masivo y la lógica de negocio de la REST API.

## Stack Tecnológico

| Componente | Versión | Uso |
|------------|---------|-----|
| Python | 3.11 | Servidor gRPC |
| gRPC | 1.68.1 (`grpcio`, `grpcio-tools`) | Transporte y generación de stubs |
| MongoDB | 7.0 | Persistencia NoSQL |
| PyMongo | 4.10.1 | Driver MongoDB |
| Docker/Docker Compose | latest | Contenerización y orquestación |

## Definición del Servicio (.proto)

Archivo: `protos/diablo_inventory.proto`

```protobuf
service InventoryService {
  rpc GetItem (ItemRequest) returns (ItemResponse);
  rpc ListItemsByType (ItemTypeRequest) returns (stream ItemResponse);
  rpc CreateBulkLoot (stream CreateItemRequest) returns (BulkCreateResponse);
}
```

### Mensajes Clave

| Mensaje | Campos relevantes |
|---------|-------------------|
| `ItemRequest` | `id` |
| `ItemTypeRequest` | `tipo` (`ItemType`) |
| `CreateItemRequest` | `id`, `nombre`, `tipo`, `poder_de_objeto`, `dano_base`/`armadura_base`, `habilidad_pasiva`, `habilidad_activa`, `gemas` |
| `ItemResponse` | Mismos campos que la creación + `gemas_incrustadas` |
| `BulkCreateResponse` | `items_creados`, `mensaje`, `items` |

### Enums

| Enum | Valores |
|------|---------|
| `ItemType` | `UNKNOWN_TYPE`, `WEAPON`, `ARMOR` |
| `GemType` | `UNKNOWN_GEM`, `AMATISTA`, `CRANEO`, `DIAMANTE`, `ESMERALDA`, `RUBI`, `TOPACIO`, `ZAFIRO` |

## Validaciones (src/server.py)

| Regla | Descripción | gRPC Status |
|-------|-------------|-------------|
| ID obligatorio en `GetItem` | El request debe incluir `id` | `INVALID_ARGUMENT` |
| Item inexistente | Consulta por `_id` en Mongo | `NOT_FOUND` |
| `nombre` requerido | Cadenas vacías no permitidas | `INVALID_ARGUMENT` |
| `tipo` válido | Rechaza `UNKNOWN_TYPE` | `INVALID_ARGUMENT` |
| `poder_de_objeto` 1-1000 | Rango estricto | `OUT_OF_RANGE` |
| Máximo 2 gemas | `len(gemas) <= 2` | `INVALID_ARGUMENT` |
| Campos específicos por tipo | `WEAPON` requiere `dano_base`, `ARMOR` requiere `armadura_base` | `INVALID_ARGUMENT` |
| ID duplicado | MongoDB `DuplicateKeyError` | `ALREADY_EXISTS` |

Fragmento relevante:

```python
if req.tipo == diablo_inventory_pb2.ItemType.WEAPON:
    if campo != 'dano_base' or req.dano_base <= 0:
        context.abort(grpc.StatusCode.INVALID_ARGUMENT, 'Armas requieren dano_base positivo')
elif req.tipo == diablo_inventory_pb2.ItemType.ARMOR:
    if campo != 'armadura_base' or req.armadura_base <= 0:
        context.abort(grpc.StatusCode.INVALID_ARGUMENT, 'Armaduras requieren armadura_base positiva')
```

## Base de Datos (MongoDB)

### Comparación con otros servicios

| Servicio | Tipo | Motor | Puerto | Entidad principal |
|----------|------|-------|--------|-------------------|
| `DiabloApi` (SOAP) | Relacional | MySQL 8.0 | 3306 | `characters` |
| `DiablodexApi` (gRPC) | Documental | MongoDB 7.0 | 27017 | `loot_items` |

### Esquema `loot_items`

```json
{
  "_id": "string (UUID)",
  "nombre": "string",
  "tipo": "int (1=WEAPON, 2=ARMOR)",
  "poder": "int 1-1000",
  "dano": "int opcional para armas",
  "armadura": "int opcional para armaduras",
  "pasiva": "string",
  "activa": "string",
  "gemas": ["enum GemType", "máx 2"],
  "created_at": "date",
  "updated_at": "date"
}
```

El script `scripts/init-db.js` puede ejecutarse con `mongosh mongodb://localhost:27017 < scripts/init-db.js` para provisionar la colección con validaciones JSON Schema, índices e ítems de ejemplo.

### Índices

```javascript
db.loot_items.createIndex({ tipo: 1 }, { background: true })
```

El servidor también intenta crearlo automáticamente al iniciar.

## Configuración y despliegue

### Variables de entorno

| Variable | Default | Uso |
|----------|---------|-----|
| `MONGO_URI` | `mongodb://localhost:27017` | URI de MongoDB |
| `MONGO_DB` | `diablo_weapons` | Base de datos | 
| `MONGO_COLLECTION` | `loot_items` | Colección


### Dockerfile

```dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY protos/ ./protos/
RUN python -m grpc_tools.protoc -I./protos --python_out=./src --grpc_python_out=./src ./protos/diablo_inventory.proto
COPY src/ ./src/
EXPOSE 50051
CMD ["python", "src/server.py"]
```

### docker-compose (standalone)

```bash
docker-compose up --build
```

Servicios expuestos:

- `weapons-api` → gRPC puerto `50051`.
- `weapons-mongo` → MongoDB puerto `27017` (con healthcheck e inicialización de volumen).

Dentro del stack principal (`DiablodexApi/docker-compose.yml`) también se levantan `diablodex-api`, `diablo-api` (SOAP) y la infraestructura OAuth2 (Hydra en `4444/4445`).

## Integración con DiablodexApi (REST → gRPC)

`ItemService.java` utiliza `@GrpcClient("weaponsApi")` para resolver dependencias en Spring y delegar llamadas:

```java
public ItemDto getItemById(String id) {
    ItemRequest request = ItemRequest.newBuilder().setId(id).build();
    ItemResponse response = blockingStub.getItem(request);
    return mapToDto(response);
}

public BulkCreateResponseDto createBulkItems(List<CreateItemDto> payload) {
    StreamObserver<CreateItemRequest> observer = asyncStub.createBulkLoot(responseObserver);
    payload.forEach(item -> observer.onNext(buildRequest(item)));
    observer.onCompleted();
    return responseObserver.await();
}
```

Esto garantiza que REST cumpla con los tipos de RPC requeridos (Unary para lecturas puntuales, Server Streaming para filtros y Client Streaming para inserciones masivas).

## Pruebas Manuales

1. **Arrancar stack completo**
   ```bash
   cd DiablodexApi
   docker-compose up --build
   ```
2. **Crear cliente OAuth2 y obtener token** usando Hydra (`localhost:4445/4444`).
3. **Consumir endpoints REST** (`localhost:8081`):
   - `POST /api/v1/items/bulk` → client streaming → crea armas o armaduras.
   - `GET /api/v1/items/type/{tipo}` → server streaming → lista por tipo.
   - `GET /api/v1/items/{id}` → unary → devuelve un item.
4. **Verificar códigos de error** enviando cargas inválidas (nombre vacío, poder > 1000, más de dos gemas, etc.).
5. **Probar directamente con gRPC** usando `grpcurl`:
   ```bash
   grpcurl -plaintext -d '{"id":"sample-weapon-001"}' localhost:50051 diablopb.InventoryService/GetItem
   grpcurl -plaintext -d '{"tipo":1}' localhost:50051 diablopb.InventoryService/ListItemsByType
   ```

## Desarrollo local

```bash
pip install -r requirements.txt
python -m grpc_tools.protoc -I protos --python_out=protos --pyi_out=protos --grpc_python_out=protos protos/diablo_inventory.proto
MONGO_URI=mongodb://localhost:27017 python server.py
```

## Estructura del proyecto

```
WeaponsApi/
├── Dockerfile
├── docker-compose.yml
├── README.md
├── requirements.txt
├── server.py
├── scripts/
│   └── init-db.js
└── protos/
    └── diablo_inventory.proto
```

## Documentación relacionada

- Para entender cómo REST y SOAP conviven alrededor de este servicio, revisa `DiablodexApi/README.md` en el repositorio principal.
    
