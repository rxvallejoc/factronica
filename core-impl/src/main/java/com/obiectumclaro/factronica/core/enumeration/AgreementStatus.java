/**
 * 
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 * @author faustodelatog
 * 
 */
public enum AgreementStatus {

	SEND("ENVIADO"), ACCEPTED("ACEPTADO"), REJECTED("RECHAZADO");

	private String description;

	private AgreementStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
