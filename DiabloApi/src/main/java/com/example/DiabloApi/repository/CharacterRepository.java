package com.example.DiabloApi.repository;

import com.example.DiabloApi.entity.Character;
import com.example.DiabloApi.enums.CharacterClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, UUID> {
    
    // JpaRepository ya incluye:
    // save() - para CREATE
    // findById() - para GET by ID
    // Ademas declaro dos metodos personalizados
    
    Optional<Character> findByName(String name);
    List<Character> findByCharacterClass(CharacterClass characterClass);
}
