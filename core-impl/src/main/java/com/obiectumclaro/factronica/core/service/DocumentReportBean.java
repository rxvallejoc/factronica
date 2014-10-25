/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.DocumentReport;
import com.obiectumclaro.factronica.core.model.access.DocumentReportEaoBean;

/**
 * @author rxvallejoc
 *
 */
@Stateless
@LocalBean
public class DocumentReportBean {

	@EJB
	private DocumentReportEaoBean documentEaoBean;
	
	
	public void store(DocumentReport document){
		 documentEaoBean.store(document);
	}
}
