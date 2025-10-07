package com.example.DiabloApi.dto.request;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id" })
@XmlRootElement(name = "getCharacterByIdRequest", namespace = "http://DiabloApi.example.com/characters")
public class GetCharacterByIdRequestDto {

    @XmlElement(namespace = "http://DiabloApi.example.com/characters", required = true)
    private String id;

    public GetCharacterByIdRequestDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}