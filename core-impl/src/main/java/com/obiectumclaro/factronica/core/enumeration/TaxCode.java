/**
 * 
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 * Taxes codes defined by SRI, table 15.
 * 
 * @author iapazmino
 *
 */
public enum TaxCode {

	IVA(2L), ICE(3L);
	
	public final Long code;

	private TaxCode(Long code) {
		this.code = code;
	}
	
}
