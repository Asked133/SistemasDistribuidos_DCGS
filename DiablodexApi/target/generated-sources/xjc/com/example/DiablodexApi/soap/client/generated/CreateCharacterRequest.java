//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.11.30 a las 03:56:21 PM CST 
//


package com.example.DiablodexApi.soap.client.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="characterClass" type="{http://DiabloApi.example.com/characters}characterClassEnum"/&gt;
 *         &lt;element name="level" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="power" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="armor" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="life" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="strength" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="intelligence" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="willpower" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="dexterity" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "characterClass",
    "level",
    "power",
    "armor",
    "life",
    "strength",
    "intelligence",
    "willpower",
    "dexterity"
})
@XmlRootElement(name = "createCharacterRequest")
public class CreateCharacterRequest {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected CharacterClassEnum characterClass;
    protected Integer level;
    protected Integer power;
    protected Integer armor;
    protected Integer life;
    protected int strength;
    protected int intelligence;
    protected int willpower;
    protected int dexterity;

    /**
     * Obtiene el valor de la propiedad name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define el valor de la propiedad name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Obtiene el valor de la propiedad characterClass.
     * 
     * @return
     *     possible object is
     *     {@link CharacterClassEnum }
     *     
     */
    public CharacterClassEnum getCharacterClass() {
        return characterClass;
    }

    /**
     * Define el valor de la propiedad characterClass.
     * 
     * @param value
     *     allowed object is
     *     {@link CharacterClassEnum }
     *     
     */
    public void setCharacterClass(CharacterClassEnum value) {
        this.characterClass = value;
    }

    /**
     * Obtiene el valor de la propiedad level.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Define el valor de la propiedad level.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLevel(Integer value) {
        this.level = value;
    }

    /**
     * Obtiene el valor de la propiedad power.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPower() {
        return power;
    }

    /**
     * Define el valor de la propiedad power.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPower(Integer value) {
        this.power = value;
    }

    /**
     * Obtiene el valor de la propiedad armor.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getArmor() {
        return armor;
    }

    /**
     * Define el valor de la propiedad armor.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setArmor(Integer value) {
        this.armor = value;
    }

    /**
     * Obtiene el valor de la propiedad life.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLife() {
        return life;
    }

    /**
     * Define el valor de la propiedad life.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLife(Integer value) {
        this.life = value;
    }

    /**
     * Obtiene el valor de la propiedad strength.
     * 
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Define el valor de la propiedad strength.
     * 
     */
    public void setStrength(int value) {
        this.strength = value;
    }

    /**
     * Obtiene el valor de la propiedad intelligence.
     * 
     */
    public int getIntelligence() {
        return intelligence;
    }

    /**
     * Define el valor de la propiedad intelligence.
     * 
     */
    public void setIntelligence(int value) {
        this.intelligence = value;
    }

    /**
     * Obtiene el valor de la propiedad willpower.
     * 
     */
    public int getWillpower() {
        return willpower;
    }

    /**
     * Define el valor de la propiedad willpower.
     * 
     */
    public void setWillpower(int value) {
        this.willpower = value;
    }

    /**
     * Obtiene el valor de la propiedad dexterity.
     * 
     */
    public int getDexterity() {
        return dexterity;
    }

    /**
     * Define el valor de la propiedad dexterity.
     * 
     */
    public void setDexterity(int value) {
        this.dexterity = value;
    }

}
