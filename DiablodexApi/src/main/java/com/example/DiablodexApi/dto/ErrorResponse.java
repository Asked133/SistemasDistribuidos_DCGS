package com.example.DiablodexApi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {
    
    @Schema(description = "HTTP status code", example = "400")
    private int status;
    
    @Schema(description = "Error message", example = "Validation failed")
    private String message;
    
    @Schema(description = "Detailed error information", example = "Name is required")
    private String details;
    
    @Schema(description = "Timestamp", example = "2025-11-01T10:30:00")
    private String timestamp;
}
