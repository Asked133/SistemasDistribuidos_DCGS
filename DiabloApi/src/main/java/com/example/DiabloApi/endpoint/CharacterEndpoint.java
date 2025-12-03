package com.example.DiabloApi.endpoint;

import com.example.DiabloApi.dto.DeleteCharacterResponseDto;
import com.example.DiabloApi.dto.request.*;
import com.example.DiabloApi.dto.response.CharacterListResponseDto;
import com.example.DiabloApi.dto.response.CharacterResponseDto;
import com.example.DiabloApi.entity.Character;
import com.example.DiabloApi.enums.CharacterClass;
import com.example.DiabloApi.mapper.CharacterMapper;
import com.example.DiabloApi.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import jakarta.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Endpoint
public class CharacterEndpoint {
    private static final String NAMESPACE_URI = "http://DiabloApi.example.com/characters";

    @Autowired
    private CharacterService characterService;

    
    private final CharacterMapper characterMapper = new CharacterMapper();

    private JAXBElement<CharacterResponseDto> createResponse(CharacterResponseDto dto, String rootElementName) {
        QName qName = new QName(NAMESPACE_URI, rootElementName);
        return new JAXBElement<>(qName, CharacterResponseDto.class, dto);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createCharacterRequest")
    @ResponsePayload
    public JAXBElement<CharacterResponseDto> createCharacter(@RequestPayload CreateCharacterRequestDto request) {
        CharacterResponseDto responseDto = new CharacterResponseDto();
        try {
            String classString = request.getCharacterClass() != null ? request.getCharacterClass().toUpperCase() : null;
            Character character = characterService.createCharacter(
                request.getName(), CharacterClass.valueOf(classString),
                request.getLevel() != null ? request.getLevel() : 1,
                request.getPower() != null ? request.getPower() : 0,
                request.getArmor() != null ? request.getArmor() : 0,
                request.getLife() != null ? request.getLife() : 100,
                request.getStrength(), request.getIntelligence(),
                request.getWillpower(), request.getDexterity()
            );
            responseDto = characterMapper.toCharacterResponse(character);
            responseDto.setMessage("Character created successfully");
        } catch (Exception e) {
            responseDto.setMessage("Error creating character: " + e.getMessage());
        }
        return createResponse(responseDto, "createCharacterResponse");
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCharacterByIdRequest")
    @ResponsePayload
    public JAXBElement<CharacterResponseDto> getCharacterById(@RequestPayload GetCharacterByIdRequestDto request) {
        CharacterResponseDto responseDto = new CharacterResponseDto();
        try {
            UUID id = UUID.fromString(request.getId());
            Optional<Character> characterOpt = characterService.findById(id);
            if (characterOpt.isPresent()) {
                responseDto = characterMapper.toCharacterResponse(characterOpt.get());
                responseDto.setMessage("Character found");
            } else {
                responseDto.setMessage("Character not found with ID: " + request.getId());
            }
        } catch (Exception e) {
            responseDto.setMessage("Error finding character by ID: " + e.getMessage());
        }
        return createResponse(responseDto, "getCharacterByIdResponse");
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCharacterByNameRequest")
    @ResponsePayload
    public JAXBElement<CharacterResponseDto> getCharacterByName(@RequestPayload GetCharacterByNameRequestDto request) {
        CharacterResponseDto responseDto = new CharacterResponseDto();
        try {
            Optional<Character> characterOpt = characterService.findByName(request.getName());
            if (characterOpt.isPresent()) {
                responseDto = characterMapper.toCharacterResponse(characterOpt.get());
                responseDto.setMessage("Character found");
            } else {
                responseDto.setMessage("Character not found with name: " + request.getName());
            }
        } catch (Exception e) {
            responseDto.setMessage("Error finding character by name: " + e.getMessage());
        }
        return createResponse(responseDto, "getCharacterByNameResponse");
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCharactersByClassRequest")
    @ResponsePayload
    public CharacterListResponseDto getCharactersByClass(@RequestPayload GetCharactersByClassRequestDto request) {
        try {
            String classString = request.getCharacterClass() != null ? request.getCharacterClass().toUpperCase() : null;
            List<Character> characters = characterService.findByCharacterClass(CharacterClass.valueOf(classString));
            return characterMapper.toCharacterListResponse(characters);
        } catch (Exception e) {
            CharacterListResponseDto error = new CharacterListResponseDto();
            error.setMessage("Error listing characters by class: " + e.getMessage());
            return error;
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllCharactersRequest")
    @ResponsePayload
    public CharacterListResponseDto getAllCharacters(@RequestPayload GetAllCharactersRequestDto request) {
        try {
            List<Character> characters = characterService.findAll();
            return characterMapper.toCharacterListResponse(characters);
        } catch (Exception e) {
            CharacterListResponseDto error = new CharacterListResponseDto();
            error.setMessage("Error listing all characters: " + e.getMessage());
            return error;
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateCharacterRequest")
    @ResponsePayload
    public JAXBElement<CharacterResponseDto> updateCharacter(@RequestPayload UpdateCharacterRequestDto request) {
        CharacterResponseDto responseDto = new CharacterResponseDto();
        try {
            UUID id = UUID.fromString(request.getId());
            String classString = request.getCharacterClass() != null ? request.getCharacterClass().toUpperCase() : null;
            CharacterClass charClass = classString != null ? CharacterClass.valueOf(classString) : null;
            
            Character character = characterService.updateCharacter(
                id, request.getName(), charClass,
                request.getLevel(), request.getPower(), request.getArmor(),
                request.getLife(), request.getStrength(), request.getIntelligence(),
                request.getWillpower(), request.getDexterity()
            );
            responseDto = characterMapper.toCharacterResponse(character);
            responseDto.setMessage("Character updated successfully");
        } catch (IllegalArgumentException e) {
            responseDto.setMessage("Error: " + e.getMessage());
        } catch (Exception e) {
            responseDto.setMessage("Error updating character: " + e.getMessage());
        }
        return createResponse(responseDto, "updateCharacterResponse");
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteCharacterRequest")
    @ResponsePayload
    public DeleteCharacterResponseDto deleteCharacter(@RequestPayload DeleteCharacterRequestDto request) {
        DeleteCharacterResponseDto responseDto = new DeleteCharacterResponseDto();
        try {
            UUID id = UUID.fromString(request.getId());
            boolean deleted = characterService.deleteCharacter(id);
            if (deleted) {
                responseDto.setSuccess(true);
                responseDto.setMessage("Character deleted successfully");
            } else {
                responseDto.setSuccess(false);
                responseDto.setMessage("Character not found with ID: " + request.getId());
            }
        } catch (Exception e) {
            responseDto.setSuccess(false);
            responseDto.setMessage("Error deleting character: " + e.getMessage());
        }
        return responseDto;
    }
}