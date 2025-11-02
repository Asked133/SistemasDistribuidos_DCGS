package com.example.DiabloApi.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurationSupport;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurationSupport {

    private static final String NAMESPACE_URI = "http://DiabloApi.example.com/characters";

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "characters")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema charactersSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CharactersPort");
        wsdl11Definition.setServiceName("CharactersService");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(NAMESPACE_URI);
        wsdl11Definition.setSchema(charactersSchema);
        wsdl11Definition.setCreateSoap11Binding(true);
        wsdl11Definition.setCreateSoap12Binding(false);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema charactersSchema() {
        return new SimpleXsdSchema(new ClassPathResource("characters.xsd"));
    }

    @Bean(name = "marshaller")
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(
            "com.example.DiabloApi.dto.request",
            "com.example.DiabloApi.dto.response"
        );
        return marshaller;
    }
}