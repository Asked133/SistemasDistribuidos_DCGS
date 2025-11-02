package com.example.DiablodexApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CharacterResponse extends RepresentationModel<CharacterResponse> {
    private String id;
    private String name;
    private String characterClass; 
    private int level;
    private int power;
    private int armor;
    private int life;
    private StatsDto stats;
    
}