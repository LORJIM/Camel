package com.camel.core.controller;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camel.core.config.RutasConfig;
import com.camel.core.models.CAMELPRUEBASGETBBDD;
import com.camel.core.models.CAMELPRUEBASPOST;

@RestController
@RequestMapping("/")
public class ProductREST {
	
	@Autowired
    private CamelContext camelContext; //como el contexto de la app, pero para camel, solo se tocaria si hubiera que hacer algo nada mas arrancar las rutas,
	//como por ejemplo, asociarlas a un xsd
	
	@Autowired
	private ProducerTemplate producer; //el producer en camel es el que envia una request y recibe una respuesta
	
	public Object executeCamel(String rutaCamel, Object request){ //esta estructura es comun para ejecutar cualquier camel
		Exchange sendExchange = ExchangeBuilder.anExchange(camelContext).withBody(request).build(); //construimos un exchange cuyo body contiene los datos de entrada AL CAMEL (si es que hay)
		Exchange outExchange = producer.send(rutaCamel, sendExchange); //el producer envia a la ruta del camel el exchange de entrada, es decir, ejecuta dicho camel
		return outExchange.getMessage().getBody(); //devolvemos el body del exchange de salida del camel
	}
	
	@GetMapping(path= "/pruebasgetbbdd") //un get que consulta en bbdd y ejecuta un WS (si le mandamos el country)
	public ResponseEntity<Object> pruebasgetbbdd(@RequestBody(required=false) CAMELPRUEBASGETBBDD request) { //los datos de entrada no son obligatorios porque si va vacio igualmente funcionara la consulta en bbdd (es un findAll)
		return ResponseEntity.ok(executeCamel(RutasConfig.CAMELPRUEBASGETBBDD,request)); //ejecutar camel, indicando ruta y request
	}
	
	@PostMapping(path= "/pruebaspost")
	public ResponseEntity<Object> createProduct(@RequestBody CAMELPRUEBASPOST request){
		return ResponseEntity.ok(executeCamel(RutasConfig.CAMELPRUEBASPOST,request)); //ejecutar camel, indicando ruta y request
	}
	
	
}
