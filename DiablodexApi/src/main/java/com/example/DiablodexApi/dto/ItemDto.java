package com.example.DiablodexApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String nombre;

    @JsonProperty("type")
    private String tipo;

    @JsonProperty("power")
    private Integer poderDeObjeto;

    @JsonProperty("damage")
    private Integer danoBase;

    @JsonProperty("defense")
    private Integer armaduraBase;

    @JsonProperty("passiveSkill")
    private String habilidadPasiva;

    @JsonProperty("activeSkill")
    private String habilidadActiva;

    @JsonProperty("gems")
    private List<String> gemasIncrustadas;
}
