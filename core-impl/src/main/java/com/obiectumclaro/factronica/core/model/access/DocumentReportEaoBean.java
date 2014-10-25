/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.DocumentReport;

/**
 * @author rxvallejoc
 *
 */
@Stateless
@LocalBean
public class DocumentReportEaoBean extends BaseEaoBean{
	
	public void store(DocumentReport document){
		entityManager.persist(document);
	}
}
