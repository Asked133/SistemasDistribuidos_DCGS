package com.example.DiabloApi.dto.response;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "deleteCharacterResponse", namespace = "http://DiabloApi.example.com/characters")
public class DeleteCharacterResponseDto {

    @XmlElement
    private String message;

    // Getters y Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}