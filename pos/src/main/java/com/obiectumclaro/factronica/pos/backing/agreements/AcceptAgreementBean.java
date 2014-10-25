/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.agreements;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.model.EmailTemplate;
import com.obiectumclaro.factronica.core.service.AgreementsBean;
import com.obiectumclaro.factronica.core.service.TemplatesBean;
import com.obiectumclaro.factronica.core.service.exception.AgreementException;

/**
 * @author faustodelatog
 * 
 */
@ManagedBean
@ViewScoped
public class AcceptAgreementBean {

	private String customer;
	private String token;

	private String message;
	private String agreementText;

	@Inject
	private AgreementsBean agreements;
	@Inject
	private TemplatesBean templates;

	@PostConstruct
	public void init() {
		final EmailTemplate agreement = templates.findByKeyword("acuerdo");
		agreementText = agreement.getText();
	}

	public void acceptAgreement() {
		try {
			IdType customerIdType = null;
			String customerId = null;
			String[] credentials = customer.split(":");
			if (credentials.length == 2) {
				customerIdType = IdType.valueOf(credentials[0]);
				customerId = credentials[1];
			} else {
				message = "Los parametros se encuentran mal definidos, se espera customer y token";
			}
			agreements.acceptAgreement(customerIdType, customerId, token);
			message = "Acuerdo Aceptado Correctamente";
		} catch (AgreementException e) {
			message = e.getMessage();
		}
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getMessage() {
		return message;
	}

	public String getAgreementText() {
		return agreementText;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

}
