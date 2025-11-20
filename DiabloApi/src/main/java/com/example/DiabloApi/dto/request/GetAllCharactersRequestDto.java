package com.example.DiabloApi.dto.request;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"page", "size"})
@XmlRootElement(name = "getAllCharactersRequest", namespace = "http://DiabloApi.example.com/characters")
public class GetAllCharactersRequestDto {

    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer page;
    
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer size;

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
