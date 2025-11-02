package com.example.DiablodexApi.service;

import com.example.DiablodexApi.dto.*; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICharacterService {
    
    Page<CharacterResponse> getAllByClass(String characterClass, Pageable pageable);
    
    CharacterResponse getById(String id); 
    
    CharacterResponse create(CreateCharacterRequest request);
    
    CharacterResponse update(String id, UpdateCharacterRequest request);
    
    CharacterResponse patch(String id, PatchCharacterRequest request);
    
    void delete(String id);
}   