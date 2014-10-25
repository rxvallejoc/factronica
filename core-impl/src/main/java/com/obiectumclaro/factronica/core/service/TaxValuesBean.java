/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.model.access.TaxValueEaoBean;

/**
 * @author faustodelatog
 * 
 */
@Stateless
public class TaxValuesBean {

	@EJB
	private TaxValueEaoBean taxValueEao;

	public List<TaxValue> findAllTaxes() {
		return taxValueEao.findAll();
	}
	
	public TaxValue findById(final Long id) {
		return taxValueEao.findById(id);
	}
	
	public TaxValue findByCode(final String code){
		return taxValueEao.findByCode(code);
	}
	
}
