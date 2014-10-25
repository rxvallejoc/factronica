/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.obiectumclaro.factronica.core.enumeration.AgreementStatus;
import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.model.Agreement;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.Email;
import com.obiectumclaro.factronica.core.model.access.CustomerEaoBean;
import com.obiectumclaro.factronica.core.service.exception.AgreementException;
import com.obiectumclaro.factronica.core.service.exception.AgreementGenerationException;

/**
 * @author iapazmino
 * 
 */
public class TestAgreementsBean {

	private static Customer customer;

	
	private AgreementsBean agreements;
	private Email email;
	private CustomerEaoBean customerEao;

	@Before
	public void setup() {
		customer = new Customer(IdType.CEDULA, "1722452365", "José Terán");
		customer.setEmail("dev-tools@obiectumclaro.com");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		customer.setAgreement(new Agreement(c.getTime(), AgreementStatus.SEND,
				"123", 36));
		agreements = new AgreementsBean();
		email = new Email("subject", "html text");
		email.addDestinatary(customer);
		customerEao = mock(CustomerEaoBean.class);
		agreements.setEaos(customerEao);
	}

	@Test
	@Ignore
	public void shouldRespondeOK() {
		try {
			agreements.send(email);
		} catch (MessagingException e) {
			fail(e.getMessage());
		}
	}

	@Test
	@Ignore
	public void shouldGenerateAgreement() {
		when(customerEao.findByIdType(customer.getIdType(), customer.getId()))
				.thenReturn(customer);
		try {
			byte[] agreementDocument = agreements.generateCustomerAgreement(
					customer.getIdType(), customer.getId());
			assertTrue(agreementDocument.length > 0);
		} catch (AgreementGenerationException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void shouldAcceptAgreement() {
		when(customerEao.findByIdType(customer.getIdType(), customer.getId()))
				.thenReturn(customer);
		try {
			agreements.acceptAgreement(customer.getIdType(), customer.getId(),
					"123");
		} catch (AgreementException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void shouldNotAcceptAgreementAccepted() {
		customer.getAgreement().setStatus(AgreementStatus.ACCEPTED);
		when(customerEao.findByIdType(customer.getIdType(), customer.getId()))
				.thenReturn(customer);
		try {
			agreements.acceptAgreement(customer.getIdType(), customer.getId(),
					"123");
			fail("No deberia permitir aceptar el acuerdo");
		} catch (AgreementException e) {
			assertTrue("El cliente ya cuenta con un acuerdo aceptado"
					.compareTo(e.getMessage()) == 0);
		}
	}

	@Test
	@Ignore
	public void shouldNotAcceptAgreementExpired() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, -37);
		customer.getAgreement().setStatus(AgreementStatus.SEND);
		customer.getAgreement().setLastDate(c.getTime());
		when(customerEao.findByIdType(customer.getIdType(), customer.getId()))
				.thenReturn(customer);
		try {
			agreements.acceptAgreement(customer.getIdType(), customer.getId(),
					"123");
			fail("No deberia permitir aceptar el acuerdo");
		} catch (AgreementException e) {
			assertTrue("El tiempo para aceptar el acuerdo ha expirado\n"
					.compareTo(e.getMessage()) == 0);
		}
	}

	@Test
	@Ignore
	public void shouldNotAcceptAgreementExpiredTokenError() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, -37);
		customer.getAgreement().setStatus(AgreementStatus.SEND);
		customer.getAgreement().setLastDate(c.getTime());
		when(customerEao.findByIdType(customer.getIdType(), customer.getId()))
				.thenReturn(customer);
		try {
			agreements.acceptAgreement(customer.getIdType(), customer.getId(),
					"1234");
			fail("No deberia permitir aceptar el acuerdo");
		} catch (AgreementException e) {
			assertTrue("El tiempo para aceptar el acuerdo ha expirado\nEl token no coincide con el generado"
					.compareTo(e.getMessage()) == 0);
		}
	}

	@Test
	public void shouldNotAcceptNullAgreement() {
		customer.setAgreement(null);
		when(customerEao.findByIdType(customer.getIdType(), customer.getId()))
				.thenReturn(customer);
		try {
			agreements.acceptAgreement(customer.getIdType(), customer.getId(),
					"123");
			fail("No deberia permitir aceptar el acuerdo");
		} catch (AgreementException e) {
			assertTrue("No se ha generado un pre acuerdo para el cliente"
					.compareTo(e.getMessage()) == 0);
		}
	}

}
