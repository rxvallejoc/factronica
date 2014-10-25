/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 *
 * @author marco
 */
public enum SiNo {
    SI("SI"), NO("NO");
 
    private String valor; 
    
    SiNo(String valor) {
        this.valor = valor;
    }
 
    // Devuelve la capacidad del vaso
    public String getValor() {
        return valor;
    }
}
