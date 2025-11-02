package com.example.DiabloApi.dto.request;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "id", "name", "characterClass", "level", "power", "armor", "life",
        "strength", "intelligence", "willpower", "dexterity"
})
@XmlRootElement(name = "updateCharacterRequest", namespace = "http://DiabloApi.example.com/characters")
public class UpdateCharacterRequestDto {

    @XmlElement(namespace = "http://DiabloApi.example.com/characters", required = true)
    private String id;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private String name;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private String characterClass;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer level;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer power;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer armor;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer life;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer strength;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer intelligence;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer willpower;
    @XmlElement(namespace = "http://DiabloApi.example.com/characters")
    private Integer dexterity;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCharacterClass() { return characterClass; }
    public void setCharacterClass(String characterClass) { this.characterClass = characterClass; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public Integer getPower() { return power; }
    public void setPower(Integer power) { this.power = power; }
    public Integer getArmor() { return armor; }
    public void setArmor(Integer armor) { this.armor = armor; }
    public Integer getLife() { return life; }
    public void setLife(Integer life) { this.life = life; }
    public Integer getStrength() { return strength; }
    public void setStrength(Integer strength) { this.strength = strength; }
    public Integer getIntelligence() { return intelligence; }
    public void setIntelligence(Integer intelligence) { this.intelligence = intelligence; }
    public Integer getWillpower() { return willpower; }
    public void setWillpower(Integer willpower) { this.willpower = willpower; }
    public Integer getDexterity() { return dexterity; }
    public void setDexterity(Integer dexterity) { this.dexterity = dexterity; }
}
