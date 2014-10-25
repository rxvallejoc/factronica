/**
 * 
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 * List of id a client might use.
 * 
 * @author iapazmino
 *
 */
public enum IdType {

	CEDULA, RUC, PASAPORTE, CONSUMIDOR_FINAL;

	public static IdType getTypeOf(final String tipoId) {
		switch (tipoId) {
		case "04": return RUC;
		case "05": return CEDULA;
		case "06": return PASAPORTE;
		case "07": return CONSUMIDOR_FINAL;
		default:
			throw new IllegalArgumentException(tipoId + " is not a valid Id Type");
		}
	}
}
