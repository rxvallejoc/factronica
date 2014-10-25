/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.agreements;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.obiectumclaro.factronica.core.model.EmailTemplate;
import com.obiectumclaro.factronica.core.service.TemplatesBean;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 * Backing bean for the agreement's template edition page.
 * 
 * @author ipazmino
 *
 */
@ManagedBean
@ViewScoped
public class AgreementTemplateBackingBean {

	private EmailTemplate template;
	
	@Inject
	TemplatesBean templates;
	
	@PostConstruct
	public void findTemplate() {
		template = templates.findByKeyword("acuerdo");
	}
	
	public String save() {
		templates.save(template);
		FacesMessageHelper.addInfo("Templates", "El acuerdo para la emisi\u00f3n de comprobantes.");
		return "/customers/list?faces-redirect=true";
	}

	public EmailTemplate getTemplate() {
		return template;
	}

	public void setTemplate(EmailTemplate template) {
		this.template = template;
	}

}
