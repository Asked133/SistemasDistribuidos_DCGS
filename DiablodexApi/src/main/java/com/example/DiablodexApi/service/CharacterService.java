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

            CharacterDetails details = extractCharacterDetails(
                webServiceTemplate.marshalSendAndReceive(request)
            );
            validateCharacterResponse(details, "Character with id " + id + " not found", true);
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
            List<CharacterDetails> allCharacters;
            Integer soapTotalPages = null;
            Long soapTotalElements = null;

            if (responseObj instanceof JAXBElement<?> jaxbElement) {
                Object value = jaxbElement.getValue();
                SoapListPayload payload = unwrapListPayload(value);
                allCharacters = payload.characters();
                soapTotalElements = payload.totalElements();
                soapTotalPages = payload.totalPages();
            } else {
                SoapListPayload payload = unwrapListPayload(responseObj);
                allCharacters = payload.characters();
                soapTotalElements = payload.totalElements();
                soapTotalPages = payload.totalPages();
            }

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
                .totalElements(soapTotalElements != null ? soapTotalElements.intValue() : totalElements)
                .totalPages(soapTotalPages != null ? soapTotalPages : totalPages)
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

            CharacterDetails details = ensureCharacterExists(
                extractCharacterDetails(webServiceTemplate.marshalSendAndReceive(soapRequest)),
                null,
                request.getName()
            );

            validateCharacterResponse(details, "Failed to create character " + request.getName(), false);
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

            CharacterDetails details = ensureCharacterExists(
                extractCharacterDetails(webServiceTemplate.marshalSendAndReceive(soapRequest)),
                id,
                request.getName()
            );

            validateCharacterResponse(details, "Failed to update character " + id, true);
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

            CharacterDetails details = ensureCharacterExists(
                extractCharacterDetails(webServiceTemplate.marshalSendAndReceive(soapRequest)),
                id,
                request.getName()
            );

            validateCharacterResponse(details, "Failed to update character " + id, true);
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

    private void validateCharacterResponse(CharacterDetails details, String defaultMessage, boolean notFoundOnEmpty) {
        if (details != null) {
            log.debug("SOAP payload received - id: {}, name: {}, message: {}", details.getId(), details.getName(), details.getMessage());
        } else {
            log.debug("SOAP payload received is null for context: {}", defaultMessage);
        }
        if (details == null) {
            if (notFoundOnEmpty) {
                throw new CharacterNotFoundException(defaultMessage);
            }
            throw new SoapServiceException(defaultMessage);
        }

        if (hasIdentity(details)) {
            return;
        }

        String message = details.getMessage();
        if (message != null) {
            String normalized = message.toLowerCase();
            if (normalized.contains("not found")) {
                throw new CharacterNotFoundException(message);
            }
            if (normalized.contains("already exists") || normalized.contains("unique")) {
                throw new CharacterAlreadyExistsException(message);
            }
        }

        if (notFoundOnEmpty) {
            throw new CharacterNotFoundException(defaultMessage);
        }
        throw new SoapServiceException(message != null ? message : defaultMessage);
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

    private SoapListPayload unwrapListPayload(Object value) {
        if (value instanceof GetAllCharactersResponse paged) {
            return new SoapListPayload(paged.getCharacters(), paged.getTotalElements(), paged.getTotalPages());
        }
        if (value instanceof GetCharactersByClassResponse byClass) {
            return new SoapListPayload(byClass.getCharacters(), null, null);
        }
        throw new SoapServiceException("Unexpected SOAP response type: " + value.getClass().getName());
    }

    private boolean hasIdentity(CharacterDetails details) {
        return details != null && details.getId() != null && details.getName() != null;
    }

    private CharacterDetails ensureCharacterExists(CharacterDetails details, String id, String name) {
        if (hasIdentity(details)) {
            return details;
        }

        log.warn("SOAP response missing identifying data (id: {}, name: {}), running fallback lookup", id, name);
        CharacterDetails resolved = null;
        if (id != null && !id.isBlank()) {
            resolved = fetchCharacterById(id);
        }
        if (!hasIdentity(resolved) && name != null && !name.isBlank()) {
            resolved = fetchCharacterByName(name);
        }

        return resolved != null ? resolved : details;
    }

    private CharacterDetails fetchCharacterByName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        try {
            GetCharacterByNameRequest lookup = objectFactory.createGetCharacterByNameRequest();
            lookup.setName(name);
            return extractCharacterDetails(webServiceTemplate.marshalSendAndReceive(lookup));
        } catch (Exception ex) {
            log.error("Fallback lookup by name failed for {}", name, ex);
            return null;
        }
    }

    private CharacterDetails fetchCharacterById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        try {
            GetCharacterByIdRequest lookup = objectFactory.createGetCharacterByIdRequest();
            lookup.setId(id);
            return extractCharacterDetails(webServiceTemplate.marshalSendAndReceive(lookup));
        } catch (Exception ex) {
            log.error("Fallback lookup by id failed for {}", id, ex);
            return null;
        }
    }

    private CharacterDetails extractCharacterDetails(Object responseObj) {
        if (responseObj instanceof JAXBElement<?> jaxbElement) {
            Object value = jaxbElement.getValue();
            if (value instanceof CharacterDetails details) {
                return details;
            }
        } else if (responseObj instanceof CharacterDetails details) {
            return details;
        }
        if (responseObj == null) {
            return null;
        }
        throw new SoapServiceException("Unexpected SOAP response type: " + responseObj.getClass().getName());
    }

    private record SoapListPayload(List<CharacterDetails> characters, Long totalElements, Integer totalPages) {}
}
