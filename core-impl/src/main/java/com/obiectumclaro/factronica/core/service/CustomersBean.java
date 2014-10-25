/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.access.CustomerEaoBean;
import com.obiectumclaro.factronica.core.model.access.exception.NoEntityException;
import com.obiectumclaro.factronica.core.service.exception.CustomerAdditionException;

/**
 * Vertical service which address {@link Customer}s related business services.
 * 
 * @author iapazmino
 * 
 */
@Stateless
public class CustomersBean {

	@EJB
	private CustomerEaoBean customerEAO;

	/**
	 * Inserts a new customer into the customer's repository.
	 * 
	 * @param customer
	 * @throws CustomerAdditionException
	 */
	public void add(final Customer customer) throws CustomerAdditionException {
		try {
			customerEAO.findByIdType(customer.getIdType(), customer.getId());
			throwCustomerAditionException(customer);
		} catch (NoEntityException nee) {
			customerEAO.save(customer);
		}
	}

	/**
	 * Finds a customer identified by a type of identification and the number of
	 * such an identification.
	 * 
	 * @param idType
	 * @param id
	 * @return
	 */
	public Customer findById(final IdType idType, final String id) {
		return customerEAO.findByIdType(idType, id);
	}
	
	public Customer findById(final String id){
		return customerEAO.findById(id);
	}

	private void throwCustomerAditionException(final Customer customer) throws CustomerAdditionException {
		final String message = "El cliente con %s %s ya existe";
		throw new CustomerAdditionException(String.format(message, customer.getIdType(), customer.getId()));
	}

	public void setCustomerEAO(final CustomerEaoBean eao) {
		this.customerEAO = eao;
	}
	
	public void update(Customer customer){
		customerEAO.update(customer);
	}

	public List<Customer> findAll() {
		return customerEAO.findAll();
	}

}
