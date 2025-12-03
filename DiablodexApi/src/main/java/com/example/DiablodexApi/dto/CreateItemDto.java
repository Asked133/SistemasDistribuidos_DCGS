package com.example.DiablodexApi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDto {
    
    // ID is now optional - will be auto-generated if not provided
    @JsonProperty("id")
    private String id;
    
    @NotBlank(message = "Name is required")
    @JsonProperty("name")
    @JsonAlias({"nombre"})
    private String nombre;
    
    @NotNull(message = "Type is required")
    @JsonProperty("type")
    @JsonAlias({"tipo"})
    private String tipo;
    
    @NotNull(message = "Power is required")
    @Min(value = 1, message = "Power must be at least 1")
    @Max(value = 1000, message = "Power must be at most 1000")
    @JsonProperty("power")
    @JsonAlias({"poderDeObjeto", "poder"})
    private Integer poderDeObjeto;
    
    // danoBase and armaduraBase are now optional - will be auto-set based on type
    @JsonProperty("damage")
    @JsonAlias({"danoBase", "danioBase"})
    @Positive(message = "Damage must be positive")
    private Integer danoBase;

    @JsonProperty("defense")
    @JsonAlias({"armaduraBase"})
    @Positive(message = "Defense must be positive")
    private Integer armaduraBase;
    
    @JsonProperty("passiveSkill")
    @JsonAlias({"habilidadPasiva"})
    private String habilidadPasiva;

    @JsonProperty("activeSkill")
    @JsonAlias({"habilidadActiva"})
    private String habilidadActiva;
    
    @Size(max = 2, message = "Maximum 2 gems allowed")
    @JsonProperty("gems")
    @JsonAlias({"gemas"})
    private List<String> gemas;
}
