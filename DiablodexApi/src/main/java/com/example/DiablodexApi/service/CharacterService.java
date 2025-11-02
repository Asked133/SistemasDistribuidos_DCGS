package com.example.DiablodexApi.service;

import com.example.DiablodexApi.dto.*;
import com.example.DiablodexApi.gateway.CharacterSoapGateway;
import com.example.DiablodexApi.mapper.CharacterMapper;
import com.example.DiablodexApi.soap.client.generated.CharacterDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterService implements ICharacterService {

    @Autowired
    private CharacterSoapGateway gateway; 
    @Autowired
    private CharacterMapper mapper; 

    @Override
    @Cacheable(value = "characters", key = "#characterClass + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CharacterResponse> getAllByClass(String characterClass, Pageable pageable) {
        var soapResponse = gateway.getCharactersByClass(characterClass);
        
        List<CharacterResponse> allCharacters = soapResponse.getCharacters()
                .stream() 
                .map(mapper::toRestDto)
                .collect(Collectors.toList());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allCharacters.size());
        
        List<CharacterResponse> pageContent = allCharacters.subList(start, end);
        
        return new PageImpl<>(pageContent, pageable, allCharacters.size());
    }

    @Override
    @Cacheable(value = "character", key = "#id") 
    public CharacterResponse getById(String id) {
        CharacterDetails details = gateway.getCharacterById(id);
        return mapper.toRestDto(details);
    }

    @Override
    @CacheEvict(value = {"character", "characters"}, allEntries = true) 
    public CharacterResponse create(com.example.DiablodexApi.dto.CreateCharacterRequest request) {
        var soapRequest = mapper.toSoapDto(request);
        var soapResponse = gateway.createCharacter(soapRequest);
        return mapper.toRestDto(soapResponse);
    }

    @Override
    @CacheEvict(value = {"character", "characters"}, allEntries = true)
    public CharacterResponse update(String id, com.example.DiablodexApi.dto.UpdateCharacterRequest request) {
        var soapRequest = mapper.toSoapDto(id, request);
        var soapResponse = gateway.updateCharacter(soapRequest);
        return mapper.toRestDto(soapResponse);
    }

    @Override
    @CacheEvict(value = {"character", "characters"}, allEntries = true)
    public CharacterResponse patch(String id, PatchCharacterRequest request) {
        CharacterResponse currentState = this.getById(id);
        
        com.example.DiablodexApi.dto.UpdateCharacterRequest updateRequest = new com.example.DiablodexApi.dto.UpdateCharacterRequest();
        
        updateRequest.setName(request.getName() != null ? request.getName() : currentState.getName());
        updateRequest.setCharacterClass(request.getCharacterClass() != null ? request.getCharacterClass() : currentState.getCharacterClass());
        updateRequest.setLevel(request.getLevel() != null ? request.getLevel() : currentState.getLevel());
        updateRequest.setPower(request.getPower() != null ? request.getPower() : currentState.getPower());
        updateRequest.setArmor(request.getArmor() != null ? request.getArmor() : currentState.getArmor());
        updateRequest.setLife(request.getLife() != null ? request.getLife() : currentState.getLife());
        
        if (request.getStats() == null) {
            updateRequest.setStats(currentState.getStats());
        } else {
            StatsDto statsToUpdate = currentState.getStats();
            if (request.getStats().getStrength() != null) statsToUpdate.setStrength(request.getStats().getStrength());
            if (request.getStats().getIntelligence() != null) statsToUpdate.setIntelligence(request.getStats().getIntelligence());
            if (request.getStats().getWillpower() != null) statsToUpdate.setWillpower(request.getStats().getWillpower());
            if (request.getStats().getDexterity() != null) statsToUpdate.setDexterity(request.getStats().getDexterity());
            updateRequest.setStats(statsToUpdate);
        }
        
        return this.update(id, updateRequest);
    }
    
    @Override
    @CacheEvict(value = {"character", "characters"}, allEntries = true)
    public void delete(String id) {
        gateway.deleteCharacter(id);
    }
}