package com.example.DiabloApi.mapper;

import com.example.DiabloApi.dto.response.CharacterListResponseDto;
import com.example.DiabloApi.dto.response.CharacterResponseDto;
import com.example.DiabloApi.entity.Character;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CharacterMapper {

    // Convierte un objeto Character (entidad) a CharacterResponseDto
    public CharacterResponseDto toCharacterResponse(Character character) {
        if (character == null) return null;

        CharacterResponseDto dto = new CharacterResponseDto();
        dto.setId(character.getId().toString());
        dto.setName(character.getName());
        dto.setCharacterClass(character.getCharacterClass().name());
        dto.setLevel(character.getLevel());
        dto.setPower(character.getPower());
        dto.setArmor(character.getArmor());
        dto.setLife(character.getLife());
        if (character.getStats() != null) {
            dto.setStrength(character.getStats().getStrength());
            dto.setIntelligence(character.getStats().getIntelligence());
            dto.setWillpower(character.getStats().getWillpower());
            dto.setDexterity(character.getStats().getDexterity());
        }
        return dto;
    }

    // Convierte una lista de Entidades a nuestro DTO de lista
    public CharacterListResponseDto toCharacterListResponse(List<Character> characters) {
        CharacterListResponseDto response = new CharacterListResponseDto();
        if (characters != null) {
            List<CharacterResponseDto> dtoList = characters.stream()
                .map(this::toCharacterResponse)
                .collect(Collectors.toList());
            response.setCharacters(dtoList);
        } else {
            response.setCharacters(new ArrayList<>());
        }
        response.setMessage("Characters listed successfully");
        return response;
    }
}