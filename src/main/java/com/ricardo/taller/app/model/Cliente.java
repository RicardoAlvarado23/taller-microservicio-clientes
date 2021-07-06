package com.ricardo.taller.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ricardo.taller.app.util.UtilFunctions;

@Entity
@Table(name = "cliente")
public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public String id;
	
	@Column(nullable = false)
	public String nombre;
	
	@Column(nullable = false)
	public String apellido;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@Column(name = "fecha_nacimiento")
	@Temporal(TemporalType.TIMESTAMP)
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
	public Integer getEdad() {
		return UtilFunctions.calcularEdad(getFechaNacimiento());
	}
	
	public String getFechaDefuncion() {
		Date fechaDefuncion = UtilFunctions.calcularFechaDefuncion(getFechaNacimiento());
		return  UtilFunctions.formatearFecha(fechaDefuncion, "dd/MM/yyyy");
	}
	
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", edad=" + getEdad()
				+ ", fechaNacimiento=" + fechaNacimiento + "]";
	}
	

	
	
}
