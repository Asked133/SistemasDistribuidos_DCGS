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
}
