package com.example.DiablodexApi.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class PatchCharacterRequest {
    private String name;
    private String characterClass;
    private Integer level;
    private Integer power;
    private Integer armor;
    private Integer life;
    
    @Valid 
    private StatsDto stats;
}