package com.example.DiabloApi.dto.response;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "characters", "message" })
@XmlRootElement(name = "getCharactersByClassResponse", namespace = "http://DiabloApi.example.com/characters")
public class CharacterListResponseDto {

    // Hace una lista de CharacterResponseDto
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private List<CharacterResponseDto> characters;

    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private String message;

    public CharacterListResponseDto() {
    }

    public List<CharacterResponseDto> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharacterResponseDto> characters) {
        this.characters = characters;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}