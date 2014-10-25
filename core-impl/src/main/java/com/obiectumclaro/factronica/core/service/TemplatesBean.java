/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.EmailTemplate;
import com.obiectumclaro.factronica.core.model.access.EmailTemplateEaoBean;

/**
 * @author iapazmino
 *
 */
@Stateless
public class TemplatesBean {
	
	@EJB
	private EmailTemplateEaoBean templateEao;

	public EmailTemplate findByKeyword(final String keyword) {
		return templateEao.findByKeyword(keyword);
	}
	
	public void save(final EmailTemplate template) {
		templateEao.save(template);
	}

}
