package com.example.DiablodexApi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCharacterRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "La clase no puede estar vacía")
    private String characterClass;

    @NotNull
    @Min(value = 1)
    private Integer level;
    
    @NotNull
    @Min(value = 1)
    private Integer power;
    
    @NotNull
    @Min(value = 1)
    private Integer armor;
    
    @NotNull
    @Min(value = 1)
    private Integer life;
    
    @NotNull
    @Valid 
    private StatsDto stats;
}