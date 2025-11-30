package com.example.DiablodexApi.controller;

import com.example.DiablodexApi.dto.*;
import com.example.DiablodexApi.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "Items", description = "Item/Loot management API (via gRPC to WeaponsApi)")
@SecurityRequirement(name = "oauth2")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @Operation(summary = "Get item by ID", description = "Retrieves an item by its unique identifier via gRPC")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item found",
                content = @Content(schema = @Schema(implementation = ItemDto.class))),
        @ApiResponse(responseCode = "404", description = "Item not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "502", description = "gRPC service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ItemDto> getItemById(
            @Parameter(description = "Item ID", required = true)
            @PathVariable String id) {
        ItemDto item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @Operation(summary = "Get items by type", 
               description = "Retrieves all items of a specific type (WEAPON or ARMOR) via gRPC server streaming")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Items retrieved successfully",
                content = @Content(schema = @Schema(implementation = ItemDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid item type",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "502", description = "gRPC service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ItemDto>> getItemsByType(
            @Parameter(description = "Item type (WEAPON or ARMOR)", required = true, example = "WEAPON")
            @PathVariable String type) {
        List<ItemDto> items = itemService.getItemsByType(type);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @Operation(summary = "Create items in bulk", 
               description = "Creates multiple items in a single request via gRPC client streaming")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Items created successfully",
                content = @Content(schema = @Schema(implementation = BulkCreateResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Item already exists",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "502", description = "gRPC service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BulkCreateResponseDto> createBulkItems(
            @Valid @RequestBody List<CreateItemDto> items) {
        BulkCreateResponseDto response = itemService.createBulkItems(items);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
