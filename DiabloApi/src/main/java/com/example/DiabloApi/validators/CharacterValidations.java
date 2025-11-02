package com.example.DiabloApi.validators;

import com.example.DiabloApi.entity.Stats;
import diablo.api.characters.*;

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

    // Validaciones para JAXB request types
    public static void validate(GetCharacterByIdRequest request) {
        if (request.getId() == null || request.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Character ID cannot be null or empty");
        }
    }

    public static void validate(GetCharacterByNameRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be null or empty");
        }
    }

    public static void validate(GetCharactersByClassRequest request) {
        if (request.getCharacterClass() == null) {
            throw new IllegalArgumentException("Character class cannot be null");
        }
    }

    public static void validate(CreateCharacterRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be null or empty");
        }
        if (request.getCharacterClass() == null) {
            throw new IllegalArgumentException("Character class cannot be null");
        }
        Integer level = request.getLevel();
        if (level != null && level < 1) {
            throw new IllegalArgumentException("Character level must be at least 1");
        }
    }

    public static void validate(UpdateCharacterRequest request) {
        if (request.getId() == null || request.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Character ID cannot be null or empty");
        }
        CharacterDetails details = request.getCharacterDetails();
        if (details == null) {
            throw new IllegalArgumentException("Character details cannot be null");
        }
        if (details.getName() == null || details.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be null or empty");
        }
        if (details.getCharacterClass() == null) {
            throw new IllegalArgumentException("Character class cannot be null");
        }
        if (details.getLevel() < 1) {
            throw new IllegalArgumentException("Character level must be at least 1");
        }
    }

    public static void validate(DeleteCharacterRequest request) {
        if (request.getId() == null || request.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Character ID cannot be null or empty");
        }
    }

}
