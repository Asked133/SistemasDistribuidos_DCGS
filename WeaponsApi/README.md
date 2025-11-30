# WeaponsApi - gRPC Service

Servicio gRPC en Python para gestionar items de inventario de Diablo IV (armas y armaduras).

## 📋 Resumen de Requisitos Implementados

| Requisito | Estado | Descripción |
|-----------|--------|-------------|
| 1 Unary RPC | ✅ | `GetItem` - Obtener item por ID |
| 1 Server Streaming RPC | ✅ | `ListItemsByType` - Listar items por tipo |
| 1 Client Streaming RPC | ✅ | `CreateBulkLoot` - Crear items masivamente |
| BD distinta a SOAP | ✅ | SOAP usa MySQL, gRPC usa MongoDB |
| Scripts de BD | ✅ | Ver sección "Base de Datos" |
| Validaciones con códigos gRPC | ✅ | INVALID_ARGUMENT, ALREADY_EXISTS, NOT_FOUND, OUT_OF_RANGE |
| REST invoca gRPC | ✅ | ItemService.java hace las llamadas |
| Pruebas manuales | ✅ | Ver sección "Pruebas" |

---

## 🗄️ Base de Datos

### Comparación SOAP vs gRPC

| Aspecto | SOAP (DiabloApi) | gRPC (WeaponsApi) |
|---------|-----------------|-------------------|
| Base de datos | MySQL 8.0 | MongoDB 7.0 |
| Puerto | 3306 | 27017 |
| Tipo | Relacional | NoSQL/Documental |
| Entidad | `characters` (personajes) | `loot_items` (items) |

### Script de Inicialización MongoDB

El servidor crea automáticamente la colección y los índices al iniciar. Pero puedes ejecutar manualmente:

```javascript
// Conectar a MongoDB
mongosh mongodb://localhost:27017

// Usar base de datos
use diablo_weapons

// Crear colección con validación de esquema
db.createCollection("loot_items", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["_id", "nombre", "tipo", "poder"],
         properties: {
            _id: {
               bsonType: "string",
               description: "ID único del item (UUID)"
            },
            nombre: {
               bsonType: "string",
               description: "Nombre del item - obligatorio"
            },
            tipo: {
               bsonType: "int",
               enum: [1, 2],
               description: "1=WEAPON, 2=ARMOR"
            },
            poder: {
               bsonType: "int",
               minimum: 0,
               maximum: 1000,
               description: "Poder del objeto (0-1000)"
            },
            dano: {
               bsonType: "int",
               description: "Daño base (solo para WEAPON)"
            },
            armadura: {
               bsonType: "int",
               description: "Armadura base (solo para ARMOR)"
            },
            pasiva: {
               bsonType: "string",
               description: "Habilidad pasiva"
            },
            activa: {
               bsonType: "string",
               description: "Habilidad activa"
            },
            gemas: {
               bsonType: "array",
               maxItems: 2,
               items: {
                  bsonType: "int",
                  enum: [0, 1, 2, 3, 4, 5, 6, 7]
               },
               description: "Gemas incrustadas (máximo 2)"
            }
         }
      }
   }
})

// Crear índice en tipo para búsquedas eficientes
db.loot_items.createIndex({ "tipo": 1 })

// Insertar datos de ejemplo
db.loot_items.insertMany([
  {
    _id: "weapon-001",
    nombre: "Espada del Caos",
    tipo: 1,
    poder: 750,
    dano: 150,
    pasiva: "Aumenta daño crítico 15%",
    activa: "Torbellino de fuego",
    gemas: [5, 3]
  },
  {
    _id: "armor-001", 
    nombre: "Coraza de Hierro Ancestral",
    tipo: 2,
    poder: 600,
    armadura: 85,
    pasiva: "Reduce daño recibido 10%",
    activa: "Escudo de hielo",
    gemas: [4]
  }
])

// Verificar datos
db.loot_items.find().pretty()
```

---

## 📡 Definición Proto

```protobuf
service InventoryService {
    // 1. UNARY - Obtener un item por ID
    rpc GetItem (ItemRequest) returns (ItemResponse);
    
    // 2. SERVER STREAMING - Listar items por tipo
    rpc ListItemsByType (ItemTypeRequest) returns (stream ItemResponse);
    
    // 3. CLIENT STREAMING - Crear items masivamente
    rpc CreateBulkLoot (stream CreateItemRequest) returns (BulkCreateResponse);
}
```

### Mensajes

