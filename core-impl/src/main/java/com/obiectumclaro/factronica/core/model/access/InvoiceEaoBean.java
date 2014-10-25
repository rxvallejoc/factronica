/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model.access;

import java.util.List;

import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.Invoice;

/**
 * 
 * @author marco zaragocin
 */
@Stateless
public class InvoiceEaoBean extends BaseEaoBean {
	/**
	 * Persists a new product
	 * 
	 * @param product
	 */
	public void save(final Invoice invoice) {
		entityManager.persist(invoice);
	}

	/**
	 * Obtains all products
	 * 
	 * @return list of products
	 */
	public List<Invoice> findAll() {
		return getResultListFromNamedQuery(Invoice.class, "Product.findAll");
	}

	/**
	 * Obtains an Invoice by Pk
	 * 
	 * @param pk
	 * @return Invoice
	 */
	public Invoice findByPk(Long pk) {
		return entityManager.find(Invoice.class, pk);
	}
}
