package com.example.DiabloApi.dto.request;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id"})
@XmlRootElement(name = "deleteCharacterRequest", namespace = "http://DiabloApi.example.com/characters")
public class DeleteCharacterRequestDto {

    @XmlElement(namespace = "http://DiabloApi.example.com/characters", required = true)
    private String id;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
