package com.ricardo.taller.app.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "El atributo id se debe enviar cuando se quiera realizar una actualizacion")
public class ClienteDTO {

	@ApiModelProperty(required = false, hidden = true)
	public String id;
	
	@ApiModelProperty(required = true, example = "admin")
	public String nombre;
	
	@ApiModelProperty(required = true, example = "admin")
	public String apellido;
	
	@ApiModelProperty(required = true, notes = "dd/MM/yyyy", example = "23/03/1995")
	@JsonFormat(pattern="dd/MM/yyyy")
	public Date fechaNacimiento;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	
	
}