| Mensaje | Campos |
|---------|--------|
| `ItemRequest` | id (string) |
| `ItemTypeRequest` | tipo (ItemType) |
| `CreateItemRequest` | id, nombre, tipo, poder_de_objeto, dano_base/armadura_base, habilidad_pasiva, habilidad_activa, gemas |
| `ItemResponse` | Mismos campos que CreateItemRequest |
| `BulkCreateResponse` | items_creados, mensaje |

### Enums

| Enum | Valores |
|------|---------|
| `ItemType` | UNKNOWN_TYPE=0, WEAPON=1, ARMOR=2 |
| `GemType` | UNKNOWN_GEM=0, AMATISTA=1, CRANEO=2, DIAMANTE=3, ESMERALDA=4, RUBI=5, TOPACIO=6, ZAFIRO=7 |

---

## ✅ Validaciones y Códigos gRPC

| Validación | Código gRPC | HTTP |
|------------|-------------|------|
| ID vacío en GetItem | `INVALID_ARGUMENT` | 400 |
| Item no existe | `NOT_FOUND` | 404 |
| Nombre vacío | `INVALID_ARGUMENT` | 400 |
| Tipo inválido (UNKNOWN_TYPE) | `INVALID_ARGUMENT` | 400 |
| Poder fuera de rango (0-1000) | `OUT_OF_RANGE` | 400 |
| Más de 2 gemas | `INVALID_ARGUMENT` | 400 |
| ID duplicado | `ALREADY_EXISTS` | 409 |

### Código del Servidor (server.py)

```python
def _validate_create_request(self, req, item_id, context):
    # Nombre obligatorio
    if not req.nombre:
        context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Nombre es obligatorio")
    
    # Tipo válido
    if req.tipo == diablo_inventory_pb2.ItemType.UNKNOWN_TYPE:
        context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Tipo es invalido")
    
    # Límite numérico de poder
    if not (0 <= req.poder_de_objeto <= 1000):
        context.abort(grpc.StatusCode.OUT_OF_RANGE, "Poder fuera de rango (0-1000)")
    
    # Máximo 2 gemas
    if len(req.gemas) > 2:
        context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Maximo 2 gemas permitidas")
```

---

## 🔗 REST API invoca gRPC

La REST API (DiablodexApi) invoca al servicio gRPC. Fragmento de código en `ItemService.java`:

```java
// UNARY RPC - GetItem
@GrpcClient("weaponsApi")
private InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub;

public ItemDto getItemById(String id) {
    ItemRequest request = ItemRequest.newBuilder()
            .setId(id)
            .build();
    
    // Llamada gRPC Unary
    ItemResponse response = blockingStub.getItem(request);
    return mapToDto(response);
}

// SERVER STREAMING RPC - ListItemsByType
public List<ItemDto> getItemsByType(String type) {
    ItemTypeRequest request = ItemTypeRequest.newBuilder()
            .setTipo(parseItemType(type))
            .build();
    
    List<ItemDto> items = new ArrayList<>();
    // Llamada gRPC Server Streaming
    Iterator<ItemResponse> responseIterator = blockingStub.listItemsByType(request);
    
    while (responseIterator.hasNext()) {
        items.add(mapToDto(responseIterator.next()));
    }
    return items;
}

// CLIENT STREAMING RPC - CreateBulkLoot
@GrpcClient("weaponsApi")
private InventoryServiceGrpc.InventoryServiceStub asyncStub;

public BulkCreateResponseDto createBulkItems(List<CreateItemDto> items) {
    // Llamada gRPC Client Streaming
    StreamObserver<CreateItemRequest> requestObserver = asyncStub.createBulkLoot(responseObserver);
    
    for (CreateItemDto item : items) {
        requestObserver.onNext(buildRequest(item));
    }
    requestObserver.onCompleted();
    // ...
}
```

---

## 🧪 Pruebas Manuales

### Requisitos Previos

```bash
cd DiablodexApi
docker-compose up --build
```

### 1. Obtener Token OAuth2

```bash
# Crear cliente OAuth2
curl -X POST 'http://localhost:4445/admin/clients' \
  -H 'Content-Type: application/json' \
  -d '{
    "client_id": "my-client",
    "client_secret": "my-secret",
    "grant_types": ["client_credentials"],
    "scope": "read write"
  }'

# Obtener token
TOKEN=$(curl -s -X POST 'http://localhost:4444/oauth2/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=client_credentials&client_id=my-client&client_secret=my-secret&scope=read write' \
  | jq -r '.access_token')

echo $TOKEN
```

### 2. Crear Items (Client Streaming via REST)

