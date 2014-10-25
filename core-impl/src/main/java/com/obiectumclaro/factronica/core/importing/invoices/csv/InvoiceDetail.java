/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.importing.invoices.csv;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 
 * @author marco
 */
public class InvoiceDetail {

	private BigDecimal cantidad;
	private String codigoProducto;
	private Map<String, String> infoAdicional;

	private final int lineNumber;

	public InvoiceDetail() {
		lineNumber = 0;
	}

	public InvoiceDetail(final InvoiceLine line, final int lineNumber) {
		this.cantidad = line.getQuantity();
		this.codigoProducto = line.getProductCode();
		this.infoAdicional = line.getAdditionalInfo();
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the codigoProducto
	 */
	public String getCodigoProducto() {
		return codigoProducto;
	}

	/**
	 * @param codigoProducto
	 *            the codigoProducto to set
	 */
	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	/**
	 * @return the cantidad
	 */
	public BigDecimal getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad
	 *            the cantidad to set
	 */
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public Map<String, String> getInfoAdicional() {
		return infoAdicional;
	}

	public void setInfoAdicional(Map<String, String> infoAdicional) {
		this.infoAdicional = infoAdicional;
	}
}