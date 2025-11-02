package com.example.DiabloApi.mapper;

import com.example.DiabloApi.entity.Character;
import com.example.DiabloApi.entity.Stats;
import com.example.DiabloApi.enums.CharacterClass;
// Asegúrate de que este import sea el de tus clases generadas por JAXB/XSD
import diablo.api.characters.*; 
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

    /**
     * Este método AHORA SÍ recibirá una entidad con ID,
     * porque el servicio y el endpoint fueron corregidos.
     */
    public CharacterDetails toSoapDto(Character entity) {
        if (entity == null) {
            return null;
        }

        CharacterDetails dto = new CharacterDetails();
        
        // Esta línea ya no fallará
        dto.setId(entity.getId().toString()); 
        
        dto.setName(entity.getName());
        dto.setLevel(entity.getLevel());
        dto.setCharacterClass(CharacterClassEnum.fromValue(entity.getCharacterClass().name()));
        dto.setPower(entity.getPower());
        dto.setArmor(entity.getArmor());
        dto.setLife(entity.getLife());

        if (entity.getStats() != null) {
            Stats stats = entity.getStats();
            dto.setStrength(stats.getStrength());
            dto.setIntelligence(stats.getIntelligence());
            dto.setWillpower(stats.getWillpower());
            dto.setDexterity(stats.getDexterity());
        }

        return dto;
    }

    // --- Mapeo de Request (Crear) ---
    public Character toEntity(CreateCharacterRequest dto) {
        if (dto == null) return null;

        Character entity = new Character();
        entity.setName(dto.getName());
        entity.setLevel(dto.getLevel());
        entity.setCharacterClass(toEntityEnum(dto.getCharacterClass()));
        entity.setPower(dto.getPower());
        entity.setArmor(dto.getArmor());
        entity.setLife(dto.getLife());

        Stats stats = new Stats();
        stats.setStrength(dto.getStrength());
        stats.setIntelligence(dto.getIntelligence());
        stats.setWillpower(dto.getWillpower());
        stats.setDexterity(dto.getDexterity());

        // Asigna la relación bidireccional
        entity.setStats(stats); 
        
        return entity;
    }
    
    // --- Mapeo de Request (Actualizar) ---
    public void updateEntityFromSoapDto(CharacterDetails dto, Character entity) {
        if (dto == null || entity == null) return;
        
        entity.setName(dto.getName());
        entity.setLevel(dto.getLevel());
        entity.setCharacterClass(toEntityEnum(dto.getCharacterClass()));
        entity.setPower(dto.getPower());
        entity.setArmor(dto.getArmor());
        entity.setLife(dto.getLife());
        
        if (entity.getStats() == null) {
            entity.setStats(new Stats());
        }
        
        Stats stats = entity.getStats();
        stats.setStrength(dto.getStrength());
        stats.setIntelligence(dto.getIntelligence());
        stats.setWillpower(dto.getWillpower());
        stats.setDexterity(dto.getDexterity());
    }

    // --- Helpers de Enums ---
    public CharacterClass toEntityEnum(CharacterClassEnum soapEnum) {
        if (soapEnum == null) return null;
        return CharacterClass.valueOf(soapEnum.value());
    }

    public CharacterClassEnum toSoapEnum(CharacterClass entityEnum) {
        if (entityEnum == null) return null;
        return CharacterClassEnum.fromValue(entityEnum.name());
    }
}