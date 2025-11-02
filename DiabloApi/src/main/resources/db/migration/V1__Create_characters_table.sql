-- Flyway Migration V1: Crear tabla characters
-- Filename: V1__Create_characters_table.sql

CREATE TABLE characters (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    character_class ENUM('BARBARIAN', 'SORCERER', 'DRUID', 'ROGUE', 'NECROMANCER', 'SPIRITBORN') NOT NULL,
    level INT DEFAULT 1,
    power INT DEFAULT 0,
    armor INT DEFAULT 0,
    life INT DEFAULT 100,
    strength INT DEFAULT 10,
    intelligence INT DEFAULT 10,
    willpower INT DEFAULT 10,
    dexterity INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices para mejorar performance
CREATE INDEX idx_character_name ON characters(name);
CREATE INDEX idx_character_class ON characters(character_class);
CREATE INDEX idx_character_level ON characters(level);