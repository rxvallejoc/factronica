/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.agreements;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.service.AgreementsBean;
import com.obiectumclaro.factronica.core.service.exception.AgreementGenerationException;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;

/**
 * @author faustodelatog
 * 
 */
@Named
public class PrintAgreementBean extends BaseBackingBean {

	private IdType customerIdType;
	private String customerId;

	private String message;

	@Inject
	private AgreementsBean agreements;

	@PostConstruct
	public void init() {
		System.out.println("****************************************************");
		String customer = getRequestParameter("customer");
		System.out.println("CUSTOMER: " + customer);
		String[] customerId = new String[2];
		if (customer != null && (customerId = customer.split(":")).length == 2) {
			this.customerIdType = IdType.valueOf(customerId[0]);
			this.customerId = customerId[1];
			printAgreement(this.customerIdType, this.customerId);
		} else {
			message = "Los parametros se encuentran mal definidos, se espera customer";
		}
	}

	private void printAgreement(final IdType idType, String id) {
		try {
			System.out.println("imprimiendo.................... " + idType + " " + id);
			byte[] agreement = agreements.generateCustomerAgreement(idType, id);
			downloadFile(agreement, "application/pdf", String.format("acuerdo_%s_%s", idType, id));
			message = "Acuerdo generado";
		} catch (AgreementGenerationException e) {
			message = String.format("No se puede generar el acuerdo\n %s", e.getMessage());
		} catch (IOException e) {
			message = String.format("No se puede descargar el acuerdo\n %s", e.getMessage());
		}
	}

	public IdType getCustomerIdType() {
		return customerIdType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getMessage() {
		return message;
	}

}
