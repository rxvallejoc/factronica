/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.products;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.model.access.TaxValueEaoBean;

/**
 * Converts tax values from string to object and vice versa.
 * 
 * @author iapazmino
 *
 */
@Named
public class TaxValuePickListConverter implements Converter {
	
	@Inject
	private TaxValueEaoBean taxValueEaoBean;


	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public TaxValue getAsObject(FacesContext context, UIComponent component, String value) {
		return taxValueEaoBean.findById(Long.valueOf(value));
		
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}
	
	
}
