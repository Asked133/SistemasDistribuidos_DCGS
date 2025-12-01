package com.example.DiablodexApi.dto;

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
    private int itemsCreados;
    private String mensaje;
    private List<ItemDto> items;
}
