/**
 * 
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 * Codes assigned to IVA's rates.
 * 
 * @author iapazmino
 *
 */
public enum IvaRateCode {

	ZERO("0"), TWELVE("2"), NOT_TAXABLE("6");
	
	public final String code;

	private IvaRateCode(String code) {
		this.code = code;
	}
	
}
