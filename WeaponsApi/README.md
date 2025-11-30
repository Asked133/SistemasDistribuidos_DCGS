# WeaponsApi - gRPC Service

A Python gRPC service for managing Diablo IV inventory items (weapons and armor).

## Features

- **Unary RPC**: Get a single item by ID
- **Server Streaming RPC**: List all items by type (WEAPON or ARMOR)
- **Client Streaming RPC**: Create multiple items in bulk

## Proto Definition

The service uses `diablo_inventory.proto` with the following:

### Messages
- `ItemRequest`: Request for getting an item by ID
- `ItemTypeRequest`: Request for listing items by type
- `CreateItemRequest`: Request for creating an item
- `ItemResponse`: Response containing item details
- `BulkCreateResponse`: Response for bulk creation

### Enums
- `ItemType`: UNKNOWN_TYPE, WEAPON, ARMOR
- `GemType`: AMATISTA, CRANEO, DIAMANTE, ESMERALDA, RUBI, TOPACIO, ZAFIRO

## Running with Docker Compose

```bash
docker-compose up --build
```

This will start:
- `weapons-api`: gRPC server on port 50051
- `weapons-mongo`: MongoDB on port 27017

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| MONGO_URI | mongodb://localhost:27017 | MongoDB connection string |
| MONGO_DB | diablo_weapons | Database name |
| MONGO_COLLECTION | loot_items | Collection name |

## API Usage

The service is consumed by DiablodexApi through REST endpoints:

- `GET /api/v1/items/{id}` - Get item by ID (Unary RPC)
- `GET /api/v1/items/type/{type}` - List items by type (Server Streaming)
- `POST /api/v1/items/bulk` - Create items in bulk (Client Streaming)

## Development

### Prerequisites
- Python 3.11+
- pip

### Install dependencies
```bash
pip install -r requirements.txt
```

### Generate proto files
```bash
python -m grpc_tools.protoc -I protos --python_out=protos --pyi_out=protos --grpc_python_out=protos protos/diablo_inventory.proto
```

### Run the server
```bash
python server.py
```
