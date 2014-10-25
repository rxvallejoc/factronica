/**
 * 
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 * Modes available to issue invoices and other documents.
 * 
 * @author iapazmino
 * 
 */
public enum IssuingMode {

	NORMAL, CONTINGENCIA;

	public static IssuingMode valueOf(final int tipoEmision) {
		switch (tipoEmision) {
		case 1:
			return NORMAL;
		case 2:
			return CONTINGENCIA;
		default:
			throw new IllegalArgumentException(tipoEmision + " is not a valid Issuing Mode");
		}
	}

}
