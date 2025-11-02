package com.example.DiablodexApi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new character")
public class CreateCharacterDto {
    
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Schema(description = "Character name", example = "Aragorn", required = true)
    private String name;
    
    @NotBlank(message = "Character class is required")
    @Pattern(regexp = "BARBARIAN|SORCERER|NECROMANCER|ROGUE|DRUID|SPIRITBORN", 
             message = "Invalid character class")
    @Schema(description = "Character class", example = "BARBARIAN", required = true)
    private String characterClass;
    
    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 100, message = "Level cannot exceed 100")
    @Schema(description = "Character level", example = "50", defaultValue = "1")
    private Integer level;
    
    @Min(value = 0, message = "Power cannot be negative")
    @Schema(description = "Character power", example = "1500", defaultValue = "0")
    private Integer power;
    
    @Min(value = 0, message = "Armor cannot be negative")
    @Schema(description = "Character armor", example = "2000", defaultValue = "0")
    private Integer armor;
    
    @Min(value = 1, message = "Life must be at least 1")
    @Schema(description = "Character life points", example = "5000", defaultValue = "100")
    private Integer life;
    
    @NotNull(message = "Strength is required")
    @Min(value = 0, message = "Strength cannot be negative")
    @Schema(description = "Strength stat", example = "150", required = true)
    private Integer strength;
    
    @NotNull(message = "Intelligence is required")
    @Min(value = 0, message = "Intelligence cannot be negative")
    @Schema(description = "Intelligence stat", example = "80", required = true)
    private Integer intelligence;
    
    @NotNull(message = "Willpower is required")
    @Min(value = 0, message = "Willpower cannot be negative")
    @Schema(description = "Willpower stat", example = "100", required = true)
    private Integer willpower;
    
    @NotNull(message = "Dexterity is required")
    @Min(value = 0, message = "Dexterity cannot be negative")
    @Schema(description = "Dexterity stat", example = "120", required = true)
    private Integer dexterity;
}
