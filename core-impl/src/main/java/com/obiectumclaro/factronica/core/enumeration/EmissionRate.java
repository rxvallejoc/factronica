/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 *
 * @author marco
 */
public enum EmissionRate {
     NORMAL("NORMAL","1"), CONTINGENCIA("INDISPONIBILIDAD DEL SISTEMA","2");
     private String valor;
    private String descripcion;
    
    EmissionRate(String valor,String descripcion) {
        this.valor = valor;
        this.descripcion=descripcion;
    }
 
    // Devuelve la capacidad del vaso
    public String getValor() {
        return valor;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
     public String toString(){
         return this.name();
     }
}
