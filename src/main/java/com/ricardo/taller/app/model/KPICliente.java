package com.ricardo.taller.app.model;

import java.math.BigDecimal;

public class KPICliente {

	private BigDecimal promedio;
	private double desviacionEstandar;
	
	public BigDecimal getPromedio() {
		return promedio;
	}
	public void setPromedio(BigDecimal promedio) {
		this.promedio = promedio;
	}
	public double getDesviacionEstandar() {
		return desviacionEstandar;
	}
	public void setDesviacionEstandar(double desviacionEstandar) {
		this.desviacionEstandar = desviacionEstandar;
	}
	
	
}
