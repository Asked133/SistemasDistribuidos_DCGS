package com.example.DiablodexApi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated character list response")
public class PagedCharacterResponse {
    
    @Schema(description = "List of characters")
    private List<CharacterDto> characters;
    
    @Schema(description = "Current page number", example = "0")
    private int page;
    
    @Schema(description = "Page size", example = "10")
    private int pageSize;
    
    @Schema(description = "Total number of characters", example = "100")
    private long totalElements;
    
    @Schema(description = "Total number of pages", example = "10")
    private int totalPages;
}
