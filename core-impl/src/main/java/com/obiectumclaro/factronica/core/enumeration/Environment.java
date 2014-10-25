/**
 * 
 */
package com.obiectumclaro.factronica.core.enumeration;


/**
 * Environment to which the document is issued for.
 * 
 * @author iapazmino
 * 
 */
public enum Environment {

	PRUEBAS, PRODUCCION;

	public static Environment valueOf(final int ordinal) {
		switch (ordinal) {
		case 1:
			return PRUEBAS;
		case 2:
			return PRODUCCION;
		default:
			throw new IllegalArgumentException(ordinal + " is not a valid Environment");
		}
	}

}
