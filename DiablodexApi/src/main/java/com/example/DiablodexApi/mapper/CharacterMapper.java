package com.example.DiablodexApi.mapper;

import com.example.DiablodexApi.dto.*;
import com.example.DiablodexApi.soap.client.generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

    private static final Logger logger = LoggerFactory.getLogger(CharacterMapper.class);

    public CharacterResponse toRestDto(CharacterDetails soapDetails) {
        logger.debug("===== MAPPER DEBUG =====");
        logger.debug("soapDetails is null? {}", soapDetails == null);
        
        if (soapDetails == null) return null;
        
        logger.debug("ID: {}", soapDetails.getId());
        logger.debug("Name: {}", soapDetails.getName());
        logger.debug("Level: {}", soapDetails.getLevel());
        logger.debug("CharacterClass: {}", soapDetails.getCharacterClass());
        logger.debug("Power: {}", soapDetails.getPower());
        logger.debug("Armor: {}", soapDetails.getArmor());
        logger.debug("Life: {}", soapDetails.getLife());
        logger.debug("Strength: {}", soapDetails.getStrength());
        logger.debug("========================");
        
        // Validar que los campos obligatorios no sean nulos
        if (soapDetails.getId() == null || soapDetails.getName() == null || 
            soapDetails.getLevel() == null || soapDetails.getCharacterClass() == null) {
            logger.error("Required fields are null! Returning null.");
            return null;
        }
        
        CharacterResponse restDto = new CharacterResponse();
        restDto.setId(soapDetails.getId()); 
        restDto.setName(soapDetails.getName());
        restDto.setLevel(soapDetails.getLevel() != null ? soapDetails.getLevel() : 0);
        restDto.setCharacterClass(soapDetails.getCharacterClass().value()); 
        restDto.setPower(soapDetails.getPower() != null ? soapDetails.getPower() : 0);
        restDto.setArmor(soapDetails.getArmor() != null ? soapDetails.getArmor() : 0);
        restDto.setLife(soapDetails.getLife() != null ? soapDetails.getLife() : 0);

        StatsDto statsDto = new StatsDto();
        statsDto.setStrength(soapDetails.getStrength() != null ? soapDetails.getStrength() : 0);
        statsDto.setIntelligence(soapDetails.getIntelligence() != null ? soapDetails.getIntelligence() : 0);
        statsDto.setWillpower(soapDetails.getWillpower() != null ? soapDetails.getWillpower() : 0);
        statsDto.setDexterity(soapDetails.getDexterity() != null ? soapDetails.getDexterity() : 0);
        restDto.setStats(statsDto);
        
        return restDto;
    }

    public com.example.DiablodexApi.soap.client.generated.CreateCharacterRequest toSoapDto(com.example.DiablodexApi.dto.CreateCharacterRequest restDto) {
        if (restDto == null) return null;
        
        var soapDto = new com.example.DiablodexApi.soap.client.generated.CreateCharacterRequest();
        
        soapDto.setName(restDto.getName());
        soapDto.setLevel(restDto.getLevel());
        soapDto.setCharacterClass(CharacterClassEnum.fromValue(restDto.getCharacterClass().toUpperCase()));
        soapDto.setPower(restDto.getPower());
        soapDto.setArmor(restDto.getArmor());
        soapDto.setLife(restDto.getLife());

        if (restDto.getStats() != null) {
            soapDto.setStrength(restDto.getStats().getStrength());
            soapDto.setIntelligence(restDto.getStats().getIntelligence());
            soapDto.setWillpower(restDto.getStats().getWillpower());
            soapDto.setDexterity(restDto.getStats().getDexterity());
        }
        return soapDto;
    }

    public com.example.DiablodexApi.soap.client.generated.UpdateCharacterRequest toSoapDto(String id, com.example.DiablodexApi.dto.UpdateCharacterRequest restDto) {
        if (restDto == null) return null;
        
        var soapRequest = new com.example.DiablodexApi.soap.client.generated.UpdateCharacterRequest();
        
        soapRequest.setId(id);
        
        var details = new CharacterDetails();
        details.setName(restDto.getName());
        details.setLevel(restDto.getLevel());
        details.setCharacterClass(CharacterClassEnum.fromValue(restDto.getCharacterClass().toUpperCase()));
        details.setPower(restDto.getPower());
        details.setArmor(restDto.getArmor());
        details.setLife(restDto.getLife());
        
        if (restDto.getStats() != null) {
            details.setStrength(restDto.getStats().getStrength());
            details.setIntelligence(restDto.getStats().getIntelligence());
            details.setWillpower(restDto.getStats().getWillpower());
            details.setDexterity(restDto.getStats().getDexterity());
        }
        
        soapRequest.setCharacterDetails(details);
        
        return soapRequest;
    }
}