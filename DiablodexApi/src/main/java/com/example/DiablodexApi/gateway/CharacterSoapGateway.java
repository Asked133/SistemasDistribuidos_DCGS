package com.example.DiablodexApi.gateway;

import com.example.DiablodexApi.soap.client.generated.*; 
import jakarta.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class CharacterSoapGateway {

    private static final Logger logger = LoggerFactory.getLogger(CharacterSoapGateway.class);

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public CharacterDetails getCharacterById(String id) {
        GetCharacterByIdRequest request = new GetCharacterByIdRequest();
        request.setId(id);
        Object response = webServiceTemplate.marshalSendAndReceive(request);
        return extractValue(response, CharacterDetails.class);
    }

    public GetCharactersByClassResponse getCharactersByClass(String characterClass) {
        GetCharactersByClassRequest request = new GetCharactersByClassRequest();
        request.setCharacterClass(CharacterClassEnum.fromValue(characterClass.toUpperCase()));
        Object response = webServiceTemplate.marshalSendAndReceive(request);
        return extractValue(response, GetCharactersByClassResponse.class);
    }

    public CharacterDetails createCharacter(com.example.DiablodexApi.soap.client.generated.CreateCharacterRequest request) {
        Object response = webServiceTemplate.marshalSendAndReceive(request);
        return extractValue(response, CharacterDetails.class);
    }
    
    public CharacterDetails updateCharacter(com.example.DiablodexApi.soap.client.generated.UpdateCharacterRequest request) {
        Object response = webServiceTemplate.marshalSendAndReceive(request);
        return extractValue(response, CharacterDetails.class);
    }
    
    public void deleteCharacter(String id) {
        DeleteCharacterRequest request = new DeleteCharacterRequest();
        request.setId(id);
        webServiceTemplate.marshalSendAndReceive(request);
    }
    
    @SuppressWarnings("unchecked")
    private <T> T extractValue(Object response, Class<T> expectedType) {
        logger.debug("===== GATEWAY EXTRACT =====");
        logger.debug("Response class: {}", response != null ? response.getClass().getName() : "null");
        logger.debug("Expected type: {}", expectedType.getName());
        logger.debug("Is JAXBElement? {}", response instanceof JAXBElement);
        
        if (response instanceof JAXBElement) {
            JAXBElement<?> element = (JAXBElement<?>) response;
            logger.debug("JAXBElement name: {}", element.getName());
            logger.debug("JAXBElement value class: {}", element.getValue() != null ? element.getValue().getClass().getName() : "null");
            T value = ((JAXBElement<T>) response).getValue();
            logger.debug("Extracted value: {}", value);
            logger.debug("===========================");
            return value;
        }
        logger.debug("Casting directly to {}", expectedType.getName());
        logger.debug("===========================");
        return expectedType.cast(response);
    }
}