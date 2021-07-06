package com.ricardo.taller.app.enums;

/**
 * Codigo de status para respuestas de los servicios REST
 * @author ricardo
 *
 */
public enum StatusRest {
    EXITO("00"),
    ERROR("99");
    
    private final String code;
    
    StatusRest(String code){
        this.code = code;
    }

    /**
     * @return the valor
     */
    public String getCode() {
        return code;
    }
}