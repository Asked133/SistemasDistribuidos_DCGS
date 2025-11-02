package com.example.DiablodexApi.controller;

import com.example.DiablodexApi.dto.*;
import com.example.DiablodexApi.service.ICharacterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private static final Logger logger = LoggerFactory.getLogger(CharacterController.class);

    @Autowired
    private ICharacterService characterService;

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<PageResponse<CharacterDTO>> getAllCharacters(
        @RequestParam(name = "class") String characterClass,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(defaultValue = "name") String sort
    ) {
        logger.info("===== GET ALL CHARACTERS =====");
        logger.info("Class: {}, Page: {}, PageSize: {}, Sort: {}", characterClass, page, pageSize, sort);
        
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sort));
        Page<CharacterResponse> characters = characterService.getAllByClass(characterClass, pageable);
        
        logger.info("Total characters from service: {}", characters.getTotalElements());
        logger.info("Page content size: {}", characters.getContent().size());
        
        // Convertir CharacterResponse a CharacterDTO simple para evitar problemas de serialización con HATEOAS
        List<CharacterDTO> characterDTOs = characters.getContent().stream()
            .map(response -> {
                CharacterDTO dto = new CharacterDTO();
                dto.setId(response.getId());
                dto.setName(response.getName());
                dto.setCharacterClass(response.getCharacterClass());
                dto.setLevel(response.getLevel());
                dto.setPower(response.getPower());
                dto.setArmor(response.getArmor());
                dto.setLife(response.getLife());
                dto.setStats(response.getStats());
                return dto;
            })
            .collect(Collectors.toList());
        
        logger.info("Converted DTOs size: {}", characterDTOs.size());
        
        PageResponse<CharacterDTO> pageResponse = new PageResponse<>(
            characterDTOs,
            characters.getNumber(),
            characters.getSize(),
            characters.getTotalElements(),
            characters.getTotalPages(),
            characters.isFirst(),
            characters.isLast()
        );
        
        logger.info("PageResponse created successfully");
        logger.info("================================");
        
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<CharacterResponse> getCharacterById(@PathVariable String id) {
        CharacterResponse character = characterService.getById(id);

        character.add(linkTo(methodOn(CharacterController.class).getCharacterById(id)).withSelfRel());
        character.add(linkTo(methodOn(CharacterController.class)
                .getAllCharacters(character.getCharacterClass(), 0, 10, "name"))
                .withRel("collection-by-class"));

        return ResponseEntity.ok(character);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<CharacterResponse> createCharacter(
        @Valid @RequestBody CreateCharacterRequest request
    ) {
        CharacterResponse newCharacter = characterService.create(request);

        newCharacter.add(
            linkTo(methodOn(CharacterController.class).getCharacterById(newCharacter.getId())).withSelfRel()
        );

        return ResponseEntity
            .created(newCharacter.getRequiredLink("self").toUri())
            .body(newCharacter);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<CharacterResponse> updateCharacter(
        @PathVariable String id, 
        @Valid @RequestBody UpdateCharacterRequest request
    ) {
        CharacterResponse updatedCharacter = characterService.update(id, request);
        
        updatedCharacter.add(
            linkTo(methodOn(CharacterController.class).getCharacterById(updatedCharacter.getId())).withSelfRel()
        );
        
        return ResponseEntity.ok(updatedCharacter);
    }
    
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<CharacterResponse> patchCharacter(
        @PathVariable String id, 
        @RequestBody PatchCharacterRequest request
    ) {
        CharacterResponse patchedCharacter = characterService.patch(id, request);
        
        patchedCharacter.add(
            linkTo(methodOn(CharacterController.class).getCharacterById(patchedCharacter.getId())).withSelfRel()
        );
        
        return ResponseEntity.ok(patchedCharacter);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String id) {
        characterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}