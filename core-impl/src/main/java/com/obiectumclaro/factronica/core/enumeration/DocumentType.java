/**
 * 
 */
package com.obiectumclaro.factronica.core.enumeration;

/**
 * Available type of documents to be electronically isseued.
 * 
 * @author iapazmino
 *
 */
public enum DocumentType {

	FACTURA("01"), 
	NOTA_CREDITO("04"), 
	NOTA_DEBITO("05"), 
	GUIA_REMISION("06"), 
	COMPROBANTE_RETENCION("07");
	
	public final String code;

	private DocumentType(final String code) {
		this.code = code;
	}
	
}
