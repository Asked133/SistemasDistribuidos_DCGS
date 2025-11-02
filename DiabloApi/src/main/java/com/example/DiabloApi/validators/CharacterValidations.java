package com.example.DiabloApi.validators;

import com.example.DiabloApi.entity.Stats;

public class CharacterValidations {

    // Valida que el nombre del personaje no sea nulo o vacío
    public static void validateCharacterName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be null or empty");
        }
    }

    // Valida que el nivel del personaje sea al menos 1
    public static void validateCharacterLevel(int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Character level must be at least 1");
        }
    }

    // Valida que las estadísticas del personaje no sean nulas y no contengan valores negativos
    public static void validateCharacterStats(Stats stats) {
        if (stats == null) {
            throw new IllegalArgumentException("Character stats cannot be null");
        }
        if (stats.getStrength() < 0 || stats.getIntelligence() < 0 || stats.getWillpower() < 0 || stats.getDexterity() < 0) {
            throw new IllegalArgumentException("Stats values cannot be negative");
        }
    }

    // Valida que el nombre del personaje sea único en la base de datos
    public static void validateCharacterNameUnique(String name, com.example.DiabloApi.repository.CharacterRepository characterRepository) {
        if (characterRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Character name must be unique");
        }
    }

}
