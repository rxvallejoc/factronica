/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.invoice;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.access.CustomerEaoBean;

/**
 * @author Obiectumclaro
 *
 */
@Named
public class CustomerConverter implements Converter{
	@EJB
	private CustomerEaoBean customerEaoBean;

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String submittedValue) {
		 if (submittedValue.trim().equals("")) {  
	            return null;  
	        } else { 
	        	return customerEaoBean.findById(submittedValue);
	        }
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
		if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Customer) value).getId());  
        }
	}

}
