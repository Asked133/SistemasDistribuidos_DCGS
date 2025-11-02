package com.example.DiablodexApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Character details")
public class CharacterDto implements Serializable {
    
    @Schema(description = "Character unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;
    
    @Schema(description = "Character name", example = "Aragorn", required = true)
    private String name;
    
    @Schema(description = "Character class", example = "BARBARIAN", required = true, 
            allowableValues = {"BARBARIAN", "SORCERER", "NECROMANCER", "ROGUE", "DRUID", "SPIRITBORN"})
    private String characterClass;
    
    @Schema(description = "Character level", example = "50", minimum = "1", maximum = "100")
    private Integer level;
    
    @Schema(description = "Character power", example = "1500", minimum = "0")
    private Integer power;
    
    @Schema(description = "Character armor", example = "2000", minimum = "0")
    private Integer armor;
    
    @Schema(description = "Character life points", example = "5000", minimum = "1")
    private Integer life;
    
    @Schema(description = "Strength stat", example = "150", minimum = "1", required = true)
    private Integer strength;
    
    @Schema(description = "Intelligence stat", example = "80", minimum = "1", required = true)
    private Integer intelligence;
    
    @Schema(description = "Willpower stat", example = "100", minimum = "1", required = true)
    private Integer willpower;
    
    @Schema(description = "Dexterity stat", example = "120", minimum = "1", required = true)
    private Integer dexterity;
}
