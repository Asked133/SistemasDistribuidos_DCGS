package com.example.DiabloApi.dto.request;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "deleteCharacterRequest", namespace = "http://DiabloApi.example.com/characters")
public class DeleteCharacterRequestDto {

    @XmlElement(required = true)
    private String id;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}