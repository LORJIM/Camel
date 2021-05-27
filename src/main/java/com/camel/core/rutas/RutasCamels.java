package com.camel.core.rutas;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.camel.core.conectores.ConectorBBDD;
import com.camel.core.conectores.ConectorWS;
import com.camel.core.config.RutasConfig;
import com.camel.core.transformers.TransformerCamels;


@Component
public class RutasCamels extends RouteBuilder{

	@Override
	public void configure() throws Exception {
        
		errorHandler(defaultErrorHandler().maximumRedeliveries(0));
		
		from(RutasConfig.CAMELPRUEBASGETBBDD) //las rutas que llamamos desde la API estan parametrizadas
			.log("Entrando en "+RutasConfig.CAMELPRUEBASGETBBDD)
			.setHeader("camelRoute", constant(RutasConfig.CAMELPRUEBASGETBBDD)) //seteamos un header con la ruta del camel, esto servira para hacer los mapeos de salida en el transformer
			.setHeader("country", simple("${body.country}")) //seteamos el header country con el dato que puede venir informado en el body, esto nos sirve para comprobar su valor mas abajo en el flujo
			.to("direct:getproducts") //ejecutamos esta ruta para obtener los products
			.choice() //indica que vamos a utilizar una estructura if alike
				.when(header("country").isNotNull()) //si el dato country viene informado
					.to("direct:getcountry") //ejecutamos esta ruta
			.end()
			.bean(TransformerCamels.class, "translateOutput") //mapeo de salida del camel
			.log("Body de salida: ${body}")
		.end();
			
		from("direct:getproducts") //las rutas que se llaman dentro de camels no estan parametrizadas, se entiende que no van a cambiar porque son especificas y no se llaman desde fuera
		//este tipo de rutas podriamos denominarlas rutas secundarias y estan asociadas a un servicio, para dar flexibilidad y escalabilidad a la app
			.setHeader("serviceName", constant("obtenerProductos")) //seteamos un header llamado 'serviceName' en el exchange, con una constante como valor ('obtenerProductos')
			//el header serviceName sirve para poder identificar en el conector lo que queremos hacer, en este caso obtenerProductos
			//la constante significa que el valor de este header no puede cambiar en ningun sitio, SALVO en el propio camel, como hacemos mas abajo
			.bean(ConectorBBDD.class, "execute") //ejecutamos el metodo 'execute' de la clase ConectorBBDD, es decir, la consulta en BBDD (findAll)
			.log("Ejecucion correcta")
		.end();
		
		from("direct:getcountry")
			.setHeader("serviceName", constant("obtenerPais")) //nueva ruta, nuevo servicio
			.bean(TransformerCamels.class, "translateInput") //mapeo previo de datos de entrada al WS
			.log("Body antes de ejecutar el WS: ${body}")
			.bean(ConectorWS.class, "getCountry") //ejecutamos el WS
			.log("Ejecucion correcta")
		.end();
		
		
		from(RutasConfig.CAMELPRUEBASPOST)
			.log("Entrando en "+RutasConfig.CAMELPRUEBASPOST)
			.setHeader("camelRoute", constant(RutasConfig.CAMELPRUEBASPOST))
			.setHeader("tipo", simple("${body.tipo}")) //seteo este header de un dato de entrada para poder compararlo en la estructura de abajo
			.choice() //es como un switch pero sin argumento
				.when(header("tipo").isNotNull()) //los when son los cases
					.choice() //puede haber choice dentro de choice
						.when(header("tipo").isEqualTo("D"))
							.log("DERECHA!!!!!!")
						.when(header("tipo").isEqualTo("I"))
							.log("IZQUIERDA!!!!")
						.otherwise()
							.log("Hay un tipo pero no se lo que es, sera Edmundo Vall?")
					.endChoice() //pero estos que estan dentro se cierran con endChoice
				.otherwise()
					.log("No hay tipo informado")
			.end()
			.to("direct:saveproduct") //ejecutamos esta ruta para guardar un nuevo producto
			
			.bean(TransformerCamels.class, "translateOutput")
			.log("Body de salida: ${body}")
		.end();
		
		from("direct:saveproduct")
			.setHeader("serviceName", constant("guardarProducto")) //tipo de servicio, en vez de nombre de tx le decimos directamente que hace
			.bean(TransformerCamels.class, "translateInput")
			.bean(ConectorBBDD.class, "execute")
			.log("Ejecucion correcta")
		.end();
	}

}
