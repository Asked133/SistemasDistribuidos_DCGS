package com.example.DiablodexApi.dto;

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
public class ItemDto implements Serializable {
    private String id;
    private String nombre;
    private String tipo;
    private Integer poderDeObjeto;
    private Integer danoBase;
    private Integer armaduraBase;
    private String habilidadPasiva;
    private String habilidadActiva;
    private List<String> gemasIncrustadas;
}
