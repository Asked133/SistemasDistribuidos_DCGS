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
@Schema(description = "Request to update a character (partial update)")
public class UpdateCharacterDto {
    
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Schema(description = "Character name", example = "Aragorn")
    private String name;
    
    @Pattern(regexp = "BARBARIAN|SORCERER|NECROMANCER|ROGUE|DRUID|SPIRITBORN", 
             message = "Invalid character class")
    @Schema(description = "Character class", example = "BARBARIAN")
    private String characterClass;
    
    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 100, message = "Level cannot exceed 100")
    @Schema(description = "Character level", example = "50")
    private Integer level;
    
    @Min(value = 0, message = "Power cannot be negative")
    @Schema(description = "Character power", example = "1500")
    private Integer power;
    
    @Min(value = 0, message = "Armor cannot be negative")
    @Schema(description = "Character armor", example = "2000")
    private Integer armor;
    
    @Min(value = 1, message = "Life must be at least 1")
    @Schema(description = "Character life points", example = "5000")
    private Integer life;
    
    @Min(value = 0, message = "Strength cannot be negative")
    @Schema(description = "Strength stat", example = "150")
    private Integer strength;
    
    @Min(value = 0, message = "Intelligence cannot be negative")
    @Schema(description = "Intelligence stat", example = "80")
    private Integer intelligence;
    
    @Min(value = 0, message = "Willpower cannot be negative")
    @Schema(description = "Willpower stat", example = "100")
    private Integer willpower;
    
    @Min(value = 0, message = "Dexterity cannot be negative")
    @Schema(description = "Dexterity stat", example = "120")
    private Integer dexterity;
}
