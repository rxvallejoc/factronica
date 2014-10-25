/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.customers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.service.CustomersBean;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;

/**
 * @author faustodelatog
 * 
 */
@Named
public class ListCustomersBackingBean extends BaseBackingBean {

	// Servicios
	@Inject
	private CustomersBean customersService;

	private List<Customer> customers;

	@PostConstruct
	public void init() {
		customers = customersService.findAll();
	}

	public String printAgremment(IdType idType, String id) {
		String navRule = String.format("/agreements/print?customer=%s:%sfaces-redirect=true", idType, id);
		return navRule;
	}

	public String sendAgremment(IdType idType, String id) {
		String navRule = String.format("/agreements/send?to=%s:%s&template=acuerdo-emailfaces-redirect=true", idType, id);
		return navRule;
	}

	public List<Customer> getCustomers() {
		return customers;
	}
	
	public String goToEdit(final String productId) {
		String navRule = String.format("/customers/customer?customerId=%sfaces-redirect=true", productId);
		return navRule;
	}

}
