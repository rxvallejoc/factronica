/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.products;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;

/**
 * @author iapazmino
 *
 */
@Named
public class HeaderLabelConverter implements Converter {

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		final String header = value.substring(value.lastIndexOf("=") + 1, value.lastIndexOf("]"));
		final String label = value.substring(value.indexOf("=") + 1, value.indexOf(","));
		return new HeaderLabel(label, Integer.parseInt(header));
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}

}
