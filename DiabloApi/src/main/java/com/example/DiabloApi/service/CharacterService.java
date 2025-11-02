package com.example.DiabloApi.service;

import com.example.DiabloApi.entity.Character;
import com.example.DiabloApi.enums.CharacterClass;
import com.example.DiabloApi.repository.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository repository;

    @Transactional(readOnly = true)
    public Optional<Character> getById(UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Character> getByName(String name) {
        return repository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Character> getAllByClass(CharacterClass characterClass) {
        return repository.findByCharacterClass(characterClass);
    }

    /**
     * ================== CORRECCIÓN AQUÍ ==================
     *
     * El método "repository.save(character)" devuelve la entidad
     * guardada y actualizada (con el ID).
     * Debemos retornar esa instancia.
     *
     * @param character La entidad a guardar (sin ID)
     * @return La entidad guardada (CON ID)
     */
    @Transactional
    public Character createCharacter(Character character) {
        // ANTES:
        // repository.save(character);
        // return character; // <-- Esto devolvía la entidad original sin ID
        
        // AHORA:
        return repository.save(character);
    }
    
    /**
     * ================== CORRECCIÓN AQUÍ ==================
     *
     * Al igual que en create, el método "save" para actualizar
     * también devuelve la entidad actualizada.
     *
     * @param existingCharacter La entidad actualizada
     * @return La entidad guardada en la BBDD
     */
    @Transactional
    public Character updateCharacter(Character existingCharacter) {
        // ANTES:
        // repository.save(existingCharacter);
        // return existingCharacter;
        
        // AHORA:
        return repository.save(existingCharacter);
    }

    @Transactional
    public void deleteCharacter(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Character not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return repository.findByName(name).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}