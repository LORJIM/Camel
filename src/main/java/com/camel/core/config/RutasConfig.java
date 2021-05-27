package com.camel.core.config;

public class RutasConfig { //definimos los endpoint de las rutas camel para una facil escalabilidad y parametrizacion
	//utilizamos direct para asegurarnos de que las request a las rutas de ejecutaran de manera sincrona, es decir, hasta que no haya terminado
	//de ejecutarse el camel, no se ejecuta lo que venga debajo en el endpoint REST
	public static String CAMELPRUEBASPOST="direct:pruebaspost";
	public static String CAMELPRUEBASGETBBDD="direct:pruebasgetbbdd";
}
