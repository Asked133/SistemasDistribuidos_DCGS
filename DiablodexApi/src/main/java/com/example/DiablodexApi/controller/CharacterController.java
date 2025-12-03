package com.example.DiablodexApi.controller;

import com.example.DiablodexApi.dto.*;
import com.example.DiablodexApi.service.CharacterService;
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

import java.net.URI;

@RestController
@RequestMapping("/api/v1/characters")
@Tag(name = "Characters", description = "Character management API")
@SecurityRequirement(name = "oauth2")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @Operation(summary = "Get character by ID", description = "Retrieves a character by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Character found",
                content = @Content(schema = @Schema(implementation = CharacterDto.class))),
        @ApiResponse(responseCode = "404", description = "Character not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "502", description = "SOAP service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CharacterDto> getCharacterById(
            @Parameter(description = "Character ID", required = true)
            @PathVariable String id) {
        CharacterDto character = characterService.getCharacterById(id);
        return ResponseEntity.ok(character);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @Operation(summary = "Get all characters with pagination", 
               description = "Retrieves a paginated list of characters with optional sorting and filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Characters retrieved successfully",
                content = @Content(schema = @Schema(implementation = PagedCharacterResponse.class))),
        @ApiResponse(responseCode = "502", description = "SOAP service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PagedCharacterResponse> getAllCharacters(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field and direction (e.g., 'name,asc' or 'level,desc')", example = "name,asc")
            @RequestParam(required = false) String sort,
            @Parameter(description = "Filter by character class", example = "BARBARIAN")
            @RequestParam(required = false) String characterClass) {
        
        PagedCharacterResponse response = characterService.getAllCharacters(characterClass, sort, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @Operation(summary = "Create a new character", description = "Creates a new character and returns the created resource")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Character created successfully",
                content = @Content(schema = @Schema(implementation = CharacterDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Character already exists",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "502", description = "SOAP service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CharacterDto> createCharacter(
            @Valid @RequestBody CreateCharacterDto request) {
        CharacterDto created = characterService.createCharacter(request);
        return ResponseEntity
                .created(URI.create("/api/v1/characters/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @Operation(summary = "Update a character (full replacement)", 
               description = "Completely replaces an existing character with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Character updated successfully",
                content = @Content(schema = @Schema(implementation = CharacterDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Character not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "502", description = "SOAP service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CharacterDto> updateCharacter(
            @Parameter(description = "Character ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody CreateCharacterDto request) {
        CharacterDto updated = characterService.updateCharacter(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @Operation(summary = "Partially update a character", 
               description = "Updates only the provided fields of an existing character")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Character updated successfully",
                content = @Content(schema = @Schema(implementation = CharacterDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Character not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "502", description = "SOAP service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CharacterDto> patchCharacter(
            @Parameter(description = "Character ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateCharacterDto request) {
        CharacterDto updated = characterService.patchCharacter(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @Operation(summary = "Delete a character", description = "Deletes an existing character by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Character deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Character not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "502", description = "SOAP service error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteCharacter(
            @Parameter(description = "Character ID", required = true)
            @PathVariable String id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
