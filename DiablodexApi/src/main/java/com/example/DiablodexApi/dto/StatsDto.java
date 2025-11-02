package com.example.DiablodexApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsDto {

    @NotNull
    @Min(0)
    private Integer strength;

    @NotNull
    @Min(0)
    private Integer intelligence;

    @NotNull
    @Min(0)
    private Integer willpower;

    @NotNull
    @Min(0)
    private Integer dexterity;
}