package com.example.DiablodexApi.service;

import com.example.DiablodexApi.dto.CharacterDto;
import com.example.DiablodexApi.dto.CreateCharacterDto;
import com.example.DiablodexApi.dto.PagedCharacterResponse;
import com.example.DiablodexApi.dto.UpdateCharacterDto;
import com.example.DiablodexApi.exception.CharacterAlreadyExistsException;
import com.example.DiablodexApi.exception.CharacterNotFoundException;
import com.example.DiablodexApi.exception.SoapServiceException;
import com.example.DiablodexApi.soap.client.generated.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import jakarta.xml.bind.JAXBElement;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CharacterService {

    private final WebServiceTemplate webServiceTemplate;
    private final ObjectFactory objectFactory = new ObjectFactory();

    @Cacheable(value = "characters", key = "#id")
    public CharacterDto getCharacterById(String id) {
        try {
            log.info("Fetching character with id: {}", id);
            GetCharacterByIdRequest request = objectFactory.createGetCharacterByIdRequest();
            request.setId(id);

            Object responseObj = webServiceTemplate.marshalSendAndReceive(request);
            CharacterDetails details;
            
            if (responseObj instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<CharacterDetails> jaxbElement = (JAXBElement<CharacterDetails>) responseObj;
                details = jaxbElement.getValue();
            } else if (responseObj instanceof CharacterDetails) {
                details = (CharacterDetails) responseObj;
            } else {
                throw new SoapServiceException("Unexpected SOAP response type: " + responseObj.getClass().getName());
            }
            
            log.info("Successfully fetched character, mapping to DTO...");
            CharacterDto dto = mapToDto(details);
            log.info("DTO created successfully, returning response for character: {}", dto.getName());
            return dto;
        } catch (Exception e) {
            log.error("Error fetching character by id: {}", id, e);
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                throw new CharacterNotFoundException("Character with id " + id + " not found");
            }
            throw new SoapServiceException("Error calling SOAP service: " + e.getMessage());
        }
    }

    @Cacheable(value = "charactersList", key = "#characterClass + '_' + #sort + '_' + #page + '_' + #size")
    public PagedCharacterResponse getAllCharacters(String characterClass, String sort, int page, int size) {
        try {
            log.info("Fetching all characters - class: {}, sort: {}, page: {}, size: {}", 
                characterClass, sort, page, size);
            GetAllCharactersRequest request = objectFactory.createGetAllCharactersRequest();

            // The SOAP service returns the response object directly, not wrapped in JAXBElement
            Object responseObj = webServiceTemplate.marshalSendAndReceive(request);
            GetAllCharactersResponse allResponse;
            
            if (responseObj instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<GetAllCharactersResponse> jaxbElement = (JAXBElement<GetAllCharactersResponse>) responseObj;
                allResponse = jaxbElement.getValue();
            } else if (responseObj instanceof GetAllCharactersResponse) {
                allResponse = (GetAllCharactersResponse) responseObj;
            } else {
                throw new SoapServiceException("Unexpected SOAP response type: " + responseObj.getClass().getName());
            }
            
            List<CharacterDetails> allCharacters = allResponse.getCharacters();

            // Filter by class if specified
            List<CharacterDetails> filtered = allCharacters;
            if (characterClass != null && !characterClass.isEmpty()) {
                filtered = allCharacters.stream()
                    .filter(c -> c.getCharacterClass() != null && 
                                c.getCharacterClass().value().equalsIgnoreCase(characterClass))
                    .collect(Collectors.toList());
            }

            // Sort
            if (sort != null && !sort.isEmpty()) {
                String[] sortParts = sort.split(",");
                String field = sortParts[0];
                boolean desc = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1]);

                Comparator<CharacterDetails> comparator = getComparator(field);
                if (comparator != null) {
                    filtered.sort(desc ? comparator.reversed() : comparator);
                }
            }

            // Pagination
            int totalElements = filtered.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int start = page * size;
            List<CharacterDetails> paged = start < totalElements ?
                filtered.subList(start, Math.min(start + size, totalElements)) :
                List.of();

            List<CharacterDto> dtos = paged.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

            log.info("Mapped {} characters to DTOs, building response...", dtos.size());
            PagedCharacterResponse response = PagedCharacterResponse.builder()
                .characters(dtos)
                .page(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
            log.info("Response built successfully, returning {} characters", dtos.size());
            return response;
        } catch (Exception e) {
            log.error("Error fetching all characters", e);
            throw new SoapServiceException("Error calling SOAP service: " + e.getMessage());
        }
    }

    @CacheEvict(value = {"characters", "charactersList"}, allEntries = true)
    public CharacterDto createCharacter(CreateCharacterDto request) {
        try {
            log.info("Creating character with name: {}", request.getName());
            
            // Create SOAP request using the generated class
            com.example.DiablodexApi.soap.client.generated.CreateCharacterRequest soapRequest = 
                objectFactory.createCreateCharacterRequest();
            
            soapRequest.setName(request.getName());
            soapRequest.setCharacterClass(CharacterClassEnum.fromValue(request.getCharacterClass()));
            soapRequest.setLevel(request.getLevel() != null ? request.getLevel() : 1);
            soapRequest.setPower(request.getPower() != null ? request.getPower() : 0);
            soapRequest.setArmor(request.getArmor() != null ? request.getArmor() : 0);
            soapRequest.setLife(request.getLife() != null ? request.getLife() : 100);
            soapRequest.setStrength(request.getStrength());
            soapRequest.setIntelligence(request.getIntelligence());
            soapRequest.setWillpower(request.getWillpower());
            soapRequest.setDexterity(request.getDexterity());

            Object responseObj = webServiceTemplate.marshalSendAndReceive(soapRequest);
            CharacterDetails details;
            
            if (responseObj instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<CharacterDetails> jaxbElement = (JAXBElement<CharacterDetails>) responseObj;
                details = jaxbElement.getValue();
            } else if (responseObj instanceof CharacterDetails) {
                details = (CharacterDetails) responseObj;
            } else {
                throw new SoapServiceException("Unexpected SOAP response type: " + responseObj.getClass().getName());
            }
            
            log.info("Character created successfully with id: {}", details.getId());
            return mapToDto(details);
        } catch (Exception e) {
            log.error("Error creating character", e);
            if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                throw new CharacterAlreadyExistsException("Character with name " + request.getName() + " already exists");
            }
            throw new SoapServiceException("Error calling SOAP service: " + e.getMessage());
        }
    }

    @CacheEvict(value = {"characters", "charactersList"}, allEntries = true)
    public CharacterDto updateCharacter(String id, CreateCharacterDto request) {
        try {
            log.info("Updating character with id: {}", id);
            
            // Create SOAP request using the generated class
            com.example.DiablodexApi.soap.client.generated.UpdateCharacterRequest soapRequest = 
                objectFactory.createUpdateCharacterRequest();
            
            soapRequest.setId(id);
            soapRequest.setName(request.getName());
            soapRequest.setCharacterClass(CharacterClassEnum.fromValue(request.getCharacterClass()));
            soapRequest.setLevel(request.getLevel());
            soapRequest.setPower(request.getPower());
            soapRequest.setArmor(request.getArmor());
            soapRequest.setLife(request.getLife());
            soapRequest.setStrength(request.getStrength());
            soapRequest.setIntelligence(request.getIntelligence());
            soapRequest.setWillpower(request.getWillpower());
            soapRequest.setDexterity(request.getDexterity());

            Object responseObj = webServiceTemplate.marshalSendAndReceive(soapRequest);
            CharacterDetails details;
            
            if (responseObj instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<CharacterDetails> jaxbElement = (JAXBElement<CharacterDetails>) responseObj;
                details = jaxbElement.getValue();
            } else if (responseObj instanceof CharacterDetails) {
                details = (CharacterDetails) responseObj;
            } else {
                throw new SoapServiceException("Unexpected SOAP response type: " + responseObj.getClass().getName());
            }
            
            log.info("Character updated successfully: {}", id);
            return mapToDto(details);
        } catch (Exception e) {
            log.error("Error updating character with id: {}", id, e);
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                throw new CharacterNotFoundException("Character with id " + id + " not found");
            }
            throw new SoapServiceException("Error calling SOAP service: " + e.getMessage());
        }
    }

    @CacheEvict(value = {"characters", "charactersList"}, allEntries = true)
    public CharacterDto patchCharacter(String id, UpdateCharacterDto request) {
        try {
            log.info("Patching character with id: {}", id);
            
            // Create SOAP request using the generated class
            com.example.DiablodexApi.soap.client.generated.UpdateCharacterRequest soapRequest = 
                objectFactory.createUpdateCharacterRequest();
            
            soapRequest.setId(id);
            if (request.getName() != null) soapRequest.setName(request.getName());
            if (request.getCharacterClass() != null) {
                soapRequest.setCharacterClass(CharacterClassEnum.fromValue(request.getCharacterClass()));
            }
            if (request.getLevel() != null) soapRequest.setLevel(request.getLevel());
            if (request.getPower() != null) soapRequest.setPower(request.getPower());
            if (request.getArmor() != null) soapRequest.setArmor(request.getArmor());
            if (request.getLife() != null) soapRequest.setLife(request.getLife());
            if (request.getStrength() != null) soapRequest.setStrength(request.getStrength());
            if (request.getIntelligence() != null) soapRequest.setIntelligence(request.getIntelligence());
            if (request.getWillpower() != null) soapRequest.setWillpower(request.getWillpower());
            if (request.getDexterity() != null) soapRequest.setDexterity(request.getDexterity());

            Object responseObj = webServiceTemplate.marshalSendAndReceive(soapRequest);
            CharacterDetails details;
            
            if (responseObj instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<CharacterDetails> jaxbElement = (JAXBElement<CharacterDetails>) responseObj;
                details = jaxbElement.getValue();
            } else if (responseObj instanceof CharacterDetails) {
                details = (CharacterDetails) responseObj;
            } else {
                throw new SoapServiceException("Unexpected SOAP response type: " + responseObj.getClass().getName());
            }
            
            log.info("Character patched successfully: {}", id);
            return mapToDto(details);
        } catch (Exception e) {
            log.error("Error patching character with id: {}", id, e);
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                throw new CharacterNotFoundException("Character with id " + id + " not found");
            }
            throw new SoapServiceException("Error calling SOAP service: " + e.getMessage());
        }
    }

    @CacheEvict(value = {"characters", "charactersList"}, allEntries = true)
    public void deleteCharacter(String id) {
        try {
            log.info("Deleting character with id: {}", id);
            
            DeleteCharacterRequest request = objectFactory.createDeleteCharacterRequest();
            request.setId(id);

            Object responseObj = webServiceTemplate.marshalSendAndReceive(request);
            DeleteCharacterResponse response;
            
            if (responseObj instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<DeleteCharacterResponse> jaxbElement = (JAXBElement<DeleteCharacterResponse>) responseObj;
                response = jaxbElement.getValue();
            } else if (responseObj instanceof DeleteCharacterResponse) {
                response = (DeleteCharacterResponse) responseObj;
            } else {
                throw new SoapServiceException("Unexpected SOAP response type: " + responseObj.getClass().getName());
            }

            if (!response.isSuccess()) {
                throw new CharacterNotFoundException("Character with id " + id + " not found");
            }
            log.info("Character deleted successfully: {}", id);
        } catch (Exception e) {
            log.error("Error deleting character with id: {}", id, e);
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                throw new CharacterNotFoundException("Character with id " + id + " not found");
            }
            throw new SoapServiceException("Error calling SOAP service: " + e.getMessage());
        }
    }

    private CharacterDto mapToDto(CharacterDetails details) {
        return CharacterDto.builder()
            .id(details.getId())
            .name(details.getName())
            .characterClass(details.getCharacterClass() != null ? details.getCharacterClass().value() : null)
            .level(details.getLevel())
            .power(details.getPower())
            .armor(details.getArmor())
            .life(details.getLife())
            .strength(details.getStrength())
            .intelligence(details.getIntelligence())
            .willpower(details.getWillpower())
            .dexterity(details.getDexterity())
            .build();
    }

    private Comparator<CharacterDetails> getComparator(String field) {
        return switch (field.toLowerCase()) {
            case "name" -> Comparator.comparing(CharacterDetails::getName, Comparator.nullsLast(String::compareTo));
            case "level" -> Comparator.comparing(CharacterDetails::getLevel, Comparator.nullsLast(Integer::compareTo));
            case "power" -> Comparator.comparing(CharacterDetails::getPower, Comparator.nullsLast(Integer::compareTo));
            case "class", "characterclass" -> Comparator.comparing(
                c -> c.getCharacterClass() != null ? c.getCharacterClass().value() : null,
                Comparator.nullsLast(String::compareTo)
            );
            default -> null;
        };
    }
}
