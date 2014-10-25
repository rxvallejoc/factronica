/**
 * 
 */
package com.obiectumclaro.factronica.core.web.service.sri.client;

import java.util.List;

import com.obiectumclaro.factronica.core.web.service.sri.client.reception.Comprobante;

/**
 * Exception to denote an invoice has not been received. The error message for
 * the invoice(s) can be retrieved using {@link #getComprobantes()}.
 * 
 * @author iapazmino
 * 
 */
public class ReturnedInvoiceException extends Exception {


	private static final long serialVersionUID = 1L;
	private final List<Comprobante> comprobantes;

	public ReturnedInvoiceException(final List<Comprobante> comprobantes) {
		super("El comprobante no fue recibido");
		this.comprobantes = comprobantes;
	}

	public List<Comprobante> getComprobantes() {
		return comprobantes;
	}

}
