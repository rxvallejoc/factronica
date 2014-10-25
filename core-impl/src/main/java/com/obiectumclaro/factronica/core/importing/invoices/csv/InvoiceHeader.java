/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.importing.invoices.csv;

import com.obiectumclaro.factronica.core.enumeration.IdType;

/**
 * 
 * @author marco
 */
public class InvoiceHeader {
	
	private IdType tipoIdentificacion;
	private String numeroIdentificacion;
	private String fechaEmision;
	
	private final int lineNumber;

	public InvoiceHeader(final InvoiceLine line, final int lineNumber) {
		this.tipoIdentificacion = line.getIdType();
		this.numeroIdentificacion = line.getId();
		this.fechaEmision = line.getIssuedDate();
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public IdType getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion
	 *            the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(IdType tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion
	 *            the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the fechaEmision
	 */
	public String getFechaEmision() {
		return fechaEmision;
	}

	/**
	 * @param fechaEmision
	 *            the fechaEmision to set
	 */
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fechaEmision == null) ? 0 : fechaEmision.hashCode());
		result = prime
				* result
				+ ((numeroIdentificacion == null) ? 0 : numeroIdentificacion
						.hashCode());
		result = prime
				* result
				+ ((tipoIdentificacion == null) ? 0 : tipoIdentificacion
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof InvoiceHeader)) {
			return false;
		}
		InvoiceHeader other = (InvoiceHeader) obj;
		if (fechaEmision == null) {
			if (other.fechaEmision != null) {
				return false;
			}
		} else if (!fechaEmision.equals(other.fechaEmision)) {
			return false;
		}
		if (numeroIdentificacion == null) {
			if (other.numeroIdentificacion != null) {
				return false;
			}
		} else if (!numeroIdentificacion.equals(other.numeroIdentificacion)) {
			return false;
		}
		if (tipoIdentificacion != other.tipoIdentificacion) {
			return false;
		}
		return true;
	}

	public int getLineNumber() {
		return lineNumber;
	}

}
