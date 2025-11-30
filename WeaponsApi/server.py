import sys
import os
import time

sys.path.append(os.path.join(os.path.dirname(__file__), "protos"))

import grpc
from concurrent import futures
from pymongo import MongoClient
from pymongo.errors import DuplicateKeyError, ServerSelectionTimeoutError
from dotenv import load_dotenv

import diablo_inventory_pb2
import diablo_inventory_pb2_grpc

load_dotenv()

MONGO_URI = os.getenv("MONGO_URI", "mongodb://localhost:27017")
MONGO_DB = os.getenv("MONGO_DB", "diablo_weapons")
MONGO_COLLECTION = os.getenv("MONGO_COLLECTION", "loot_items")

# Retry connection to MongoDB
def get_mongo_collection(max_retries=10, delay=2):
    for attempt in range(max_retries):
        try:
            mongo_client = MongoClient(MONGO_URI, serverSelectionTimeoutMS=5000)
            # Test connection
            mongo_client.admin.command('ping')
            db = mongo_client[MONGO_DB]
            coll = db[MONGO_COLLECTION]
            # Create index if it doesn't exist
            existing_indexes = [idx['key'] for idx in coll.list_indexes()]
            if [('tipo', 1)] not in existing_indexes:
                coll.create_index("tipo", background=True)
            print(f"Connected to MongoDB successfully on attempt {attempt + 1}")
            return coll
        except ServerSelectionTimeoutError as e:
            print(f"MongoDB connection attempt {attempt + 1}/{max_retries} failed: {e}")
            if attempt < max_retries - 1:
                time.sleep(delay)
            else:
                raise

collection = None

class InventoryService(diablo_inventory_pb2_grpc.InventoryServiceServicer):

    def GetItem(self, request, context):
        if not request.id:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "ID es obligatorio")

        doc = collection.find_one({"_id": request.id})
        if not doc:
            context.abort(grpc.StatusCode.NOT_FOUND, f"Item {request.id} no existe")

        return self._to_item_response(doc)

    def ListItemsByType(self, request, context):
        if request.tipo == diablo_inventory_pb2.ItemType.UNKNOWN_TYPE:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Tipo es obligatorio")

        cursor = collection.find({"tipo": request.tipo})
        for doc in cursor:
            yield self._to_item_response(doc)

    def CreateBulkLoot(self, request_iterator, context):
        count = 0
        for req in request_iterator:
            self._validate_create_request(req, context)

            doc = {
                "_id": req.id,
                "nombre": req.nombre,
                "tipo": req.tipo,
                "poder": req.poder_de_objeto,
                "pasiva": req.habilidad_pasiva,
                "activa": req.habilidad_activa,
                "gemas": list(req.gemas),
            }

            campo = req.WhichOneof('datos_propios')
            if campo == 'dano_base':
                doc["dano"] = req.dano_base
            elif campo == 'armadura_base':
                doc["armadura"] = req.armadura_base

            try:
                collection.insert_one(doc)
            except DuplicateKeyError:
                context.abort(grpc.StatusCode.ALREADY_EXISTS, f"ID {req.id} ya existe")

            count += 1

        return diablo_inventory_pb2.BulkCreateResponse(
            items_creados=count,
            mensaje="Loot masivo almacenado correctamente"
        )

    def _validate_create_request(self, req, context):
        if not req.id:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "ID es obligatorio")
        if not req.nombre:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Nombre es obligatorio")
        if req.tipo == diablo_inventory_pb2.ItemType.UNKNOWN_TYPE:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Tipo es invalido")
        if not (0 <= req.poder_de_objeto <= 1000):
            context.abort(grpc.StatusCode.OUT_OF_RANGE, "Poder fuera de rango (0-1000)")
        if len(req.gemas) > 2:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Maximo 2 gemas permitidas")
        campo = req.WhichOneof('datos_propios')
        if campo not in ('dano_base', 'armadura_base'):
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Debe especificar dano o armadura")

    def _to_item_response(self, doc):
        resp = diablo_inventory_pb2.ItemResponse(
            id=doc.get('_id', ''),
            nombre=doc.get('nombre', ''),
            tipo=doc.get('tipo', diablo_inventory_pb2.ItemType.UNKNOWN_TYPE),
            poder_de_objeto=doc.get('poder', 0),
            habilidad_pasiva=doc.get('pasiva', ''),
            habilidad_activa=doc.get('activa', ''),
            gemas_incrustadas=doc.get('gemas', []),
        )

        if 'dano' in doc:
            resp.dano_base = doc['dano']
        if 'armadura' in doc:
            resp.armadura_base = doc['armadura']

        return resp

def serve():
    global collection
    collection = get_mongo_collection()
    
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    diablo_inventory_pb2_grpc.add_InventoryServiceServicer_to_server(InventoryService(), server)
    server.add_insecure_port('[::]:50051')
    print("Servidor gRPC WeaponsApi escuchando en puerto 50051...")
    server.start()
    server.wait_for_termination()

if __name__ == '__main__':
    serve()
