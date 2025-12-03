package com.example.DiabloApi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stats", propOrder = {
    "strength",
    "intelligence", 
    "willpower",
    "dexterity"
})
public class Stats {
    @XmlElement(required = true)
    @Min(value = 1)
    @Column(name = "strength")
    private int strength;

    @XmlElement(required = true)
    @Min(value = 1)
    @Column(name = "intelligence")
    private int intelligence;

    @XmlElement(required = true)
    @Min(value = 1)
    @Column(name = "willpower")
    private int willpower;

    @XmlElement(required = true)
    @Min(value = 1)
    @Column(name = "dexterity")
    private int dexterity;

    public Stats() {
    }

    public Stats(int strength, int intelligence, int willpower, int dexterity) {
        this.strength = strength;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.dexterity = dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }
}