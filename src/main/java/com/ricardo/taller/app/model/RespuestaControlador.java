package com.ricardo.taller.app.model;

import com.ricardo.taller.app.enums.StatusRest;

public class RespuestaControlador<T> {

	public Boolean success;
	public String status;
	public String message;
	public T data;
	
	public RespuestaControlador() {
		// TODO Auto-generated constructor stub
	}
	
	public RespuestaControlador(String status, String message) {
		super();
		this.status = status;
		this.message = message;
		if (this.status.equals(StatusRest.EXITO.getCode())) {
			this.success = true;
		} else {
			this.success = false;
		}
	}



	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	
}
