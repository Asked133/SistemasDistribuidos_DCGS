package com.example.DiabloApi.service;

import com.example.DiabloApi.entity.Character;
import com.example.DiabloApi.entity.Stats;
import com.example.DiabloApi.enums.CharacterClass;
import com.example.DiabloApi.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.DiabloApi.validators.CharacterValidations;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    // Metodo para crear un nuevo personaje con validaciones

    public Character createCharacter(String name, CharacterClass characterClass, int level,
            int power, int armor, int life, int strength,
            int intelligence, int willpower, int dexterity) {
        CharacterValidations.validateCharacterName(name);
        CharacterValidations.validateCharacterNameUnique(name, characterRepository);
        CharacterValidations.validateCharacterLevel(level);
        CharacterValidations.validateCharacterStats(new Stats(strength, intelligence, willpower, dexterity));


        Stats stats = new Stats(strength, intelligence, willpower, dexterity);


        Character character = new Character();
        character.setName(name);
        character.setCharacterClass(characterClass);
        character.setLevel(level);
        character.setPower(power);
        character.setArmor(armor);
        character.setLife(life);
        character.setStats(stats);

        return characterRepository.save(character);
    }


    // Metodo para buscar un personaje por su ID
    public Optional<Character> findById(UUID id) {
        return characterRepository.findById(id);
    }

    // Metodo para buscar un personaje por su nombre
    public Optional<Character> findByName(String name) {
        return characterRepository.findByName(name);
    }

    // Metodo para buscar personajes por su clase
    public List<Character> findByCharacterClass(CharacterClass characterClass) {
        return characterRepository.findByCharacterClass(characterClass);
    }

    // Metodo para obtener todos los personajes
    public List<Character> findAll() {
        return characterRepository.findAll();
    }

    // Metodo para actualizar un personaje
    public Character updateCharacter(UUID id, String name, CharacterClass characterClass, 
            Integer level, Integer power, Integer armor, Integer life,
            Integer strength, Integer intelligence, Integer willpower, Integer dexterity) {
        
        Optional<Character> existingCharacterOpt = characterRepository.findById(id);
        if (existingCharacterOpt.isEmpty()) {
            throw new IllegalArgumentException("Character with id " + id + " not found");
        }

        Character existingCharacter = existingCharacterOpt.get();
        
        // Update only provided fields
        if (name != null && !name.isEmpty()) {
            CharacterValidations.validateCharacterName(name);
            if (!existingCharacter.getName().equals(name)) {
                CharacterValidations.validateCharacterNameUnique(name, characterRepository);
            }
            existingCharacter.setName(name);
        }
        
        if (characterClass != null) {
            existingCharacter.setCharacterClass(characterClass);
        }
        
        if (level != null) {
            CharacterValidations.validateCharacterLevel(level);
            existingCharacter.setLevel(level);
        }
        
        if (power != null) {
            existingCharacter.setPower(power);
        }
        
        if (armor != null) {
            existingCharacter.setArmor(armor);
        }
        
        if (life != null) {
            existingCharacter.setLife(life);
        }
        
        // Update stats
        Stats stats = existingCharacter.getStats();
        if (strength != null) {
            stats.setStrength(strength);
        }
        if (intelligence != null) {
            stats.setIntelligence(intelligence);
        }
        if (willpower != null) {
            stats.setWillpower(willpower);
        }
        if (dexterity != null) {
            stats.setDexterity(dexterity);
        }
        
        CharacterValidations.validateCharacterStats(stats);
        existingCharacter.setStats(stats);
        
        return characterRepository.save(existingCharacter);
    }

    // Metodo para borrar un personaje
    public boolean deleteCharacter(UUID id) {
        Optional<Character> characterOpt = characterRepository.findById(id);
        if (characterOpt.isEmpty()) {
            return false;
        }
        characterRepository.deleteById(id);
        return true;
    }
}
