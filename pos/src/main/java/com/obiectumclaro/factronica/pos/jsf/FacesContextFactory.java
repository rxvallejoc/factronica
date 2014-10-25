/**
 * 
 */
package com.obiectumclaro.factronica.pos.jsf;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * This factory produces all {@link FacesContext}-related objects which are not
 * directly provided for CDI injection.
 * 
 * @author iapazmino
 * 
 */
@Named
public class FacesContextFactory {

	/**
	 * @return the current {@link FacesContext} instance.
	 */
	@Produces
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

}
