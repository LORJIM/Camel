package com.camel.core.conectores;

import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;

import com.camel.core.dao.ProductsDAO;
import com.camel.core.entities.Product;


public class ConectorBBDD { //simula un conector tuxedo, o uno de metadatos, lo que sea para obtener o actualizar info de bbdd
	@Autowired
	private ProductsDAO productDAO;
	
	public void execute(final Exchange exchange) throws Exception{
		if(exchange.getIn().getHeader("serviceName")=="guardarProducto") {
			Product newProduct=productDAO.save(exchange.getIn().getHeader("producto",Product.class)); //ejecutamos el UPDATE con Hibernate
			exchange.getIn().setHeader("respuestaGuardarProducto",newProduct); //como existe la posibilidad de que en el futuro se ejecuten varios services, 
			//seteamos el resultado de este service en un header de respuesta del body que es lo recomendable
		}else if(exchange.getIn().getHeader("serviceName")=="obtenerProductos"){
			List<Product> products=productDAO.findAll();
			exchange.getIn().setHeader("respuestaObtenerProductos",products);
		}
		
	}

}
