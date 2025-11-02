package com.example.DiabloApi.dto.request;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "characterClass" })
@XmlRootElement(name = "getCharactersByClassRequest", namespace = "http://DiabloApi.example.com/characters")
public class GetCharactersByClassRequestDto {

    @XmlElement(namespace = "http://DiabloApi.example.com/characters", required = true)
    private String characterClass;

    public GetCharactersByClassRequestDto() {
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }
}