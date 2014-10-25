/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.obiectumclaro.factronica.core.model.EmailTemplate;
import com.obiectumclaro.factronica.core.model.access.exception.NoEntityException;

/**
 * @author iapazmino
 * 
 */
@Stateless
public class EmailTemplateEaoBean {

	@PersistenceContext
	private EntityManager entityManager;

	public EmailTemplate findByKeyword(final String keyword) {
		final TypedQuery<EmailTemplate> query = entityManager.createNamedQuery(
				"emailTemplate.findByKeyword", EmailTemplate.class);
		query.setParameter("keyword", keyword);
		try {
			return query.getSingleResult();
		} catch (NoResultException nre) {
			throw new NoEntityException(nre);
		}
	}
	
	public EmailTemplate save(final EmailTemplate template) {
		return entityManager.merge(template);
	}

}
