package com.camel.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.camel.core.conectores.ConectorWS;

@Configuration
public class ConectorsConfig { //aqui se configuran los conectores, uno por server
	@Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.jaimeMW.springsoap.client.gen"); //el paquete de clases JAXB que escanean y utilizan los conectores, preferiblemente comun para todos
        return marshaller;
    }
    @Bean
    public ConectorWS conectorWS(Jaxb2Marshaller marshaller) {
        ConectorWS conectorWS = new ConectorWS();
        conectorWS.setDefaultUri("http://localhost:8080/ws"); //direccion del server al que llama este conector
        conectorWS.setMarshaller(marshaller);
        conectorWS.setUnmarshaller(marshaller);
        return conectorWS;
    }
}
