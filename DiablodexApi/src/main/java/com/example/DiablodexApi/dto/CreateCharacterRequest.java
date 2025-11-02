package com.example.DiablodexApi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCharacterRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "La clase no puede estar vacía")
    private String characterClass; // Ej: "BARBARIAN"

    @NotNull(message = "El nivel es obligatorio")
    @Min(value = 1, message = "El nivel debe ser al menos 1")
    private Integer level;
    
    @NotNull(message = "Power es obligatorio")
    @Min(value = 1, message = "Power debe ser al menos 1")
    private Integer power;
    
    @NotNull(message = "Armor es obligatorio")
    @Min(value = 1, message = "Armor debe ser al menos 1")
    private Integer armor;
    
    @NotNull(message = "Life es obligatorio")
    @Min(value = 1, message = "Life debe ser al menos 1")
    private Integer life;
    
    @NotNull(message = "Stats es obligatorio")
    @Valid // <-- Valida los campos *dentro* de StatsDto
    private StatsDto stats;
}