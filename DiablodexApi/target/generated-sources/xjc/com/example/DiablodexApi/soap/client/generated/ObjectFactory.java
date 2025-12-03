//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.11.01 a las 10:59:17 PM CST 
//


package com.example.DiablodexApi.soap.client.generated;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.DiablodexApi.soap.client.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateCharacterResponse_QNAME = new QName("http://DiabloApi.example.com/characters", "createCharacterResponse");
    private final static QName _UpdateCharacterResponse_QNAME = new QName("http://DiabloApi.example.com/characters", "updateCharacterResponse");
    private final static QName _GetCharacterByIdResponse_QNAME = new QName("http://DiabloApi.example.com/characters", "getCharacterByIdResponse");
    private final static QName _GetCharacterByNameResponse_QNAME = new QName("http://DiabloApi.example.com/characters", "getCharacterByNameResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.DiablodexApi.soap.client.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateCharacterRequest }
     * 
     */
    public CreateCharacterRequest createCreateCharacterRequest() {
        return new CreateCharacterRequest();
    }

    /**
     * Create an instance of {@link UpdateCharacterRequest }
     * 
     */
    public UpdateCharacterRequest createUpdateCharacterRequest() {
        return new UpdateCharacterRequest();
    }

    /**
     * Create an instance of {@link DeleteCharacterRequest }
     * 
     */
    public DeleteCharacterRequest createDeleteCharacterRequest() {
        return new DeleteCharacterRequest();
    }

    /**
     * Create an instance of {@link GetAllCharactersRequest }
     * 
     */
    public GetAllCharactersRequest createGetAllCharactersRequest() {
        return new GetAllCharactersRequest();
    }

    /**
     * Create an instance of {@link GetCharacterByIdRequest }
     * 
     */
    public GetCharacterByIdRequest createGetCharacterByIdRequest() {
        return new GetCharacterByIdRequest();
    }

    /**
     * Create an instance of {@link GetCharacterByNameRequest }
     * 
     */
    public GetCharacterByNameRequest createGetCharacterByNameRequest() {
        return new GetCharacterByNameRequest();
    }

    /**
     * Create an instance of {@link GetCharactersByClassRequest }
     * 
     */
    public GetCharactersByClassRequest createGetCharactersByClassRequest() {
        return new GetCharactersByClassRequest();
    }

    /**
     * Create an instance of {@link CharacterDetails }
     * 
     */
    public CharacterDetails createCharacterDetails() {
        return new CharacterDetails();
    }

    /**
     * Create an instance of {@link DeleteCharacterResponse }
     * 
     */
    public DeleteCharacterResponse createDeleteCharacterResponse() {
        return new DeleteCharacterResponse();
    }

    /**
     * Create an instance of {@link GetAllCharactersResponse }
     * 
     */
    public GetAllCharactersResponse createGetAllCharactersResponse() {
        return new GetAllCharactersResponse();
    }

    /**
     * Create an instance of {@link GetCharactersByClassResponse }
     * 
     */
    public GetCharactersByClassResponse createGetCharactersByClassResponse() {
        return new GetCharactersByClassResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CharacterDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CharacterDetails }{@code >}
     */
    @XmlElementDecl(namespace = "http://DiabloApi.example.com/characters", name = "createCharacterResponse")
    public JAXBElement<CharacterDetails> createCreateCharacterResponse(CharacterDetails value) {
        return new JAXBElement<CharacterDetails>(_CreateCharacterResponse_QNAME, CharacterDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CharacterDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CharacterDetails }{@code >}
     */
    @XmlElementDecl(namespace = "http://DiabloApi.example.com/characters", name = "updateCharacterResponse")
    public JAXBElement<CharacterDetails> createUpdateCharacterResponse(CharacterDetails value) {
        return new JAXBElement<CharacterDetails>(_UpdateCharacterResponse_QNAME, CharacterDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CharacterDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CharacterDetails }{@code >}
     */
    @XmlElementDecl(namespace = "http://DiabloApi.example.com/characters", name = "getCharacterByIdResponse")
    public JAXBElement<CharacterDetails> createGetCharacterByIdResponse(CharacterDetails value) {
        return new JAXBElement<CharacterDetails>(_GetCharacterByIdResponse_QNAME, CharacterDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CharacterDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CharacterDetails }{@code >}
     */
    @XmlElementDecl(namespace = "http://DiabloApi.example.com/characters", name = "getCharacterByNameResponse")
    public JAXBElement<CharacterDetails> createGetCharacterByNameResponse(CharacterDetails value) {
        return new JAXBElement<CharacterDetails>(_GetCharacterByNameResponse_QNAME, CharacterDetails.class, null, value);
    }

}
