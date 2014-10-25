/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import java.util.List;

import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.AdditionalProductInformation;

/**
 * @author fausto
 * 
 */
@Stateless
public class AdditionalProductInformationEaoBean extends BaseEaoBean {

	public List<AdditionalProductInformation> findByProduct(Long productId) {
		return getResultListFromNamedQuery(AdditionalProductInformation.class, "AdditionalProductInformation.findByProduct",
				productId);
	}
}
