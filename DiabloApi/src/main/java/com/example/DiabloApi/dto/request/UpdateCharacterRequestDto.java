package com.example.DiabloApi.dto.request;

import com.example.DiabloApi.dto.response.CharacterResponseDto;
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "updateCharacterRequest", namespace = "http://DiabloApi.example.com/characters")
public class UpdateCharacterRequestDto {

    @XmlElement(required = true)
    private String id;

    @XmlElement(required = true)
    private CharacterResponseDto characterDetails;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public CharacterResponseDto getCharacterDetails() { return characterDetails; }
    public void setCharacterDetails(CharacterResponseDto characterDetails) { this.characterDetails = characterDetails; }
}