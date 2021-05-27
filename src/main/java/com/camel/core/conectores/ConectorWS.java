package com.camel.core.conectores;

import org.apache.camel.Exchange;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.jaimeMW.springsoap.client.gen.GetCountryRequest;
import com.jaimeMW.springsoap.client.gen.GetCountryResponse;

public class ConectorWS extends WebServiceGatewaySupport { //el conector es el que manda la request XML al server y recibe la response
	
	//aqui estaran todos los web services de este conector/server
	public void getCountry(final Exchange exchange) {
		
		GetCountryRequest request=(GetCountryRequest)exchange.getIn().getHeader("requestPais"); //obtengo la request del header que seteamos en el transformer
        GetCountryResponse response = (GetCountryResponse) getWebServiceTemplate()
          .marshalSendAndReceive(request); //llamamos al server y nos responde
		exchange.getIn().setHeader("respuestaObtenerPais",(response.getCountry())); //seteo la respuesta en otro header
    }
}
