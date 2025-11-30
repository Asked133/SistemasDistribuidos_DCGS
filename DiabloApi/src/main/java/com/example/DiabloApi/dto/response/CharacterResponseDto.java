package com.example.DiabloApi.dto.response;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CharacterDetails", namespace = CharacterResponseDto.NAMESPACE_URI, propOrder = {
    "id", "name", "characterClass", "level", "power", "armor", "life",
    "strength", "intelligence", "willpower", "dexterity", "message"
})
public class CharacterResponseDto {

    static final String NAMESPACE_URI = "http://DiabloApi.example.com/characters";

    @XmlElement(namespace = NAMESPACE_URI)
    private String id;
    @XmlElement(namespace = NAMESPACE_URI)
    private String name;
    @XmlElement(namespace = NAMESPACE_URI)
    private String characterClass;
    @XmlElement(namespace = NAMESPACE_URI)
    private Integer level;
    @XmlElement(namespace = NAMESPACE_URI)
    private Integer power;
    @XmlElement(namespace = NAMESPACE_URI)
    private Integer armor;
    @XmlElement(namespace = NAMESPACE_URI)
    private Integer life;
    @XmlElement(namespace = NAMESPACE_URI)
    private Integer strength;
    @XmlElement(namespace = NAMESPACE_URI)
    private Integer intelligence;
    @XmlElement(namespace = NAMESPACE_URI)
    private Integer willpower;
    @XmlElement(namespace = NAMESPACE_URI)
    private Integer dexterity;
    @XmlElement(namespace = NAMESPACE_URI)
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