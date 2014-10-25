/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 *
 * @author marco
 */
public enum EnvironmentXML {
    
    PRUEBAS("PRUEBAS","1"), PRODUCCION("PRODUCCION","2");
 
    private String valor;
    private String descripcion;
    
    EnvironmentXML(String valor,String descripcion) {
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
