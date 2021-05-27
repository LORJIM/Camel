package com.camel.core.models;

public class CAMELPRUEBASPOST {
	
	private Long id;
	private String name;
	private String desc;
	private String tipo; //D de Derecha y I de Izquierda
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	@Override
	public String toString() {
		return "CAMELPRUEBASPOST [id=" + id + ", name=" + name + ", desc=" + desc + ", tipo=" + tipo + "]";
	}
	
	
}
