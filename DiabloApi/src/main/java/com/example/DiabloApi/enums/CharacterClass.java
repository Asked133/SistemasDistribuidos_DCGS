package com.example.DiabloApi.enums;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "characterClass")
@XmlEnum
public enum CharacterClass {
    @XmlEnumValue("BARBARIAN")
    BARBARIAN("Barbarian", "Melee combat specialist"),

    @XmlEnumValue("SORCERER")
    SORCERER("Sorcerer", "Elemental magic master"),

    @XmlEnumValue("NECROMANCER")
    NECROMANCER("Necromancer", "Death magic wielder"),

    @XmlEnumValue("ROGUE")
    ROGUE("Rogue", "Agile ranged fighter"),

    @XmlEnumValue("DRUID")
    DRUID("Druid", "Shape-shifting nature warrior"),

    @XmlEnumValue("SPIRITBORN")
    SPIRITBORN("Spiritborn", "Spiritual magic user");

    private final String displayName;
    private final String description;

    CharacterClass(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}