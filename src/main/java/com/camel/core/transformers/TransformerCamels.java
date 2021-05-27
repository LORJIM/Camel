package com.camel.core.transformers;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.camel.core.config.RutasConfig;
import com.camel.core.entities.Product;
import com.camel.core.models.CAMELPRUEBASGETBBDD;
import com.camel.core.models.CAMELPRUEBASPOST;
import com.jaimeMW.springsoap.client.gen.Country;
import com.jaimeMW.springsoap.client.gen.GetCountryRequest;



public class TransformerCamels {

	private final Log logger= LogFactory.getLog(getClass()); //el "objeto" log para esta clase
	
	public void translateInput(final Exchange exchange) throws Exception { //este metodo mapea datos de entrada, previo a la ejecucion de la query o el WS
		//dependiendo del servicio, cada uno requerira un mapeo de datos distinto
		String serviceName=(String)exchange.getIn().getHeader("serviceName");
		
		if(serviceName=="guardarProducto") {
			logger.debug("Preparando datos, body: " + exchange.getIn().getBody().toString());
			CAMELPRUEBASPOST entradaCamel=(CAMELPRUEBASPOST)exchange.getIn().getBody(); //utilizamos una clase camel pensando en la escalabilidad,
			//para que aloje todos los posibles datos de entrada de los servicios que pueda ejecutar
			
			//mapeamos los datos del camel al JPA que necesitamos para ejecutar la query, tambien podemos hacer los formateos que queramos
			Product product=new Product();
			product.setName(entradaCamel.getName());
			product.setDesc(entradaCamel.getDesc()+"-MODIFICADA");
			exchange.getIn().setHeader("producto",product); //seteo en el header el JPA con los datos ya procesados
			
			logger.debug("PRODUCTO: "+product.toString());
		}else if(serviceName=="obtenerPais") {
			GetCountryRequest request = new GetCountryRequest(); //creamos la request JAXB
	        CAMELPRUEBASGETBBDD entradaCamel=(CAMELPRUEBASGETBBDD)exchange.getIn().getBody();
	        
	        request.setName(entradaCamel.getCountry()); //seteamos los campos de entrada del camel en la request JAXB
	        exchange.getIn().setHeader("requestPais",request); //seteo en el header la request con los datos ya procesados
		}
	}
	
	public void translateOutput(final Exchange exchange) throws Exception { 
		/*este metodo mapea datos de salida, si queremos los podemos formatear, pero principalmente lo que hace es coger los header de respuesta(que se setean en las ejecuciones de WS o queries)
		 *  y los mapea en un body comun en JSON, que es lo que le llegara a la api como exchange de respuesta*/
		
		//dependiendo del servicio, cada uno requerira un mapeo de datos distinto
		String camelRoute=(String)exchange.getIn().getHeader("camelRoute");
		
		if(camelRoute==RutasConfig.CAMELPRUEBASPOST) { 
			exchange.getIn().setBody(exchange.getIn().getHeader("respuestaGuardarProducto")); //como este camel solo ejecuta un service, pues seteamos el unico header directamente en el body
			
		}else if(camelRoute==RutasConfig.CAMELPRUEBASGETBBDD) { //como este camel SI ejecuta varios services, es obligatorio mapear sus headers de respuesta en un JSON
			JSONObject respuesta= new JSONObject();
			respuesta.put("listProducts",exchange.getIn().getHeader("respuestaObtenerProductos")); //los nombres que pongamos a las keys seran los que le aparezan al cliente final en la response
			respuesta.put("country",exchange.getIn().getHeader("respuestaObtenerPais"));
			logger.debug(respuesta);
			exchange.getIn().setBody(respuesta); //seteamos el JSON con todos los datos en el body del exchange de salida
		}
	}
}