```bash
# Crear arma
curl -X POST 'http://localhost:8081/api/v1/items/bulk' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '[
    {
      "nombre": "Espada Legendaria",
      "tipo": "WEAPON",
      "poderDeObjeto": 800,
      "danoBase": 120,
      "habilidadPasiva": "Aumenta daño crítico 20%",
      "habilidadActiva": "Golpe devastador",
      "gemas": ["RUBI", "DIAMANTE"]
    }
  ]'

# Crear armadura
curl -X POST 'http://localhost:8081/api/v1/items/bulk' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '[
    {
      "nombre": "Coraza del Dragón",
      "tipo": "ARMOR",
      "poderDeObjeto": 650,
      "armaduraBase": 95,
      "habilidadPasiva": "Resistencia al fuego",
      "habilidadActiva": "Aura protectora",
      "gemas": ["ZAFIRO"]
    }
  ]'
```

### 3. Listar por Tipo (Server Streaming via REST)

```bash
# Listar todas las armas
curl -X GET 'http://localhost:8081/api/v1/items/type/WEAPON' \
  -H "Authorization: Bearer $TOKEN"

# Listar todas las armaduras
curl -X GET 'http://localhost:8081/api/v1/items/type/ARMOR' \
  -H "Authorization: Bearer $TOKEN"
```

### 4. Obtener Item por ID (Unary via REST)

```bash
# Reemplaza {id} con un ID real obtenido de los pasos anteriores
curl -X GET 'http://localhost:8081/api/v1/items/{id}' \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Probar Validaciones

```bash
# Error: Nombre vacío (400 INVALID_ARGUMENT)
curl -X POST 'http://localhost:8081/api/v1/items/bulk' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '[{"nombre": "", "tipo": "WEAPON", "poderDeObjeto": 100}]'

# Error: Poder fuera de rango (400 OUT_OF_RANGE)
curl -X POST 'http://localhost:8081/api/v1/items/bulk' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '[{"nombre": "Test", "tipo": "WEAPON", "poderDeObjeto": 1500}]'

# Error: Más de 2 gemas (400 INVALID_ARGUMENT)
curl -X POST 'http://localhost:8081/api/v1/items/bulk' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '[{"nombre": "Test", "tipo": "WEAPON", "poderDeObjeto": 100, "gemas": ["RUBI", "DIAMANTE", "ESMERALDA"]}]'

# Error: Item no existe (404 NOT_FOUND)
curl -X GET 'http://localhost:8081/api/v1/items/nonexistent-id' \
  -H "Authorization: Bearer $TOKEN"
```

### 6. Pruebas con Cliente gRPC Directo

```bash
# Instalar grpcurl
brew install grpcurl  # macOS
# o
go install github.com/fullstorydev/grpcurl/cmd/grpcurl@latest

# Listar servicios
grpcurl -plaintext localhost:50051 list

# GetItem (Unary)
grpcurl -plaintext -d '{"id": "weapon-001"}' \
  localhost:50051 diablopb.InventoryService/GetItem

# ListItemsByType (Server Streaming)
grpcurl -plaintext -d '{"tipo": 1}' \
  localhost:50051 diablopb.InventoryService/ListItemsByType
```

---

## 🚀 Ejecución

### Con Docker Compose (Recomendado)

```bash
cd DiablodexApi
docker-compose up --build
```

Servicios iniciados:
- `weapons-api`: gRPC server en puerto 50051
- `weapons-mongo`: MongoDB en puerto 27017
- `diablodex-api`: REST API en puerto 8081
- `diablo-api`: SOAP API en puerto 8055
- `hydra`: OAuth2 en puertos 4444/4445

### Desarrollo Local

```bash
# Instalar dependencias
pip install -r requirements.txt

# Generar archivos proto
python -m grpc_tools.protoc -I protos --python_out=protos --pyi_out=protos --grpc_python_out=protos protos/diablo_inventory.proto

# Ejecutar servidor
MONGO_URI=mongodb://localhost:27017 python server.py
```

---

## 📁 Estructura del Proyecto

```
WeaponsApi/
├── Dockerfile              # Imagen Docker
├── docker-compose.yml      # Compose standalone
├── README.md               # Esta documentación
├── requirements.txt        # Dependencias Python
├── server.py               # Servidor gRPC
└── protos/
    └── diablo_inventory.proto  # Definición del servicio
```

## 🔧 Variables de Entorno

| Variable | Default | Descripción |
|----------|---------|-------------|
| MONGO_URI | mongodb://localhost:27017 | URI de conexión MongoDB |
| MONGO_DB | diablo_weapons | Nombre de la base de datos |
| MONGO_COLLECTION | loot_items | Nombre de la colección |
