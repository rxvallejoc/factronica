/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.agreements;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.Email;
import com.obiectumclaro.factronica.core.model.EmailTemplate;
import com.obiectumclaro.factronica.core.service.AgreementsBean;
import com.obiectumclaro.factronica.core.service.CustomersBean;
import com.obiectumclaro.factronica.core.service.TemplatesBean;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;

/**
 * @author iapazmino
 * 
 */
@Named
public class SendAgreementBean extends BaseBackingBean {

	@Inject
	private CustomersBean customers;
	@Inject
	private TemplatesBean templates;
	@Inject
	private AgreementsBean agreements;

	private String toIds;
	private String template;

	@Produces
	@Named
	private Email newEmail = new Email();

	public void searchDestinataries(final ComponentSystemEvent sysEvent) {
		if (null != toIds && !toIds.isEmpty()) {
			final List<String> ids = Arrays.asList(toIds.split(","));
			String[] idArray = new String[2];
			Customer destinatary = null;
			for (String id : ids) {
				idArray = id.split(":");
				destinatary = customers.findById(IdType.valueOf(idArray[0]), idArray[1]);
				newEmail.addDestinatary(destinatary);
			}
		}
	}

	public void searchTemplate(final ComponentSystemEvent sysEvent) {
		if (null != template && !template.isEmpty()) {
			final EmailTemplate emailTemplate = templates
					.findByKeyword(template);
			newEmail.setSubject(emailTemplate.getSubject());
			newEmail.setText(emailTemplate.getText());
		}
	}

	public String send() {
		searchDestinataries(null);
		FacesMessage.Severity severity = null;
		String navRule = null;
		String msg = null;
		try {
			agreements.send(newEmail);
			severity = FacesMessage.SEVERITY_INFO;
			navRule = "/customers/list?faces-redirect=true";
			msg = "El mensaje fue enviado al cliente";
		} catch (MessagingException e) {
			e.printStackTrace();
			severity = FacesMessage.SEVERITY_ERROR;
			navRule = null;
			msg = "Existe un error en el envio del mensaje " + e.getMessage();
		}
		final FacesMessage message = new FacesMessage(severity, msg, null);
		getFacesContext().addMessage(null, message);
		getFlashMap().setKeepMessages(true);
		return navRule;
	}

	public Email getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(Email newEmail) {
		this.newEmail = newEmail;
	}

	public String getToIds() {
		return toIds;
	}

	public void setToIds(String toIds) {
		this.toIds = toIds;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setCustomers(CustomersBean customers) {
		this.customers = customers;
	}

	public void setTemplates(TemplatesBean templates) {
		this.templates = templates;
	}

	public void setAgreements(AgreementsBean agreements) {
		this.agreements = agreements;
	}

}
