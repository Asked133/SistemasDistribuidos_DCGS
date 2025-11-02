package com.example.DiabloApi.endpoint;

import com.example.DiabloApi.entity.Character;
import com.example.DiabloApi.mapper.CharacterMapper;
import com.example.DiabloApi.service.CharacterService;
import com.example.DiabloApi.validators.CharacterValidations;
// Asegúrate de que este import sea el de tus clases generadas por JAXB/XSD
import diablo.api.characters.*; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.UUID;

@Endpoint
public class CharacterEndpoint {

    // El Namespace URI de tu characters.xsd
    private static final String NAMESPACE_URI = "http://DiabloApi.example.com/characters"; 

    @Autowired
    private CharacterService service;

    @Autowired
    private CharacterMapper mapper;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCharacterByIdRequest")
    @ResponsePayload
    public CharacterDetails getCharacterById(@RequestPayload GetCharacterByIdRequest request) {
        CharacterValidations.validate(request);
        Character character = service.getById(UUID.fromString(request.getId())).orElse(null);
        return mapper.toSoapDto(character);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCharacterByNameRequest")
    @ResponsePayload
    public CharacterDetails getCharacterByName(@RequestPayload GetCharacterByNameRequest request) {
        CharacterValidations.validate(request);
        Character character = service.getByName(request.getName()).orElse(null);
        return mapper.toSoapDto(character);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCharactersByClassRequest")
    @ResponsePayload
    public GetCharactersByClassResponse getCharactersByClass(@RequestPayload GetCharactersByClassRequest request) {
        CharacterValidations.validate(request);
        List<Character> characters = service.getAllByClass(mapper.toEntityEnum(request.getCharacterClass()));
        
        GetCharactersByClassResponse response = new GetCharactersByClassResponse();
        characters.stream()
                .map(mapper::toSoapDto)
                .forEach(response.getCharacters()::add);
        return response;
    }

    /**
     * ================== CORRECCIÓN AQUÍ ==================
     *
     * 1. Mapea el request a la entidad (characterToSave).
     * 2. Llama al servicio, que guarda Y DEVUELVE la entidad con ID (savedCharacter).
     * 3. Mapea la "savedCharacter" (con ID) a la respuesta SOAP.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createCharacterRequest")
    @ResponsePayload
    public CharacterDetails createCharacter(@RequestPayload CreateCharacterRequest request) {
        CharacterValidations.validate(request);
        
        // 1. Entidad sin ID
        Character characterToSave = mapper.toEntity(request); 
        
        // 2. Entidad CON ID (devuelta por el servicio)
        Character savedCharacter = service.createCharacter(characterToSave); 
        
        // 3. Mapea la entidad con ID a la respuesta
        return mapper.toSoapDto(savedCharacter);
    }

    /**
     * ================== CORRECCIÓN AQUÍ ==================
     *
     * 1. Valida y busca la entidad existente.
     * 2. Mapea los cambios del request a la entidad existente.
     * 3. Llama al servicio, que guarda Y DEVUELVE la entidad actualizada.
     * 4. Mapea la entidad actualizada a la respuesta SOAP.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateCharacterRequest")
    @ResponsePayload
    public CharacterDetails updateCharacter(@RequestPayload UpdateCharacterRequest request) {
        CharacterValidations.validate(request);
        
        // 1. Obtiene la entidad actual
        Character existingCharacter = service.getById(UUID.fromString(request.getId())).orElse(null);
        // (La validación ya debería haber lanzado error si es nulo)

        // 2. Mapea los cambios
        mapper.updateEntityFromSoapDto(request.getCharacterDetails(), existingCharacter);
        
        // 3. Guarda y captura la entidad actualizada
        Character updatedCharacter = service.updateCharacter(existingCharacter);
        
        // 4. Mapea la entidad actualizada a la respuesta
        return mapper.toSoapDto(updatedCharacter);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteCharacterRequest")
    @ResponsePayload
    public DeleteCharacterResponse deleteCharacter(@RequestPayload DeleteCharacterRequest request) {
        CharacterValidations.validate(request);
        service.deleteCharacter(UUID.fromString(request.getId()));
        
        DeleteCharacterResponse response = new DeleteCharacterResponse();
        response.setMessage("Character with id " + request.getId() + " was successfully deleted.");
        return response;
    }
}