package com.example.DiabloApi.dto.response;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CharacterDetails", namespace = "http://DiabloApi.example.com/characters", propOrder = {
        "id", "name", "characterClass", "level", "power", "armor", "life",
        "strength", "intelligence", "willpower", "dexterity", "message"
})
public class CharacterResponseDto {

    @XmlElement(required = true)
    private String id;
    
    @XmlElement(required = true)
    private String name;
    
    @XmlElement(required = true)
    private String characterClass;
    
    @XmlElement(required = true)
    private Integer level;
    
    @XmlElement(required = true)
    private Integer power;
    
    @XmlElement(required = true)
    private Integer armor;
    
    @XmlElement(required = true)
    private Integer life;
    
    @XmlElement(required = true)
    private Integer strength;
    
    @XmlElement(required = true)
    private Integer intelligence;
    
    @XmlElement(required = true)
    private Integer willpower;
    
    @XmlElement(required = true)
    private Integer dexterity;
    
    @XmlElement
    private String message;

    public CharacterResponseDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getArmor() {
        return armor;
    }

    public void setArmor(Integer armor) {
        this.armor = armor;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getWillpower() {
        return willpower;
    }

    public void setWillpower(Integer willpower) {
        this.willpower = willpower;
    }

    public Integer getDexterity() {
        return dexterity;
    }

    public void setDexterity(Integer dexterity) {
        this.dexterity = dexterity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}