package com.example.DiabloApi.dto.request;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name" })
@XmlRootElement(name = "getCharacterByNameRequest", namespace = "http://DiabloApi.example.com/characters")
public class GetCharacterByNameRequestDto {

    @XmlElement(namespace = "http://DiabloApi.example.com/characters", required = true)
    private String name;

    public GetCharacterByNameRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}