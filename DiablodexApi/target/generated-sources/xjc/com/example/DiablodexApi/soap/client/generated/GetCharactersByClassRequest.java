//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.12.02 a las 10:20:18 PM CST 
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
 *         &lt;element name="characterClass" type="{http://DiabloApi.example.com/characters}characterClassEnum"/&gt;
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
    "characterClass"
})
@XmlRootElement(name = "getCharactersByClassRequest")
public class GetCharactersByClassRequest {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected CharacterClassEnum characterClass;

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

}
