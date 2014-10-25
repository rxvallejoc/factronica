/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.access.CustomerEaoBean;
import com.obiectumclaro.factronica.core.model.access.exception.NoEntityException;
import com.obiectumclaro.factronica.core.service.exception.CustomerAdditionException;

/**
 * Test cases to verify {@link CustomersBean}.
 * 
 * @author iapazmino
 *
 */
public class TestCustomersBean {
	
	private static Customer customer;
	
	@BeforeClass
	public static void setupCustomer() {
		customer = new Customer();
		customer.setIdType(IdType.RUC);
		customer.setId("1176752345001");
	}

	private CustomersBean customers;
	private CustomerEaoBean eao;
	
	@Before
	public void setup() {
		customers = new CustomersBean();
		eao = mock(CustomerEaoBean.class);
		customers.setCustomerEAO(eao);
	}
	
	@Test
	public void shouldAddNewCustomer() {
		when(eao.findByIdType(customer.getIdType(), customer.getId())).thenThrow(new NoEntityException());
		
		try {
			customers.add(customer);
		} catch (CustomerAdditionException cae) {
			fail(cae.getMessage());
		}
	}
	
//	@Test(expected = CustomerAditionException.class)
//	public void shouldNotAddExistingCustomer() throws CustomerAditionException {
//		when(eao.findById(customer.getIdType(), customer.getId())).thenReturn(customer);
//		
//		customers.add(customer);
//	}
	
}
