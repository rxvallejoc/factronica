/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.customers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.enumeration.Person;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.service.CustomersBean;
import com.obiectumclaro.factronica.core.service.exception.CustomerAdditionException;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;
import com.obiectumclaro.factronica.core.validator.compound.CedulaValidator;
import com.obiectumclaro.factronica.core.validator.compound.CompoundValidator;
import com.obiectumclaro.factronica.core.validator.compound.PasaporteValidator;
import com.obiectumclaro.factronica.core.validator.compound.RUCValidator;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;


/**
 * @author rxvallejoc
 *
 */
@ManagedBean
@ViewScoped
public class CustomerBackingBean{
	
	
	@Inject 
	private CustomersBean customersBean;
	
	private Object idType;	
	private Map<IdType, CompoundValidator> factory;
	private String customerId;
	private Customer newCustomer;
	
	private boolean requestedBefore;
	
	@PostConstruct
	public void setupFactory() {
		factory = new HashMap<IdType, CompoundValidator>();
		factory.put(IdType.CEDULA, new CedulaValidator());
		factory.put(IdType.PASAPORTE, new PasaporteValidator());
		factory.put(IdType.RUC, new RUCValidator());
	}

	public void holdIdType(final FacesContext context, final UIComponent inputText, final Object idType) {
		this.idType = idType;
	}
	
	public IdType[] getIdTypes() {
		return IdType.values();
	}
	
	public void validateId(final FacesContext context, final UIComponent inputText, final Object id) {
		final String idString = (String) id;
		final CompoundValidator validator = factory.get(idType);
		if (null == validator) {
			FacesMessageHelper.addInfo("Clientes", "El tipo de identificaci\u00f3n debe ser CEDULA, PASAPORTE o RUC");
			throwValidatorExceptionWith("El tipo de identificaci\u00f3n debe ser CEDULA, PASAPORTE o RUC");
		}
		final ValidatorResponse resp = validator.validate(idString);
		if (!resp.isValid()) {
			FacesMessageHelper.addError("Clientes", resp.getMessage());
			throwValidatorExceptionWith(resp.getMessage());
		}
	}
	private void throwValidatorExceptionWith(final String summary) {
		final FacesMessage invalidId = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		throw new ValidatorException(invalidId);
	}
	
	public void searchForCustomer(final ComponentSystemEvent systemEvent){
		if (requestedBefore) {
			return;
		}
		if (isAddingCustomer()){
			newCustomer= new Customer();
		}else{
			newCustomer=customersBean.findById(customerId);
		}
		requestedBefore=true;
	}

	public String save() {
		if (isAddingCustomer()) {
			return add();
		} else {
			return update();
		}
	}
	
    private String add() {
        String navRule = null;
        try {
        	customersBean.add(newCustomer);
            navRule = String.format("/agreements/send?to=%s:%s&template=acuerdo-email&faces-redirect=true", newCustomer.getIdType()
                    .name(), newCustomer.getId());
            FacesMessageHelper.addInfo("Clientes", "El cliente ha sido agregado");
        } catch (CustomerAdditionException cae) {
            cae.printStackTrace();
            FacesMessageHelper.addError("Clientes", cae.getMessage());
            navRule = null;
        }
        return navRule;
    }
	
	private String update(){
		String navRule = null;
		try{
                customersBean.update(newCustomer);
		navRule = "/customers/list?faces-redirect=true";
		FacesMessageHelper.addInfo("Clientes","El Cliente ha sido modificado");
                }catch(ConstraintViolationException ce){
                	FacesMessageHelper.addError("Clientes", "Ya existe un usuario con ese n\u00famero de identificaci\u00f3n");
                }
                catch(Exception e){
                	FacesMessageHelper.addError("Clientes", e.getMessage());
                }   
                    
		return navRule;
	}
	
	public boolean isNaturalPerson() {
		return Person.NATURAL.equals(newCustomer.getPerson());
	}
	
	private boolean isAddingCustomer() {
		return null==customerId || customerId.isEmpty();
	}

	public Customer getNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(Customer newCustomer) {
		this.newCustomer = newCustomer;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
