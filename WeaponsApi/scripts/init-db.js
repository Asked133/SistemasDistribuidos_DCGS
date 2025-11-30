// MongoDB Initialization Script for WeaponsApi
// Run with: mongosh mongodb://localhost:27017 < init-db.js

// Switch to diablo_weapons database
use diablo_weapons

// Drop existing collection if exists (for clean setup)
db.loot_items.drop()

// Create collection with JSON Schema validation
db.createCollection("loot_items", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["_id", "nombre", "tipo", "poder"],
         properties: {
            _id: {
               bsonType: "string",
               description: "ID único del item (UUID) - obligatorio"
            },
            nombre: {
               bsonType: "string",
               minLength: 1,
               description: "Nombre del item - obligatorio, no puede estar vacío"
            },
            tipo: {
               bsonType: "int",
               enum: [1, 2],
               description: "Tipo de item: 1=WEAPON, 2=ARMOR - obligatorio"
            },
            poder: {
               bsonType: "int",
               minimum: 0,
               maximum: 1000,
               description: "Poder del objeto (0-1000) - obligatorio"
            },
            dano: {
               bsonType: "int",
               minimum: 0,
               description: "Daño base (solo para tipo WEAPON)"
            },
            armadura: {
               bsonType: "int",
               minimum: 0,
               description: "Armadura base (solo para tipo ARMOR)"
            },
            pasiva: {
               bsonType: "string",
               description: "Habilidad pasiva del item"
            },
            activa: {
               bsonType: "string",
               description: "Habilidad activa del item"
            },
            gemas: {
               bsonType: "array",
               maxItems: 2,
               items: {
                  bsonType: "int",
                  enum: [0, 1, 2, 3, 4, 5, 6, 7]
               },
               description: "Gemas incrustadas (máximo 2). Valores: 0=UNKNOWN, 1=AMATISTA, 2=CRANEO, 3=DIAMANTE, 4=ESMERALDA, 5=RUBI, 6=TOPACIO, 7=ZAFIRO"
            }
         }
      }
   },
   validationLevel: "moderate",
   validationAction: "error"
})

print("✅ Collection 'loot_items' created with schema validation")

// Create index on 'tipo' field for efficient queries by type
db.loot_items.createIndex({ "tipo": 1 }, { background: true })
print("✅ Index on 'tipo' field created")

// Insert sample data
db.loot_items.insertMany([
  {
    _id: "sample-weapon-001",
    nombre: "Espada del Caos",
    tipo: 1, // WEAPON
    poder: 750,
    dano: 150,
    pasiva: "Aumenta daño crítico 15%",
    activa: "Torbellino de fuego",
    gemas: [5, 3] // RUBI, DIAMANTE
  },
  {
    _id: "sample-weapon-002",
    nombre: "Hacha de Guerra Ancestral",
    tipo: 1, // WEAPON
    poder: 820,
    dano: 180,
    pasiva: "Aumenta velocidad de ataque 10%",
    activa: "Golpe sísmico",
    gemas: [1] // AMATISTA
  },
  {
    _id: "sample-armor-001", 
    nombre: "Coraza de Hierro Ancestral",
    tipo: 2, // ARMOR
    poder: 600,
    armadura: 85,
    pasiva: "Reduce daño recibido 10%",
    activa: "Escudo de hielo",
    gemas: [4] // ESMERALDA
  },
  {
    _id: "sample-armor-002",
    nombre: "Yelmo del Dragón",
    tipo: 2, // ARMOR
    poder: 550,
    armadura: 65,
    pasiva: "Resistencia al fuego 25%",
    activa: "Aura protectora",
    gemas: [7, 6] // ZAFIRO, TOPACIO
  }
])

print("✅ Sample data inserted (4 items)")

// Verify data
print("\n📊 Collection Statistics:")
print("Total items: " + db.loot_items.countDocuments())
print("Weapons: " + db.loot_items.countDocuments({ tipo: 1 }))
print("Armor: " + db.loot_items.countDocuments({ tipo: 2 }))

print("\n📋 Sample Items:")
db.loot_items.find().forEach(function(doc) {
    print("  - " + doc._id + ": " + doc.nombre + " (tipo: " + (doc.tipo == 1 ? "WEAPON" : "ARMOR") + ", poder: " + doc.poder + ")")
})

print("\n✨ MongoDB initialization complete!")
