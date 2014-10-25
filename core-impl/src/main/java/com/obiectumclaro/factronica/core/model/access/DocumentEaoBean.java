/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.Document;

/**
 * @author rxvallejoc
 *
 */
@Stateless
@LocalBean
public class DocumentEaoBean extends BaseEaoBean{
	
	public void store(Document document){
		entityManager.persist(document);
	}
}
