/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.Document;
import com.obiectumclaro.factronica.core.model.access.DocumentEaoBean;

/**
 * @author rxvallejoc
 *
 */
@Stateless
@LocalBean
public class DocumentBean {

	@EJB
	private DocumentEaoBean documentEaoBean;
	
	
	public void store(Document document){
		 documentEaoBean.store(document);
	}
}
