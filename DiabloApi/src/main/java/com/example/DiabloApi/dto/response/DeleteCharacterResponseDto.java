package com.example.DiabloApi.dto.response;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"success", "message"})
@XmlRootElement(name = "deleteCharacterResponse", namespace = "http://DiabloApi.example.com/characters")
public class DeleteCharacterResponseDto {

    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Boolean success;
    
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private String message;

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
