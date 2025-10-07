package com.example.DiabloApi.entity;

import java.util.UUID;

import com.example.DiabloApi.enums.CharacterClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "character_class", nullable = false)
    private CharacterClass characterClass;

    @Min(value = 1, message = "Level must be at least 1")
    @Column(name = "level", nullable = false)
    private int level;

    @Min(value = 1, message = "Power must be at least 1")
    @Column(name = "power", nullable = false)
    private int power;

    @Min(value = 1, message = "Armor must be at least 1")
    @Column(name = "armor", nullable = false)
    private int armor;

    @Min(value = 1, message = "Life must be at least 1")
    @Column(name = "life", nullable = false)
    private int life;

    @Embedded
private Stats stats;

    public Character() {
    }

    public Character(UUID id, String name, CharacterClass characterClass, int level, int power, int armor, int life,
            int strength, int intelligence, int willpower, int dexterity) {
        this.id = id;
        this.name = name;
        this.characterClass = characterClass;
        this.level = level;
        this.power = power;
        this.armor = armor;
        this.life = life;
        this.stats = new Stats(strength, intelligence, willpower, dexterity);
}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}