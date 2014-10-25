/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;

import com.obiectumclaro.factronica.core.enumeration.AgreementStatus;
import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.mail.model.MailMessage;
import com.obiectumclaro.factronica.core.mail.MailMessageBuilder;
import com.obiectumclaro.factronica.core.mail.SMTPMailProducer;
import com.obiectumclaro.factronica.core.model.Agreement;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.Email;
import com.obiectumclaro.factronica.core.model.access.CustomerEaoBean;
import com.obiectumclaro.factronica.core.model.access.exception.NoEntityException;
import com.obiectumclaro.factronica.core.service.exception.AgreementException;
import com.obiectumclaro.factronica.core.service.exception.AgreementGenerationException;
import com.obiectumclaro.templates.exception.TemplateGenerationException;
import com.obiectumclaro.templates.generator.TemplateGenerator;
import com.obiectumclaro.templates.model.FileFormat;
import com.obiectumclaro.templates.model.SimpleTemplate;
import com.obiectumclaro.templates.model.TemplateType;

/**
 * @author iapazmino
 * 
 */
@Stateless
public class AgreementsBean {

	private static final int TOKEN_AGREEMENT_DURATION = 5;

	@EJB
	private CustomerEaoBean customerEao;
	@EJB
	private SMTPMailProducer mailProducer;
	

	public void send(final Email email) throws MessagingException {
		try {
			TemplateGenerator templateGenerator = new TemplateGenerator(new SimpleTemplate(
					getBytesFromFile("agreementsEmail.vm"), TemplateType.VELOCITY));

			for (Customer customer : email.getDestinataries()) {
				createAgreement(customer);
				
				 final MailMessage multiPartMessage = new MailMessageBuilder()
	                .from("notificaciones@trans-telco.com")
	                .addTo(customer.getEmail())
	                .subject("Facturacion Electronica")
	                .body(createCustomerMailMessage(email.getText(), customer, templateGenerator))
	                .contentType("text/html").hasAttachment(Boolean.FALSE).build();
	            mailProducer.queueMailForDelivery(multiPartMessage);
			}
		} catch (IOException e) {
		}

	}

	private String generateToken() {
		SecureRandom random = new SecureRandom();
		return String.format("%s%s", System.currentTimeMillis(), new BigInteger(150, random).toString(32));
	}

	private void createAgreement(Customer customer) {
		Agreement agreement;
		agreement = customer.getAgreement() == null ? new Agreement() : customer.getAgreement();
		agreement.setDuration(TOKEN_AGREEMENT_DURATION);
		agreement.setLastDate(new Date());
		agreement.setStatus(AgreementStatus.SEND);
		agreement.setToken(generateToken());

		customer.setAgreement(agreement);
		customerEao.update(customer);

	}

	private String createCustomerMailMessage(String message, Customer customer, TemplateGenerator tg) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cliente", customer.getLegalName());
		parameters.put("message", message);
		parameters.put("tipoDoc", customer.getIdType());
		parameters.put("doc", customer.getId());
		parameters.put("token", customer.getAgreement().getToken());
		parameters.put("raizURL", "http://184.106.156.189:8180");
		parameters.put("emisor", "Integral Data S.A.");

		try {
			return new String(tg.generate(parameters));
		} catch (TemplateGenerationException e) {
			return message;
		}
	}

	public Properties readSmtpConfig() {
		try {
			final Properties smtpConfig = new Properties();
			final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
			final InputStream file = ccl.getResourceAsStream("factronica.properties");
			smtpConfig.load(file);
			return smtpConfig;
		} catch (IOException e) {
			throw new RuntimeException("Failed to read configuration file factronica.properties", e);
		}
	}


	public byte[] generateCustomerAgreement(final IdType customerIdType, String customerId) throws AgreementGenerationException {
		try {
			Customer customer = customerEao.findByIdType(customerIdType, customerId);

			TemplateGenerator templateGenerator = new TemplateGenerator(new SimpleTemplate("agreementTemplate.jasper",
					TemplateType.JASPER));

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("customerName", customer.getName());
			parameters.put("customerEmail", customer.getEmail());
			parameters.put("customerIdType", customer.getIdType().name());
			parameters.put("customerId", customer.getId());
			return templateGenerator.generate(parameters, FileFormat.PDF);

		} catch (NoEntityException e) {
			throw new AgreementGenerationException(String.format("No existe un cliente con %s %s", customerIdType, customerId), e);
		} catch (TemplateGenerationException e) {
			throw new AgreementGenerationException(e);
		}
	}

	private byte[] getBytesFromFile(String fileName) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		int size = is.available();
		byte[] bytes = new byte[size];
		is.read(bytes);
		return bytes;
	}

	public void setEaos(CustomerEaoBean customerEao) {
		this.customerEao = customerEao;
	}

	public void acceptAgreement(final IdType customerIdType, String customerId, String token) throws AgreementException {
		try {
			Customer customer = customerEao.findByIdType(customerIdType, customerId);
			validateAgreement(customer.getAgreement(), token);
			customer.getAgreement().setStatus(AgreementStatus.ACCEPTED);
			customer.getAgreement().setLastDate(new Date());
			customerEao.update(customer);
		} catch (NoEntityException e) {
			throw new AgreementException(String.format("No existe un cliente con %s %s", customerIdType, customerId), e);
		}
	}

	// TODO cambiar el codigo del metodo a un validador
	private void validateAgreement(Agreement agreement, String actualToken) throws AgreementException {
		boolean isValid = true;
		StringBuilder message = new StringBuilder();
		if (agreement == null) {
			isValid = false;
			message.append("No se ha generado un pre acuerdo para el cliente");
		} else if (agreement.getStatus().compareTo(AgreementStatus.ACCEPTED) == 0) {
			isValid = false;
			message.append("El cliente ya cuenta con un acuerdo aceptado");
		} else {
			int actualDuration = (int) ((new Date().getTime() - agreement.getLastDate().getTime()) / 86400000);
			if (actualDuration > agreement.getDuration()) {
				isValid = false;
				message.append("El tiempo para aceptar el acuerdo ha expirado\n");
			}
			if (actualToken.compareTo(agreement.getToken()) != 0) {
				isValid = false;
				message.append("El token no coincide con el generado");
			}
		}

		if (!isValid) {
			throw new AgreementException(message.toString());
		}

	}

}
