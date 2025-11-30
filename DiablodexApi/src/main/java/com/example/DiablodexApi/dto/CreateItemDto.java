package com.example.DiablodexApi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String id;
    
    @NotBlank(message = "Name is required")
    private String nombre;
    
    @NotNull(message = "Type is required")
    private String tipo;
    
    @Min(value = 0, message = "Power must be at least 0")
    @Max(value = 1000, message = "Power must be at most 1000")
    private Integer poderDeObjeto;
    
    // danoBase and armaduraBase are now optional - will be auto-set based on type
    private Integer danoBase;
    private Integer armaduraBase;
    
    private String habilidadPasiva;
    private String habilidadActiva;
    
    @Size(max = 2, message = "Maximum 2 gems allowed")
    private List<String> gemas;
}
