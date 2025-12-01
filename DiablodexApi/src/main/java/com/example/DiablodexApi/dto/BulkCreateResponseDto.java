package com.example.DiablodexApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkCreateResponseDto {

    @JsonProperty("itemsCreated")
    private int itemsCreados;

    @JsonProperty("message")
    private String mensaje;

    @JsonProperty("items")
    private List<ItemDto> items;
}
